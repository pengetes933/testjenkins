package id.overridestudio.tixfestapi.core.service.impl;

import id.overridestudio.tixfestapi.core.service.AesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class AesServiceImpl implements AesService {

    @Value("${tixfest.aes-secret}")
    private String SECRET_KEY;

    @Override
    public String encrypt(String data) {
        try {
            return process(data, Cipher.ENCRYPT_MODE);
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        try {
            return process(encryptedData, Cipher.DECRYPT_MODE);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }

    private String process(String data, int cipherMode) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherMode, secretKey);

        byte[] resultData;

        if (cipherMode == Cipher.ENCRYPT_MODE) {
            resultData = cipher.doFinal(data.getBytes());
        } else {
            byte[] decodedData = Base64.getDecoder().decode(data);
            resultData = cipher.doFinal(decodedData);
        }
        return Base64.getEncoder().encodeToString(resultData);
    }

}

