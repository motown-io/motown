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

import java.util.Collections;
import java.util.Date;

import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getChargingStationId;

public class ComponentStatusNotificationCommandTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithChargingStationIdNull() {
        new ComponentStatusNotificationCommand(null, ChargingStationComponent.CONNECTOR, new ConnectorId(1), ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithComponentNull() {
        new ComponentStatusNotificationCommand(getChargingStationId(), null, new ConnectorId(1), ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithComponentIdNull() {
        new ComponentStatusNotificationCommand(getChargingStationId(), ChargingStationComponent.CONNECTOR, null, ComponentStatus.AVAILABLE, new Date(), null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithStatusNull() {
        new ComponentStatusNotificationCommand(getChargingStationId(), ChargingStationComponent.CONNECTOR, new ConnectorId(1), null, new Date(), Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithTimestampNull() {
        new ComponentStatusNotificationCommand(getChargingStationId(), ChargingStationComponent.CONNECTOR, new ConnectorId(1), ComponentStatus.AVAILABLE, null, Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithAttributesNull() {
        new ComponentStatusNotificationCommand(getChargingStationId(), ChargingStationComponent.CONNECTOR, new ConnectorId(1), ComponentStatus.AVAILABLE, new Date(), null);
    }
}
