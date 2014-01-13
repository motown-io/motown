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

import java.util.ArrayList;

import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getChargingStationId;
import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getProtocol;


public class SendAuthorisationListEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new SendAuthorisationListRequestedEvent(null, getProtocol(), new ArrayList<IdentifyingToken>(), 1, null, AuthorisationListUpdateType.FULL);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithProtocolNull() {
        new SendAuthorisationListRequestedEvent(getChargingStationId(), null, new ArrayList<IdentifyingToken>(), 1, null, AuthorisationListUpdateType.FULL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingEventWithProtocolEmpty() {
        new SendAuthorisationListRequestedEvent(getChargingStationId(), "", new ArrayList<IdentifyingToken>(), 1, null, AuthorisationListUpdateType.FULL);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithTokensNull() {
        new SendAuthorisationListRequestedEvent(getChargingStationId(), getProtocol(), null, 1, "", AuthorisationListUpdateType.FULL);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithHashNull() {
        new SendAuthorisationListRequestedEvent(getChargingStationId(), getProtocol(), new ArrayList<IdentifyingToken>(), 1, null, AuthorisationListUpdateType.FULL);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithUpdateTypeNull() {
        new SendAuthorisationListRequestedEvent(getChargingStationId(), getProtocol(), new ArrayList<IdentifyingToken>(), 1, null, null);
    }
}
