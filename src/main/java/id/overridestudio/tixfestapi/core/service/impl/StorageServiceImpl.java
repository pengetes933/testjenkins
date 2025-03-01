package id.overridestudio.tixfestapi.core.service.impl;

import id.overridestudio.tixfestapi.core.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    @Value("${storage.url}")
    private String storageUrl;

    @Value("${storage.token}")
    private String storageKey;

    @Value("${storage.file-max-size}")
    private Integer maxSize;

    private final List<String> contentTypes = List.of("image/jpg", "image/png", "image/webp", "image/jpeg");

    private final RestTemplate restTemplate;

    @Override
    public String uploadFile(MultipartFile file, String path) {

        try {
            validate(file);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());
            body.add("path", path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("X_API_KEY", storageKey);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(storageUrl + "/files", HttpMethod.POST, requestEntity, Map.class);

            Map<?, ?> data = (Map<?, ?>) response.getBody().get("data");
            if (data != null && data.get("url") != null) {
                return (String) data.get("url");
            } else {
                throw new IllegalArgumentException("URL not found in response data.");
            }
        } catch (HttpClientErrorException e) {
            throw new IllegalStateException("Error occurred while saving the file.");
        }
    }

    private void validate(MultipartFile multipartFile) {
        if (multipartFile.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "image cannot be empty");

        if (multipartFile.getOriginalFilename() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "filename cannot be empty");

        if (multipartFile.getSize() > maxSize) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file size exceed limit");
        }

        if (!contentTypes.contains(multipartFile.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid extensions type");
        }
    }

}
