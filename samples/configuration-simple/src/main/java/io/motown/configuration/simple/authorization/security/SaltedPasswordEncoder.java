/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.configuration.simple.authorization.security;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Simple password encoder that uses a salt value and encodes the password.
 */
public class SaltedPasswordEncoder implements PasswordEncoder {

    private static final String ENCODING = "UTF-8";
    private final String salt;
    private final MessageDigest digest;

    public SaltedPasswordEncoder(String algorithm, String salt) throws NoSuchAlgorithmException {
        this.salt = salt;
        this.digest = MessageDigest.getInstance(algorithm);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        String saltedPassword = rawPassword + this.salt;
        try {
            return new String(Hex.encode(this.digest.digest(saltedPassword.getBytes(ENCODING))));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(ENCODING + " not supported", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return this.encode(rawPassword).equals(encodedPassword);
    }

}
