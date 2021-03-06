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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.assertEquals;

public class NumberedTransactionIdTest {

    public static final int NEGATIVE_TRANSACTION_NUMBER = -123;

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithChargingStationIdNull() {
        new NumberedTransactionId(null, PROTOCOL, TRANSACTION_NUMBER);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithProtocolNull() {
        new NumberedTransactionId(CHARGING_STATION_ID, null, TRANSACTION_NUMBER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingWithEmptyProtocol() {
        new NumberedTransactionId(CHARGING_STATION_ID, "", TRANSACTION_NUMBER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingWithNegativeNumber() {
        new NumberedTransactionId(CHARGING_STATION_ID, PROTOCOL, NEGATIVE_TRANSACTION_NUMBER);
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(NumberedTransactionId.class).usingGetClass().verify();
    }

    @Test
    public void transactionNumberShouldBeRetrievedInConstruction() {
        Integer transactionNumber = 1;
        NumberedTransactionId transactionId = new NumberedTransactionId(CHARGING_STATION_ID, PROTOCOL,
                TransactionIdFactory.createTransactionId(transactionNumber.toString(), CHARGING_STATION_ID, PROTOCOL).getId());

        assertEquals(transactionNumber.intValue(), transactionId.getNumber());
    }
}
