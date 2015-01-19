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

import java.util.Date;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.assertEquals;

public class TransactionStoppedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullChargingStationId() {
        new TransactionStoppedEvent(null, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, new Date(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTransactionId() {
        new TransactionStoppedEvent(CHARGING_STATION_ID, null, IDENTIFYING_TOKEN, METER_STOP, new Date(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentifyingToken() {
        new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, null, METER_STOP, new Date(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTimestamp() {
        new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, null, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentityContext() {
        new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, new Date(), null);
    }

    @Test
    public void constructorSetsFields() {
        TransactionStoppedEvent event = new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, FIVE_MINUTES_AGO, NULL_USER_IDENTITY_CONTEXT);

        assertEquals(CHARGING_STATION_ID, event.getChargingStationId());
        assertEquals(TRANSACTION_ID, event.getTransactionId());
        assertEquals(IDENTIFYING_TOKEN, event.getIdTag());
        assertEquals(METER_STOP, event.getMeterStop());
        assertEquals(FIVE_MINUTES_AGO, event.getTimestamp());
        assertEquals(NULL_USER_IDENTITY_CONTEXT, event.getIdentityContext());
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();
        TransactionStoppedEvent event = new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, now, NULL_USER_IDENTITY_CONTEXT);
        event.getTimestamp().setTime(TWO_MINUTES_AGO.getTime());
        assertEquals(now, event.getTimestamp());
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(TransactionStoppedEvent.class).usingGetClass().verify();
    }
}
