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

import io.motown.domain.api.chargingstation.ReadingContext;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class ReadingContextTranslatorTest {

    @RunWith(Parameterized.class)
    public static class TranslateTest {

        private final io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext input;
        private final ReadingContext expected;

        public TranslateTest(io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext input, ReadingContext expected) {
            this.input = input;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static List<Object[]> getParameters() {
            return Arrays.asList(new Object[][]{
                    {io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext.INTERRUPTION_BEGIN, ReadingContext.BEGIN_INTERRUPTION},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext.TRANSACTION_BEGIN, ReadingContext.BEGIN_TRANSACTION},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext.SAMPLE_CLOCK, ReadingContext.CLOCK_SAMPLE},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext.INTERRUPTION_END, ReadingContext.END_INTERRUPTION},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext.TRANSACTION_END, ReadingContext.END_TRANSACTION},
                    {io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext.SAMPLE_PERIODIC, ReadingContext.PERIODIC_SAMPLE},
                    {null, ReadingContext.PERIODIC_SAMPLE}
            });
        }

        @Test
        public void testTranslate() {
            ReadingContextTranslator translator = new ReadingContextTranslator(input);

            ReadingContext actual = translator.translate();

            assertEquals(actual, expected);
        }
    }
}
