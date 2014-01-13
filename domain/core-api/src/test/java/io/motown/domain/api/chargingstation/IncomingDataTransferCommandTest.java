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

    private final String messageId = "MessageId";
    private final String dataToTransfer = "Data to transfer";

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenChargingStationIdNull() {
        new IncomingDataTransferCommand(null, getVendorId(), messageId, dataToTransfer);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenVendorIdNull() {
        new IncomingDataTransferCommand(getChargingStationId(), null, messageId, dataToTransfer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenVendorIdEmpty() {
        new IncomingDataTransferCommand(getChargingStationId(), "", messageId, dataToTransfer);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenMessageIdNull() {
        new IncomingDataTransferCommand(getChargingStationId(), getVendorId(), null, dataToTransfer);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenDataNull() {
        new IncomingDataTransferCommand(getChargingStationId(), getVendorId(), messageId, null);
    }
}
