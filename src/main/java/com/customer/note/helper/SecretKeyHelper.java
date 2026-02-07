package com.customer.note.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

@Component
@Slf4j
public class SecretKeyHelper {


    @Value("${vault.master-password}")
    String masterPassword;

    @Value("${vault.salt}")
    String salt;

    public SecretKey vaultKey() {
        try {
            var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            var spec = new PBEKeySpec(masterPassword.toCharArray(), salt.getBytes(), 65536, 256);
            var tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
