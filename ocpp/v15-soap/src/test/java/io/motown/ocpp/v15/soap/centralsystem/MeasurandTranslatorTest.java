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
package io.motown.ocpp.v15.soap.centralsystem;

import io.motown.domain.api.chargingstation.Measurand;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class MeasurandTranslatorTest {

    @RunWith(Parameterized.class)
    public static class TranslateTest {

        private final io.motown.ocpp.v15.soap.centralsystem.schema.Measurand input;
        private final Measurand expected;

        public TranslateTest(io.motown.ocpp.v15.soap.centralsystem.schema.Measurand input, Measurand expected) {
            this.input = input;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static List<Object[]> getParameters() {
            return Arrays.asList(new Object[][]{
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.ENERGY_ACTIVE_EXPORT_REGISTER, Measurand.EXPORTED_ACTIVE_ENERGY_REGISTER},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.ENERGY_ACTIVE_IMPORT_REGISTER, Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.ENERGY_REACTIVE_EXPORT_REGISTER, Measurand.EXPORTED_REACTIVE_ENERGY_REGISTER},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.ENERGY_REACTIVE_IMPORT_REGISTER, Measurand.IMPORTED_REACTIVE_ENERGY_REGISTER},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.ENERGY_ACTIVE_EXPORT_INTERVAL, Measurand.EXPORTED_ACTIVE_ENERGY_INTERVAL},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.ENERGY_ACTIVE_IMPORT_INTERVAL, Measurand.IMPORTED_ACTIVE_ENERGY_INTERVAL},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.ENERGY_REACTIVE_EXPORT_INTERVAL, Measurand.EXPORTED_REACTIVE_ENERGY_INTERVAL},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.ENERGY_REACTIVE_IMPORT_INTERVAL, Measurand.IMPORTED_REACTIVE_ENERGY_INTERVAL},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.POWER_ACTIVE_EXPORT, Measurand.EXPORTED_ACTIVE_POWER},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.POWER_ACTIVE_IMPORT, Measurand.IMPORTED_ACTIVE_POWER},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.POWER_REACTIVE_EXPORT, Measurand.EXPORTED_REACTIVE_POWER},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.POWER_REACTIVE_IMPORT, Measurand.IMPORTED_REACTIVE_POWER},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.CURRENT_EXPORT, Measurand.EXPORTED_CURRENT},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.CURRENT_IMPORT, Measurand.IMPORTED_CURRENT},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.VOLTAGE, Measurand.VOLTAGE},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.Measurand.TEMPERATURE, Measurand.TEMPERATURE},
                    {null, Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER},
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
