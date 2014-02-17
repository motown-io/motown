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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.PROTOCOL;

public class SendAuthorizationListEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new SendAuthorizationListRequestedEvent(null, PROTOCOL, Collections.<IdentifyingToken>emptyList(), 1, null, AuthorizationListUpdateType.FULL);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithProtocolNull() {
        new SendAuthorizationListRequestedEvent(CHARGING_STATION_ID, null, Collections.<IdentifyingToken>emptyList(), 1, null, AuthorizationListUpdateType.FULL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingEventWithProtocolEmpty() {
        new SendAuthorizationListRequestedEvent(CHARGING_STATION_ID, "", Collections.<IdentifyingToken>emptyList(), 1, null, AuthorizationListUpdateType.FULL);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithTokensNull() {
        new SendAuthorizationListRequestedEvent(CHARGING_STATION_ID, PROTOCOL, null, 1, "", AuthorizationListUpdateType.FULL);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithHashNull() {
        new SendAuthorizationListRequestedEvent(CHARGING_STATION_ID, PROTOCOL, Collections.<IdentifyingToken>emptyList(), 1, null, AuthorizationListUpdateType.FULL);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithUpdateTypeNull() {
        new SendAuthorizationListRequestedEvent(CHARGING_STATION_ID, PROTOCOL, Collections.<IdentifyingToken>emptyList(), 1, null, null);
    }
}
