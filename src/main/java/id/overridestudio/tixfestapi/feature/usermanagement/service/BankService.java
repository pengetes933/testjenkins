package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.BankRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.BankResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Bank;

import java.util.List;

public interface BankService {
    BankResponse create(BankRequest request);
    BankResponse getById(String id);
    BankResponse updateById(String id, BankRequest request);
    Bank getOne(String id);
    List<BankResponse> getAll();
    void deleteById(String id);
}
