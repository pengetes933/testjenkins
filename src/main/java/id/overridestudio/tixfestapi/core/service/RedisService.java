package id.overridestudio.tixfestapi.core.service;

import java.time.Duration;

public interface RedisService {
    void save(String key, String value, Duration duration);
    String get(String key);
    void delete(String key);
    Boolean isExists(String key);
}
