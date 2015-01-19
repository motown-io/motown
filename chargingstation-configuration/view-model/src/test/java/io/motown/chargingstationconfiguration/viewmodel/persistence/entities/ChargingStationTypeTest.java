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
package io.motown.chargingstationconfiguration.viewmodel.persistence.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChargingStationTypeTest {

    @Test
    public void testGettersAndSetters() {
        ChargingStationType type = new ChargingStationType();

        type.setId(1l);
        assertEquals(new Long(1), type.getId());

        Manufacturer manufacturer = new Manufacturer();
        type.setManufacturer(manufacturer);
        assertEquals(manufacturer, type.getManufacturer());

        type.setCode("CODE");
        assertEquals("CODE", type.getCode());
    }

}
