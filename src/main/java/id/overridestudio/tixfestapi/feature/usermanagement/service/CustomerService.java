package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.CustomerRegisterRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.CustomerUpdateRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.SearchCustomerRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.VerifyOtpRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.CustomerResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    void registration(CustomerRegisterRequest request);

    CustomerResponse verifyRegister(VerifyOtpRequest request);

    CustomerResponse getById(String id);

    Customer getOne(String id);

    Page<CustomerResponse> getAll(SearchCustomerRequest request);
    CustomerResponse updateById(String id, CustomerUpdateRequest request);

    void deleteById(String id);
    boolean existByCustomerIdAndUserId(String customerId, String userId);
}
