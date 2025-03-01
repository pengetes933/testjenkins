package id.overridestudio.tixfestapi.core.service;

public interface PermissionEvaluationService {
    boolean hasAccessToCustomer(String customerId, String userId);
    boolean hasAccessToEventOrganizer(String eventOrganizerId, String userId);
    boolean hasAccessToBankAccount(String bankAccountId, String eventOrganizerId, String userId);
    boolean hasAccessToResponsiblePerson(String responsiblePersonId, String eventOrganizerId, String userId);
    boolean hasAccessToEventOrganizerAddress(String eventOrganizerAddressId, String eventOrganizerId, String userId);
}
