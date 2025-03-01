package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.ResponsiblePersonRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.ResponsiblePersonResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResponsiblePersonService {
    ResponsiblePersonResponse create (String eventOrganizerId, ResponsiblePersonRequest request);
    ResponsiblePersonResponse getById(String id);
    ResponsiblePersonResponse updateById(String id, ResponsiblePersonRequest request);
    void requestVerify(String id);
    ResponsiblePersonResponse verifyResponsiblePerson (String id);
    List<ResponsiblePersonResponse> getAllByEventOrganizer (String eventOrganizerId);
    void deleteById(String id);
    boolean existByResponsiblePersonIdAndEventOrganizerIdAndUserId(String responsiblePersonId, String eventOrganizerId, String userId);
}
