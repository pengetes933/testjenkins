package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.BankAccountRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.BankAccountResponse;

public interface BankAccountService {
    BankAccountResponse create (String eventOrganizerId, BankAccountRequest request);
    BankAccountResponse getById (String id);
    BankAccountResponse updateById(String id, BankAccountRequest request);
    boolean existByBankAccountIdAndEventOrganizerIdAndUserId(String bankAccountId, String eventOrganizerId, String userId);
}
