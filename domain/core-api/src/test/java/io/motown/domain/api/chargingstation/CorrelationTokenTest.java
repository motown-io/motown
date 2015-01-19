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
package io.motown.domain.api.chargingstation;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.UUID;

public class CorrelationTokenTest {

    @Test
    public void creatingCorrelationTokenShouldGenerateUuidBasedToken() {
        CorrelationToken correlationToken = new CorrelationToken();

        // We want to test whether the constructor actually creates UUID for the token field. We're expecting this call
        // to NOT throw an exception. If it does throw an exception the test will fail.
        UUID.fromString(correlationToken.getToken());
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(CorrelationToken.class).usingGetClass().verify();
    }
}
