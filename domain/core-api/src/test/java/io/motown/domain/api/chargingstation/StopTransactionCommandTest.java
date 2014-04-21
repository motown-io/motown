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
import static junit.framework.Assert.assertEquals;

public class StopTransactionCommandTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullChargingStationId() {
        new StopTransactionCommand(null, TRANSACTION_ID, IDENTIFYING_TOKEN, 1, new Date(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTransactionId() {
        new StopTransactionCommand(CHARGING_STATION_ID, null, IDENTIFYING_TOKEN, 1, new Date(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentifyingToken() {
        new StopTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, null, 1, new Date(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTimestamp() {
        new StopTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, 1, null, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentityContext() {
        new StopTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, 1, new Date(), null);
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();
        StopTransactionCommand command = new StopTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, TRANSACTION_NUMBER, now, NULL_USER_IDENTITY_CONTEXT);
        command.getTimestamp().setTime(TWO_MINUTES_AGO.getTime());
        assertEquals(now, command.getTimestamp());
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(StopTransactionCommand.class).usingGetClass().verify();
    }
}
