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
import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getVendorId;

public class IncomingDataTransferCommandTest {

    private static final String MESSAGE_ID = "MessageId";
    private static final String DATA_TO_TRANSFER = "Data to transfer";

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenChargingStationIdNull() {
        new IncomingDataTransferCommand(null, getVendorId(), MESSAGE_ID, DATA_TO_TRANSFER);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenVendorIdNull() {
        new IncomingDataTransferCommand(getChargingStationId(), null, MESSAGE_ID, DATA_TO_TRANSFER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenVendorIdEmpty() {
        new IncomingDataTransferCommand(getChargingStationId(), "", MESSAGE_ID, DATA_TO_TRANSFER);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenMessageIdNull() {
        new IncomingDataTransferCommand(getChargingStationId(), getVendorId(), null, DATA_TO_TRANSFER);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenDataNull() {
        new IncomingDataTransferCommand(getChargingStationId(), getVendorId(), MESSAGE_ID, null);
    }
}
