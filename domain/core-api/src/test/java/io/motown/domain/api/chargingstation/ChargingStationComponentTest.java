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
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class ChargingStationComponentTest {

    @RunWith(Parameterized.class)
    public static class ToStringTest {
        private ChargingStationComponent chargingStationComponent;
        private String value;

        public ToStringTest(ChargingStationComponent chargingStationComponent, String value) {
            this.chargingStationComponent = chargingStationComponent;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {ChargingStationComponent.CONNECTOR, "Connector"},
                    {ChargingStationComponent.EVSE, "EVSE"}
            });
        }

        @Test
        public void toStringShouldReturnTheGivenStringValue() {
            assertTrue(chargingStationComponent.toString().equals(value));
        }
    }

    @RunWith(Parameterized.class)
    public static class FromValueTest {
        private ChargingStationComponent chargingStationComponent;
        private String value;

        public FromValueTest(ChargingStationComponent chargingStationComponent, String value) {
            this.chargingStationComponent = chargingStationComponent;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {ChargingStationComponent.CONNECTOR, "Connector"},
                    {ChargingStationComponent.EVSE, "Evse"},
                    {ChargingStationComponent.CONNECTOR, "CONNECTOR"},
                    {ChargingStationComponent.EVSE, "EVSE"}
            });
        }

        @Test
        public void testFromValue() {
            assertEquals(chargingStationComponent, ChargingStationComponent.fromValue(value));
        }

        @Test(expected = IllegalArgumentException.class)
        public void testNonExistentValue() {
            ComponentStatus.fromValue("NonExistentValue");
        }

        @Test(expected = NullPointerException.class)
        public void testNullValue() {
            ComponentStatus.fromValue(null);
        }
    }
}