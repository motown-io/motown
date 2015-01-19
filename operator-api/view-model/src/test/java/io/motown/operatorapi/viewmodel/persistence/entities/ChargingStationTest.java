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
package io.motown.operatorapi.viewmodel.persistence.entities;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChargingStationTest {

    @Test
    public void isAcceptedOnNewChargingStation() {
        ChargingStation cs = new ChargingStation("");

        assertFalse(cs.isAccepted());
    }

    @Test
    public void communicationAllowed() {
        ChargingStation cs = new ChargingStation("");
        cs.setAccepted(true);
        cs.setConfigured(true);

        assertTrue(cs.communicationAllowed());
    }

    @Test
    public void communicationNotAllowedUnconfiguredChargingStation() {
        ChargingStation cs = new ChargingStation("");
        cs.setAccepted(true);

        assertFalse(cs.communicationAllowed());
    }

    @Test
    public void communicationNotAllowedNotAcceptedChargingStation() {
        ChargingStation cs = new ChargingStation("");
        cs.setConfigured(true);

        assertFalse(cs.communicationAllowed());
    }

}
