package com.customer.note.service;

import javax.crypto.SecretKey;

public interface CryptoService {
    String encrypt(String parameter, SecretKey key);

    String decrypt(String parameter, SecretKey key);
}
