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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(Enclosed.class)
public class AccessibilityTest {

    @RunWith(Parameterized.class)
    public static class FromValueTest {
        private Accessibility accessibility;
        private String value;

        public FromValueTest(Accessibility accessibility, String value) {
            this.accessibility = accessibility;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {Accessibility.PUBLIC, "Public"},
                    {Accessibility.PAYING, "Paying"},
                    {Accessibility.PRIVATE, "Private"},
                    {Accessibility.PUBLIC, "PUBLIC"},
                    {Accessibility.PAYING, "PAYING"},
                    {Accessibility.PRIVATE, "PRIVATE"}
            });
        }

        @Test
        public void testFromValue() {
            assertEquals(accessibility, Accessibility.fromValue(value));
        }

        @Test(expected = IllegalArgumentException.class)
        public void testInvalidValue() {
            Accessibility.fromValue("Non-public");
        }

        @Test(expected = NullPointerException.class)
        public void testNullValue() {
            Accessibility.fromValue(null);
        }
    }

    @RunWith(Parameterized.class)
    public static class ToStringTest {
        private Accessibility accessibility;
        private String value;

        public ToStringTest(Accessibility accessibility, String value) {
            this.accessibility = accessibility;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {Accessibility.PUBLIC, "Public"},
                    {Accessibility.PAYING, "Paying"},
                    {Accessibility.PRIVATE, "Private"}
            });
        }

        @Test
        public void testToString() {
            assertTrue(accessibility.toString().equals(value));
        }
    }
}
