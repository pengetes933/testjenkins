package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.core.service.OtpService;
import id.overridestudio.tixfestapi.core.service.RedisService;
import id.overridestudio.tixfestapi.feature.usermanagement.constant.VerificationStatus;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.EventOrganizerRegisterRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.EventOrganizerUpdateRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.SearchEventOrganizerRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.VerifyOtpRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.EventOrganizerResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.FileResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Customer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizerImage;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.EventOrganizerRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.EventOrganizerImageService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.EventOrganizerService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.UserAccountService;
import id.overridestudio.tixfestapi.feature.usermanagement.specification.EventOrganizerSpecification;
import id.overridestudio.tixfestapi.util.JsonUtil;
import id.overridestudio.tixfestapi.util.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventOrganizerServiceImpl implements EventOrganizerService {
    private final EventOrganizerRepository eventOrganizerRepository;
    private final UserAccountService userAccountService;
    private final EventOrganizerImageService eventOrganizerImageService;
    private final OtpService otpService;
    private final RedisService redisService;

    @Value("${storage.url}")
    private String storageUrl;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registration(EventOrganizerRegisterRequest request) {
        UserAccount existingUserAccount = userAccountService.findByEmail(request.getEmail()).orElse(null);

        if (existingUserAccount != null && existingUserAccount.getDeletedAt() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        if (!userAccountService.validatePassword(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Format password not valid");
        }

        String registerRequest = JsonUtil.toJson(request);
        redisService.save(getRedisKeyForRegister(request.getEmail()), registerRequest, Duration.ofMinutes(5));

        otpService.sendOtp(request.getEmail());
    }

    private String getRedisKeyForRegister(String email) {
        return "RegisterEventOrganizerRequest:" + email;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EventOrganizerResponse verifyRegister(VerifyOtpRequest request) {
        if (!otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP verification failed");
        }
        otpService.deleteOtp(request.getEmail());

        String redisKey = getRedisKeyForRegister(request.getEmail());
        String jsonRequest = redisService.get(redisKey);
        if (jsonRequest == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration data not found or expired");
        }
        EventOrganizerRegisterRequest registerRequest = JsonUtil.fromJson(jsonRequest, EventOrganizerRegisterRequest.class);

        redisService.delete(redisKey);

        UserAccount existingUserAccount = userAccountService.findByEmail(request.getEmail()).orElse(null);
        UserAccount userAccount;
        EventOrganizer eventOrganizer;

        if(existingUserAccount == null){
            userAccount = UserAccount.builder()
                    .email(registerRequest.getEmail())
                    .password(registerRequest.getPassword())
                    .isDeleted(false)
                    .build();
            userAccountService.create(userAccount, "ROLE_EVENT_ORGANIZER");
            eventOrganizer = EventOrganizer.builder()
                    .name(registerRequest.getName())
                    .description(registerRequest.getDescription())
                    .status(VerificationStatus.DRAFT)
                    .isVerified(false)
                    .totalFollower(0)
                    .userAccount(userAccount)
                    .build();
            eventOrganizerRepository.saveAndFlush(eventOrganizer);
        } else {
            userAccount = existingUserAccount;
            userAccount.setPassword(registerRequest.getPassword());
            userAccount.setDeletedAt(null);

            userAccountService.save(userAccount);
            if(userAccount.getEventOrganizer() == null) {
                eventOrganizer = EventOrganizer.builder()
                        .name(registerRequest.getName())
                        .description(registerRequest.getDescription())
                        .status(VerificationStatus.DRAFT)
                        .isVerified(false)
                        .totalFollower(0)
                        .userAccount(userAccount)
                        .build();
                eventOrganizerRepository.saveAndFlush(eventOrganizer);
            } else {
                eventOrganizer = userAccount.getEventOrganizer();

                eventOrganizer.setName(registerRequest.getName());
                eventOrganizer.setDescription(registerRequest.getDescription());
                eventOrganizer.setCreatedAt(LocalDateTime.now());
                eventOrganizer.setUpdatedAt(null);
                eventOrganizer.setDeletedAt(null);

                eventOrganizerRepository.save(eventOrganizer);
            }
        }

        return toResponse(eventOrganizer);
    }

    @Transactional(readOnly = true)
    @Override
    public EventOrganizerResponse getById(String id) {
        return toResponse(getOne(id));
    }

    @Transactional(readOnly = true)
    @Override
    public EventOrganizer getOne(String id) {
        return eventOrganizerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @Override
    public Page<EventOrganizerResponse> getAll(SearchEventOrganizerRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<EventOrganizer> specification = EventOrganizerSpecification.getSpecification(request);
        Page<EventOrganizer> eventOrganizerPage = eventOrganizerRepository.findAll(specification, pageable);
        return eventOrganizerPage.map(this::toResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EventOrganizerResponse updateById(String id, EventOrganizerUpdateRequest request, MultipartFile multipartFile) {
        EventOrganizer eventOrganizer = getOne(id);
        if (multipartFile != null && !multipartFile.isEmpty()) {
            EventOrganizerImage eventOrganizerImage = eventOrganizerImageService.save(multipartFile, "eo-pp", eventOrganizer);
            eventOrganizer.setProfilePicture(eventOrganizerImage);
        }
        eventOrganizer.setName(request.getName());
        eventOrganizer.setDescription(request.getDescription());
        eventOrganizer.setUpdatedAt(LocalDateTime.now());
        eventOrganizerRepository.save(eventOrganizer);
        return toResponse(eventOrganizer);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void requestVerify(String id) {
        EventOrganizer eventOrganizer = getOne(id);
        if(!eventOrganizer.getStatus().equals(VerificationStatus.DRAFT)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not request verify non draft status");
        }
        checkingCompleteness(eventOrganizer);
        eventOrganizer.setStatus(VerificationStatus.REQUEST);
        eventOrganizerRepository.save(eventOrganizer);
        //TODO notification
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public EventOrganizerResponse verifyEventOrganizer(String id) {
        EventOrganizer eventOrganizer = getOne(id);
        if(!eventOrganizer.getStatus().equals(VerificationStatus.REQUEST)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no verification request");
        }
        checkingCompleteness(eventOrganizer);
        eventOrganizer.setStatus(VerificationStatus.VERIFIED);
        eventOrganizer.setIsVerified(true);
        eventOrganizerRepository.save(eventOrganizer);
        return toResponse(eventOrganizer);
    }

    private void checkingCompleteness (EventOrganizer eventOrganizer){
        if (eventOrganizer.getAddress() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is missing!");
        }
        if (eventOrganizer.getResponsiblePersons().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Responsible person is missing!");
        }
        if (eventOrganizer.getBankAccount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bank account is missing!");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        EventOrganizer eventOrganizer = getOne(id);
        eventOrganizer.setDeletedAt(LocalDateTime.now());
        eventOrganizerRepository.save(eventOrganizer);

        userAccountService.deleteAccount(eventOrganizer.getUserAccount().getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void toggleFollow(String id) {
        EventOrganizer eventOrganizer = getOne(id);

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = userAccount.getCustomer();

        if (eventOrganizer.getFollowers().contains(customer)) {
            eventOrganizer.getFollowers().remove(customer);
            eventOrganizer.setTotalFollower(eventOrganizer.getTotalFollower() - 1);
        } else {
            eventOrganizer.getFollowers().add(customer);
            eventOrganizer.setTotalFollower(eventOrganizer.getTotalFollower() + 1);
        }

        eventOrganizerRepository.save(eventOrganizer);
    }

    @Override
    public boolean existByEventOrganizerIdAndUserId(String eventOrganizerId, String userId) {
        return eventOrganizerRepository.existsByIdAndUserAccountId(eventOrganizerId, userId);
    }

    private EventOrganizerResponse toResponse(EventOrganizer eventOrganizer){
        FileResponse profilePicture;

        if (eventOrganizer.getProfilePicture() != null) {
            profilePicture = FileResponse.builder()
                    .id(eventOrganizer.getProfilePicture().getId())
                    .url(eventOrganizer.getProfilePicture().getUrl().replace("{storage_url}", storageUrl))
                    .build();
        } else {
            profilePicture = null;
        }
        return EventOrganizerResponse.builder()
                .id(eventOrganizer.getId())
                .name(eventOrganizer.getName())
                .description(eventOrganizer.getDescription())
                .isVerified(eventOrganizer.getIsVerified())
                .eventOrganizerAddressId(eventOrganizer.getAddress() != null ? eventOrganizer.getAddress().getId() : null)
                .bankAccountId(eventOrganizer.getBankAccount() != null ? eventOrganizer.getBankAccount().getId() : null)
                .totalFollower(eventOrganizer.getTotalFollower())
                .profilePicture(profilePicture)
                .build();
    }
}
