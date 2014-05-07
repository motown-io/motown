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
package io.motown.ocpp.websocketjson.request.handler;

import io.motown.domain.api.chargingstation.UnitOfMeasure;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class UnitOfMeasureTranslatorTest {

    @Test(expected = AssertionError.class)
    public void testTranslate() throws Exception {
        new UnitOfMeasureTranslator("Not.A.Unit.Of.Measure").translate();
    }

    @RunWith(Parameterized.class)
    public static class TranslateTest {

        private final String input;
        private final UnitOfMeasure expected;

        public TranslateTest(String input, UnitOfMeasure expected) {
            this.input = input;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static List<Object[]> getParameters() {
            return Arrays.asList(new Object[][]{
                    {"Amp", UnitOfMeasure.AMPERES},
                    {"Celsius", UnitOfMeasure.CELSIUS},
                    {"kvar", UnitOfMeasure.KILOVAR},
                    {"kvarh", UnitOfMeasure.KILOVAR_HOUR},
                    {"kW", UnitOfMeasure.KILOWATT},
                    {"kWh", UnitOfMeasure.KILOWATT_HOUR},
                    {"var", UnitOfMeasure.VAR},
                    {"varh", UnitOfMeasure.VAR_HOUR},
                    {"Volt", UnitOfMeasure.VOLTAGE},
                    {"W", UnitOfMeasure.WATT},
                    {"Wh", UnitOfMeasure.WATT_HOUR},
                    {null, UnitOfMeasure.WATT_HOUR},
                    {"", UnitOfMeasure.WATT_HOUR},
            });
        }

        @Test
        public void testTranslate() {
            UnitOfMeasureTranslator translator = new UnitOfMeasureTranslator(input);

            UnitOfMeasure actual = translator.translate();

            assertEquals(actual, expected);
        }
    }
}
