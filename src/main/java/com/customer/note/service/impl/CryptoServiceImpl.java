package com.customer.note.service.impl;

import com.customer.note.helper.SecretKeyHelper;
import com.customer.note.service.CryptoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {

    private static final String ALGO = "AES/GCM/NoPadding";

    private final SecretKeyHelper secretKeyHelper;

    @Override
    public String encrypt(String parameter, SecretKey key) {
        if (parameter == null) return null;

        String encrypted = encryptParameter(parameter, secretKeyHelper.vaultKey());
        log.info("Encrypting parameter {}, encrypted", parameter, encrypted);
        return encrypted;

    }

    @Override
    public String decrypt(String parameter, SecretKey key) {
        if (parameter == null) return null;
        String decrypted = decryptParameter(parameter, secretKeyHelper.vaultKey());
        log.info("Encrypting parameter {}, encrypted", parameter, decrypted);
        return decrypted;

    }


    private String encryptParameter(String data, SecretKey key) {
        try {
            byte[] iv = new byte[12];
            SecureRandom.getInstanceStrong().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String decryptParameter(String encrypted, SecretKey key) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encrypted);

            byte[] iv = Arrays.copyOfRange(decoded, 0, 12);
            byte[] cipherText = Arrays.copyOfRange(decoded, 12, decoded.length);

            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));

            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
