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

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class CoreApiTestUtils {

    public static final int TRANSACTION_NUMBER = 123;

    private CoreApiTestUtils() {
        // Private no-arg constructor to prevent instantiation of utility class.
    }

    public static ChargingStationId getChargingStationId() {
        return new ChargingStationId("CS-001");
    }

    public static String getProtocol() {
        return "protocol";
    }

    public static NumberedTransactionId getNumberedTransactionId() {
        return new NumberedTransactionId(getChargingStationId(), getProtocol(), TRANSACTION_NUMBER);
    }

    public static TextualToken getTextualToken() {
        return new TextualToken("12345AB");
    }

    public static String getVendorId() {
        return "Alfen";
    }

    public static Map<String, String> getEmptyAttributesMap() {
        return ImmutableMap.<String, String>of();
    }
}
