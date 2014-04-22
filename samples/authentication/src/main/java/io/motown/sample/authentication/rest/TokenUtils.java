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
package io.motown.sample.authentication.rest;

import com.google.common.base.Charsets;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenUtils {

    public static final String MAGIC_KEY = "obfuscate";

    /**
     * Algorithm to create digest of token signature.
     */
    private static final String MESSAGE_DIGEST_ALGORITHM = "MD5";

    /**
     * Token values separator.
     */
    private static final String TOKEN_SEPARATOR = ":";

    /**
     * Valid for one hour.
     */
    private static final long TOKEN_VALIDITY_DURATION = 1000L * 60 * 60;

    private TokenUtils() {
        // Hidden constructor to prevent instantiation of a utility class.
    }

    /**
     * Creates a token based on {@code UserDetails}, the current time and TOKEN_VALIDITY_DURATION.
     *
     * @param userDetails user details.
     * @return token.
     */
    public static String createToken(UserDetails userDetails) {
        long expires = System.currentTimeMillis() + TOKEN_VALIDITY_DURATION;

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(TOKEN_SEPARATOR);
        tokenBuilder.append(expires);
        tokenBuilder.append(TOKEN_SEPARATOR);
        tokenBuilder.append(TokenUtils.computeSignature(userDetails, expires));

        return tokenBuilder.toString();
    }

    /**
     * Computes a signature for {@code UserDetails} and expiration time.
     *
     * @param userDetails user details.
     * @param expires     expiration time.
     * @return signature.
     */
    public static String computeSignature(UserDetails userDetails, long expires) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(TOKEN_SEPARATOR);
        signatureBuilder.append(expires);
        signatureBuilder.append(TOKEN_SEPARATOR);
        signatureBuilder.append(userDetails.getPassword());
        signatureBuilder.append(TOKEN_SEPARATOR);
        signatureBuilder.append(TokenUtils.MAGIC_KEY);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No " + MESSAGE_DIGEST_ALGORITHM + " algorithm available!", e);
        }

        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes(Charsets.UTF_8))));
    }

    /**
     * Extracts the user name from token.
     *
     * @param authToken token containing username.
     * @return username or null if token is null.
     */
    public static String getUserNameFromToken(String authToken) {
        if (null == authToken) {
            return null;
        }

        return authToken.split(TOKEN_SEPARATOR)[0];
    }

    /**
     * Validates the token signature against the user details and the current system time.
     *
     * @param authToken   authorization token.
     * @param userDetails user details.
     * @return true if token is not expired and is equal to computed signature on base of user details.
     */
    public static boolean validateToken(String authToken, UserDetails userDetails) {
        String[] parts = authToken.split(TOKEN_SEPARATOR);
        long expires = Long.parseLong(parts[1]);
        String signature = parts[2];

        return expires >= System.currentTimeMillis() && signature.equals(TokenUtils.computeSignature(userDetails, expires));
    }
}
