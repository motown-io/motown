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

import java.util.Collections;
import java.util.Date;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static junit.framework.Assert.assertEquals;

public class ComponentStatusNotificationCommandTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithChargingStationIdNull() {
        new ComponentStatusNotificationCommand(null, ChargingStationComponent.CONNECTOR, EVSE_ID, ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithComponentNull() {
        new ComponentStatusNotificationCommand(CHARGING_STATION_ID, null, EVSE_ID, ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithComponentIdNull() {
        new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, null, ComponentStatus.AVAILABLE, new Date(), null, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithStatusNull() {
        new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, null, new Date(), Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithTimestampNull() {
        new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, ComponentStatus.AVAILABLE, null, Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithAttributesNull() {
        new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, ComponentStatus.AVAILABLE, new Date(), null, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithIdentityContextNull() {
        new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap(), null);
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();
        ComponentStatusNotificationCommand command = new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, ComponentStatus.AVAILABLE, now, BOOT_NOTIFICATION_ATTRIBUTES, NULL_USER_IDENTITY_CONTEXT);
        command.getTimestamp().setTime(TWO_MINUTES_AGO.getTime());
        assertEquals(now, command.getTimestamp());
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(ComponentStatusNotificationCommand.class).usingGetClass().verify();
    }
}
