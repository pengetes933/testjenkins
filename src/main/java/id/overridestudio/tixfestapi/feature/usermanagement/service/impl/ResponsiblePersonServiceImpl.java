package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.core.service.AesService;
import id.overridestudio.tixfestapi.feature.usermanagement.constant.VerificationStatus;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.AddressRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ResponsiblePersonRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.ResponsiblePersonResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.ResponsiblePerson;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.ResponsiblePersonRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.EventOrganizerService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.ResponsiblePersonAddressService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.ResponsiblePersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponsiblePersonServiceImpl implements ResponsiblePersonService {
    private final ResponsiblePersonRepository responsiblePersonRepository;
    private final EventOrganizerService eventOrganizerService;
    private final ResponsiblePersonAddressService responsiblePersonAddressService;
    private final AesService aesService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponsiblePersonResponse create(String eventOrganizerId, ResponsiblePersonRequest request) {
        EventOrganizer eventOrganizer = eventOrganizerService.getOne(eventOrganizerId);
        String encryptedNik = aesService.encrypt(request.getNik());
        ResponsiblePerson responsiblePerson = ResponsiblePerson.builder()
                .eventOrganizer(eventOrganizer)
                .name(request.getName())
                .nik(encryptedNik)
                .phoneNumber(request.getPhoneNumber())
                .status(VerificationStatus.DRAFT)
                .isVerified(false)
                .build();
        ResponsiblePerson savedResponsiblePerson = createOne(responsiblePerson);

        AddressRequest addressRequest = AddressRequest.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .province(request.getProvince())
                .postalCode(request.getPostalCode())
                .build();

        savedResponsiblePerson.setAddress(responsiblePersonAddressService.create(addressRequest, savedResponsiblePerson));
        return toResponse(responsiblePersonRepository.save(savedResponsiblePerson));
    }

    private ResponsiblePerson createOne (ResponsiblePerson responsiblePerson){
        if (responsiblePersonRepository.existsByIdAndNik(responsiblePerson.getId(), responsiblePerson.getNik())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bank already registered!");
        }
        if (responsiblePersonRepository.existsByPhoneNumber(responsiblePerson.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already in use!");
        }
        return responsiblePersonRepository.saveAndFlush(responsiblePerson);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponsiblePersonResponse getById(String id) {
        return toResponse(getOne(id));
    }

    private ResponsiblePerson getOne(String id) {
        return responsiblePersonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @Transactional(readOnly = true)
    @Override
    public ResponsiblePersonResponse updateById(String id, ResponsiblePersonRequest request) {
        ResponsiblePerson responsiblePerson = getOne(id);

        AddressRequest addressRequest = AddressRequest.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .province(request.getProvince())
                .postalCode(request.getPostalCode())
                .build();

        responsiblePerson.setName(request.getName());
        responsiblePerson.setNik(aesService.encrypt(request.getNik()));
        responsiblePerson.setPhoneNumber(request.getPhoneNumber());
        responsiblePerson.setAddress(responsiblePersonAddressService.update(addressRequest, responsiblePerson));
        responsiblePerson.setUpdatedAt(LocalDateTime.now());
        return toResponse(responsiblePersonRepository.save(responsiblePerson));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void requestVerify(String id) {
        ResponsiblePerson responsiblePerson = getOne(id);
        if(!responsiblePerson.getStatus().equals(VerificationStatus.DRAFT)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not request verify non draft status");
        }
        responsiblePerson.setStatus(VerificationStatus.REQUEST);
        responsiblePersonRepository.save(responsiblePerson);
        //TODO notification
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponsiblePersonResponse verifyResponsiblePerson(String id) {
        ResponsiblePerson responsiblePerson = getOne(id);
        if(!responsiblePerson.getStatus().equals(VerificationStatus.REQUEST)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no verification request");
        }
        responsiblePerson.setStatus(VerificationStatus.VERIFIED);
        responsiblePerson.setIsVerified(true);
        return toResponse(responsiblePersonRepository.save(responsiblePerson));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ResponsiblePersonResponse> getAllByEventOrganizer(String eventOrganizerId) {
        return responsiblePersonRepository.findAllByEventOrganizerId(eventOrganizerId).stream().map(this::toResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        ResponsiblePerson responsiblePerson = getOne(id);
        String eventOrganizerId = responsiblePerson.getEventOrganizer().getId();
        long responsiblePersonCount = responsiblePersonRepository.countByEventOrganizerIdAndDeletedAtIsNull(eventOrganizerId);

        if (responsiblePersonCount <= 1) {
            throw new IllegalStateException("Cannot delete the last responsible person for this event organizer. There must be at least one responsible person remaining.");
        }

        responsiblePerson.setDeletedAt(LocalDateTime.now());
        responsiblePersonRepository.save(responsiblePerson);
    }

    @Override
    public boolean existByResponsiblePersonIdAndEventOrganizerIdAndUserId(String responsiblePersonId, String eventOrganizerId, String userId) {
        return responsiblePersonRepository.existsByIdAndEventOrganizerIdAndEventOrganizer_UserAccount_Id(responsiblePersonId, eventOrganizerId, userId);
    }

    private ResponsiblePersonResponse toResponse (ResponsiblePerson responsiblePerson) {
        return ResponsiblePersonResponse.builder()
                .id(responsiblePerson.getId())
                .name(responsiblePerson.getName())
                .nik(aesService.decrypt(responsiblePerson.getNik()))
                .phoneNumber(responsiblePerson.getPhoneNumber())
                .street(responsiblePerson.getAddress().getStreet())
                .city(responsiblePerson.getAddress().getCity())
                .province(responsiblePerson.getAddress().getProvince())
                .postalCode(responsiblePerson.getAddress().getPostalCode())
                .status(responsiblePerson.getStatus().name())
                .isVerified(responsiblePerson.getIsVerified())
                .build();
    }
}
