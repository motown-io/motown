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

import io.motown.domain.api.chargingstation.Measurand;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Translator which translates a {@code String} to a {@code Measurand}.
 */
@RunWith(Enclosed.class)
public class MeasurandTranslatorTest {

    @Test(expected = AssertionError.class)
    public void testTranslate() throws Exception {
        new MeasurandTranslator("Not.A.Measurand").translate();
    }

    @RunWith(Parameterized.class)
    public static class TranslateTest {

        private final String input;
        private final Measurand expected;

        public TranslateTest(String input, Measurand expected) {
            this.input = input;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static List<Object[]> getParameters() {
            return Arrays.asList(new Object[][]{
                    {"Energy.Active.Export.Register", Measurand.EXPORTED_ACTIVE_ENERGY_REGISTER},
                    {"Energy.Active.Import.Register", Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER},
                    {"Energy.Reactive.Export.Register", Measurand.EXPORTED_REACTIVE_ENERGY_REGISTER},
                    {"Energy.Reactive.Import.Register", Measurand.IMPORTED_REACTIVE_ENERGY_REGISTER},
                    {"Energy.Active.Export.Interval", Measurand.EXPORTED_ACTIVE_ENERGY_INTERVAL},
                    {"Energy.Active.Import.Interval", Measurand.IMPORTED_ACTIVE_ENERGY_INTERVAL},
                    {"Energy.Reactive.Export.Interval", Measurand.EXPORTED_REACTIVE_ENERGY_INTERVAL},
                    {"Energy.Reactive.Import.Interval", Measurand.IMPORTED_REACTIVE_ENERGY_INTERVAL},
                    {"Power.Active.Export", Measurand.EXPORTED_ACTIVE_POWER},
                    {"Power.Active.Import", Measurand.IMPORTED_ACTIVE_POWER},
                    {"Power.Reactive.Export", Measurand.EXPORTED_REACTIVE_POWER},
                    {"Power.Reactive.Import", Measurand.IMPORTED_REACTIVE_POWER},
                    {"Current.Export", Measurand.EXPORTED_CURRENT},
                    {"Current.Import", Measurand.IMPORTED_CURRENT},
                    {"Voltage", Measurand.VOLTAGE},
                    {"Temperature", Measurand.TEMPERATURE},
                    {null, Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER},
                    {"", Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER}
            });
        }

        @Test
        public void testTranslate() {
            MeasurandTranslator translator = new MeasurandTranslator(input);

            Measurand actual = translator.translate();

            assertEquals(actual, expected);
        }
    }
}
