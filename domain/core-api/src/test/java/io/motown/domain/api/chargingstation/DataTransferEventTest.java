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

import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class DataTransferEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new DataTransferRequestedEvent(null, DATA_TRANSFER_VENDOR, PROTOCOL, "", "", ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithVendorIdNull() {
        new DataTransferRequestedEvent(CHARGING_STATION_ID, null, PROTOCOL, "", "", ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingEventWithVendorIdEmpty() {
        new DataTransferRequestedEvent(CHARGING_STATION_ID, "", PROTOCOL, "", "", ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithProtocolNull() {
        new DataTransferRequestedEvent(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, null, "", "", ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingEventWithProtocolEmpty() {
        new DataTransferRequestedEvent(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, "", "", "", ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithMessageIdNull() {
        new DataTransferRequestedEvent(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, PROTOCOL, null, "", ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithDataNull() {
        new DataTransferRequestedEvent(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, PROTOCOL, "", null, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithIdentityContextNull() {
        new DataTransferRequestedEvent(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, PROTOCOL, "", "", null);
    }

}
