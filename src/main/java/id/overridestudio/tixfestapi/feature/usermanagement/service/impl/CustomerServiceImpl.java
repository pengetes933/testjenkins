package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.core.service.OtpService;
import id.overridestudio.tixfestapi.core.service.RedisService;
import id.overridestudio.tixfestapi.feature.usermanagement.constant.Gender;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.CustomerRegisterRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.CustomerUpdateRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.SearchCustomerRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.VerifyOtpRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.CustomerResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Customer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.UserAccount;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.CustomerRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.CustomerService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.UserAccountService;
import id.overridestudio.tixfestapi.feature.usermanagement.specification.CustomerSpecification;
import id.overridestudio.tixfestapi.util.DateUtil;
import id.overridestudio.tixfestapi.util.JsonUtil;
import id.overridestudio.tixfestapi.util.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserAccountService userAccountService;
    private final OtpService otpService;
    private final RedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registration(CustomerRegisterRequest request) {
        DateUtil.formatValidation(request.getDob());

        UserAccount existingUserAccount = userAccountService.findByEmail(request.getEmail()).orElse(null);

        if (existingUserAccount != null && existingUserAccount.getDeletedAt() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        if (customerRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number already in use!");
        }

        if (!userAccountService.validatePassword(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Format password not valid");
        }

        String registerRequest = JsonUtil.toJson(request);
        redisService.save(getRedisKeyForRegister(request.getEmail()), registerRequest, Duration.ofMinutes(5));

        otpService.sendOtp(request.getEmail());
    }

    private String getRedisKeyForRegister(String email) {
        return "RegisterCustomerRequest:" + email;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse verifyRegister(VerifyOtpRequest request) {
        if (!otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP verification failed");
        }
        otpService.deleteOtp(request.getEmail());

        String redisKey = getRedisKeyForRegister(request.getEmail());
        String jsonRequest = redisService.get(redisKey);
        if (jsonRequest == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration data not found or expired");
        }
        CustomerRegisterRequest registerRequest = JsonUtil.fromJson(jsonRequest, CustomerRegisterRequest.class);

        redisService.delete(redisKey);

        UserAccount existingUserAccount = userAccountService.findByEmail(request.getEmail()).orElse(null);
        UserAccount userAccount;
        Customer customer;

        if(existingUserAccount == null){
            userAccount = UserAccount.builder()
                    .email(registerRequest.getEmail())
                    .password(registerRequest.getPassword())
                    .isDeleted(false)
                    .build();
            userAccountService.create(userAccount, "ROLE_CUSTOMER");
            customer = Customer.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .dateOfBirth(DateUtil.stringToLocalDateTime(registerRequest.getDob()))
                    .gender(Gender.findByDesc(registerRequest.getGender()))
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .userAccount(userAccount)
                    .build();
            customerRepository.saveAndFlush(customer);
        } else {
            userAccount = existingUserAccount;
            userAccount.setPassword(registerRequest.getPassword());
            userAccount.setDeletedAt(null);

            userAccountService.save(userAccount);

            if(userAccount.getCustomer() == null) {
                customer = Customer.builder()
                        .firstName(registerRequest.getFirstName())
                        .lastName(registerRequest.getLastName())
                        .dateOfBirth(DateUtil.stringToLocalDateTime(registerRequest.getDob()))
                        .gender(Gender.findByDesc(registerRequest.getGender()))
                        .phoneNumber(registerRequest.getPhoneNumber())
                        .userAccount(userAccount)
                        .build();
                customerRepository.saveAndFlush(customer);
            } else {
                customer = userAccount.getCustomer();

                customer.setFirstName(registerRequest.getFirstName());
                customer.setLastName(registerRequest.getLastName());
                customer.setGender(Gender.findByDesc(registerRequest.getGender()));
                customer.setDateOfBirth(DateUtil.stringToLocalDateTime(registerRequest.getDob()));
                customer.setPhoneNumber(registerRequest.getPhoneNumber());
                customer.setCreatedAt(LocalDateTime.now());
                customer.setUpdatedAt(null);
                customer.setDeletedAt(null);

                customerRepository.save(customer);
            }
        }

        return toResponse(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse getById(String id) {
        return toResponse(getOne(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getOne(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @Override
    public Page<CustomerResponse> getAll(SearchCustomerRequest request) {
        Sort sortBy = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sortBy);
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);
        Page<Customer> customerPage = customerRepository.findAll(specification,pageable);
        return customerPage.map(this::toResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse updateById(String id, CustomerUpdateRequest request) {
        Customer customer = getOne(id);
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setGender(Gender.findByDesc(request.getGender()));
        customer.setDateOfBirth(DateUtil.stringToLocalDateTime(request.getDob()));
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
        return toResponse(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Customer customer = getOne(id);
        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);
        userAccountService.deleteAccount(customer.getUserAccount().getId());
    }

    @Override
    public boolean existByCustomerIdAndUserId(String customerId, String userId) {
        return customerRepository.existsByIdAndUserAccountId(customerId, userId);
    }

    private CustomerResponse toResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .userAccountId(customer.getUserAccount().getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dob(DateUtil.localDateTimeToString(customer.getDateOfBirth()))
                .gender(customer.getGender().getDescription())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }
}
