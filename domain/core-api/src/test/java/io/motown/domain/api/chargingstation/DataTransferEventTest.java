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

import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getChargingStationId;
import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getProtocol;
import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getVendorId;

public class DataTransferEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new DataTransferEvent(null, getVendorId(), getProtocol(), "", "");
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithVendorIdNull() {
        new DataTransferEvent(getChargingStationId(), null, getProtocol(), "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingEventWithVendorIdEmpty() {
        new DataTransferEvent(getChargingStationId(), "", getProtocol(), "", "");
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithProtocolNull() {
        new DataTransferEvent(getChargingStationId(), getVendorId(), null, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingEventWithProtocolEmpty() {
        new DataTransferEvent(getChargingStationId(), getVendorId(), "", "", "");
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithMessageIdNull() {
        new DataTransferEvent(getChargingStationId(), getVendorId(), getProtocol(), null, "");
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithDataNull() {
        new DataTransferEvent(getChargingStationId(), getVendorId(), getProtocol(), "", null);
    }

}
