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

import java.util.Date;

import static io.motown.domain.api.chargingstation.CoreApiTestUtils.*;

public class TransactionStartedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullChargingStationId() {
        new TransactionStartedEvent(null, getNumberedTransactionId(), new ConnectorId(1), getTextualToken(), 1, new Date(), getEmptyAttributesMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTransactionId() {
        new TransactionStartedEvent(getChargingStationId(), null, new ConnectorId(1), getTextualToken(), 1, new Date(), getEmptyAttributesMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentifyingToken() {
        new TransactionStartedEvent(getChargingStationId(), getNumberedTransactionId(), new ConnectorId(1), null, 1, new Date(), getEmptyAttributesMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTimestamp() {
        new TransactionStartedEvent(getChargingStationId(), getNumberedTransactionId(), new ConnectorId(1), getTextualToken(), 1, null, getEmptyAttributesMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullAttributes() {
        new TransactionStartedEvent(getChargingStationId(), getNumberedTransactionId(), new ConnectorId(1), getTextualToken(), 1, new Date(), null);
    }
}
