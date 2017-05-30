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
package io.motown.ocpi.authorization;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.IDENTIFYING_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import io.motown.ocpi.persistence.entities.Token;
import io.motown.ocpi.persistence.entities.Token.TokenType;
import io.motown.ocpi.persistence.repository.OcpiRepository;

public class TokenValidatorTest {

    private TokenValidator tokenValidator;

    private OcpiRepository ocpiRepository;

    @Before
    public void setup() {
    	tokenValidator = new TokenValidator();

    	ocpiRepository = mock(OcpiRepository.class);
        tokenValidator.setOcpiRepository(ocpiRepository);
    }

    
    private Token getToken(String uid, Boolean isValid){
    	Token token = new Token();
    	token.setAuthId("111");
    	token.setDateCreated(new Date(System.currentTimeMillis()));
    	token.setIssuingCompany("e-clearing");
    	token.setTokenType(TokenType.RFID);
    	token.setUid(uid);
    	token.setValid(isValid);
    	token.setVisualNumber("7007");
    	return token;
    }
    
    @Test
    public void testIsValidByUid() {
        when(ocpiRepository.findTokenByUid(IDENTIFYING_TOKEN.getToken())).thenReturn(getToken(IDENTIFYING_TOKEN.getToken(), Boolean.TRUE));

        assertTrue(tokenValidator.validate(IDENTIFYING_TOKEN).isValid());
        assertEquals(tokenValidator.validate(IDENTIFYING_TOKEN).getToken(), IDENTIFYING_TOKEN.getToken());
    }

    @Test
    public void testIsInvalidByUid() {
        when(ocpiRepository.findTokenByUid(IDENTIFYING_TOKEN.getToken())).thenReturn(getToken(IDENTIFYING_TOKEN.getToken(), Boolean.FALSE));

        assertFalse(tokenValidator.validate(IDENTIFYING_TOKEN).isValid());
        assertEquals(tokenValidator.validate(IDENTIFYING_TOKEN).getToken(), IDENTIFYING_TOKEN.getToken());
    }
    
}
