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
import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getNumberedTransactionId;
import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getTextualToken;

public class StartTransactionRequestedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullChargingStationId() {
        new StartTransactionRequestedEvent(null, "ocpps15", getTextualToken(), 1);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullProtocol() {
        new StartTransactionRequestedEvent(getChargingStationId(), null, getTextualToken(), 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingWithEmptyProtocol() {
        new StartTransactionRequestedEvent(getChargingStationId(), "", getTextualToken(), 1);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentifyingToken() {
        new StartTransactionRequestedEvent(getChargingStationId(), "ocpps15", null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingWithNegativeConnectorId() {
        new StartTransactionRequestedEvent(getChargingStationId(), "ocpps15", getTextualToken(), Integer.MIN_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingWithZeroConnectorId() {
        new StartTransactionRequestedEvent(getChargingStationId(), "ocpps15", getTextualToken(), 0);
    }
}
