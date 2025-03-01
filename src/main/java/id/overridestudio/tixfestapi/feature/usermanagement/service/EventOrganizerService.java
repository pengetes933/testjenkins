package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.EventOrganizerRegisterRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.EventOrganizerUpdateRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.SearchEventOrganizerRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.VerifyOtpRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.EventOrganizerResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface EventOrganizerService {
    void registration(EventOrganizerRegisterRequest request);
    EventOrganizerResponse verifyRegister(VerifyOtpRequest request);
    EventOrganizerResponse getById(String id);
    EventOrganizer getOne(String id);
    Page<EventOrganizerResponse> getAll(SearchEventOrganizerRequest request);
    EventOrganizerResponse updateById(String id, EventOrganizerUpdateRequest request, MultipartFile multipartFile);
    void requestVerify(String id);
    EventOrganizerResponse verifyEventOrganizer (String id);
    void deleteById(String id);
    boolean existByEventOrganizerIdAndUserId(String eventOrganizerId, String userId);
    void toggleFollow(String id);
}
