package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.core.service.StorageService;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizer;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.ResponsiblePerson;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.EventOrganizerImage;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.EventOrganizerImageRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.EventOrganizerImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EventOrganizerImageServiceImpl implements EventOrganizerImageService {
    private final EventOrganizerImageRepository responsiblePersonImageRepository;
    private final StorageService storageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EventOrganizerImage save(MultipartFile multipartFile, String path, EventOrganizer eventOrganizer) {
        String url = storageService.uploadFile(multipartFile, path);
        EventOrganizerImage eventOrganizerImage;

        if (eventOrganizer.getProfilePicture() == null) {
            eventOrganizerImage = EventOrganizerImage.builder()
                    .filename(multipartFile.getOriginalFilename())
                    .contentType(multipartFile.getContentType())
                    .size(multipartFile.getSize())
                    .url(url)
                    .eventOrganizer(eventOrganizer)
                    .build();
        } else {
            eventOrganizerImage = eventOrganizer.getProfilePicture();
            eventOrganizerImage.setUrl(url);
        }

        return responsiblePersonImageRepository.saveAndFlush(eventOrganizerImage);
    }
}
