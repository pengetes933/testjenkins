package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizerImage;
import org.springframework.web.multipart.MultipartFile;

public interface EventOrganizerImageService {
    EventOrganizerImage save(MultipartFile multipartFile, String path, EventOrganizer eventOrganizer);
}
