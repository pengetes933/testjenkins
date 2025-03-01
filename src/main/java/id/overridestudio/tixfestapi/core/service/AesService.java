package id.overridestudio.tixfestapi.core.service;

public interface AesService {
    String encrypt(String data);
    String decrypt(String data);
}
