package id.overridestudio.tixfestapi.core.service.impl;

import id.overridestudio.tixfestapi.core.service.PermissionEvaluationService;
import id.overridestudio.tixfestapi.feature.usermanagement.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionEvaluationServiceImpl implements PermissionEvaluationService {
    private final CustomerService customerService;
    private final EventOrganizerService eventOrganizerService;
    private final BankAccountService bankAccountService;
    private final ResponsiblePersonService responsiblePersonService;
    private final EventOrganizerAddressService eventOrganizerAddressService;
    @Override
    public boolean hasAccessToCustomer(String customerId, String userId) {
        return customerService.existByCustomerIdAndUserId(customerId, userId);
    }

    @Override
    public boolean hasAccessToEventOrganizer(String eventOrganizerId, String userId) {
        return eventOrganizerService.existByEventOrganizerIdAndUserId(eventOrganizerId, userId);
    }

    @Override
    public boolean hasAccessToBankAccount(String bankAccountId, String eventOrganizerId, String userId) {
        return bankAccountService.existByBankAccountIdAndEventOrganizerIdAndUserId(bankAccountId, eventOrganizerId, userId);
    }

    @Override
    public boolean hasAccessToResponsiblePerson(String responsiblePersonId, String eventOrganizerId, String userId) {
        return responsiblePersonService.existByResponsiblePersonIdAndEventOrganizerIdAndUserId(responsiblePersonId, eventOrganizerId, userId);
    }

    @Override
    public boolean hasAccessToEventOrganizerAddress(String eventOrganizerAddressId, String eventOrganizerId, String userId) {
        return eventOrganizerAddressService.existByEventOrganizerAddressIdAndEventOrganizerIdAndUserId(eventOrganizerAddressId, eventOrganizerId, userId);
    }
}
