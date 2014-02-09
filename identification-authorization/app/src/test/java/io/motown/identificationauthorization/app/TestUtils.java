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
package io.motown.identificationauthorization.app;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.domain.api.chargingstation.TextualToken;

public class TestUtils {

    private TestUtils() {
        // Private no-arg constructor to prevent instantiation of utility class.
    }

    public static ChargingStationId getChargingStationId() {
        return new ChargingStationId("CS-001");
    }

    public static IdentifyingToken getInvalidIdentifyingToken() {
        return new TextualToken("INVALID");
    }

    public static IdentifyingToken getValidIdentifyingToken() {
        return new TextualToken("VALID");
    }

    public static IdentifyingToken getValidIdentifyingTokenSecondAuthorizationProvider() {
        return new TextualToken("VALID_SECOND_PROVIDER");
    }

    public static String getCorrelationId() {
        return "CORRELATION_ID";
    }

}
