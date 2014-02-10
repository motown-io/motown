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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ComponentStatusTest {

    private String value;
    private ComponentStatus componentStatus;

    public ComponentStatusTest(ComponentStatus componentStatus, String value) {
        this.componentStatus = componentStatus;
        this.value = value;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ComponentStatus.UNKNOWN, "Unknown"},
                {ComponentStatus.AVAILABLE, "Available"},
                {ComponentStatus.OCCUPIED, "Occupied"},
                {ComponentStatus.RESERVED, "Reserved"},
                {ComponentStatus.INOPERATIVE, "Inoperative"},
                {ComponentStatus.FAULTED, "Faulted"},
                {ComponentStatus.UNKNOWN, "UNKNOWN"},
                {ComponentStatus.AVAILABLE, "AVAILABLE"},
                {ComponentStatus.OCCUPIED, "OCCUPIED"},
                {ComponentStatus.RESERVED, "RESERVED"},
                {ComponentStatus.INOPERATIVE, "INOPERATIVE"},
                {ComponentStatus.FAULTED, "FAULTED"}
        });
    }

    @Test
    public void testFromValue() {
        assertEquals(componentStatus, ComponentStatus.fromValue(value));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromValueWithUnknownComponentStatus() {
        ComponentStatus.fromValue("NonExistentComponentStatus");
    }

    @Test(expected = NullPointerException.class)
    public void testFromValueWithComponentStatusNull() {
        ComponentStatus.fromValue(null);
    }

    @Test
    public void testToString() {
        assertTrue(componentStatus.toString().equalsIgnoreCase(value));
    }
}
