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

    @Test(expected = AssertionError.class)
    public void testTranslate() throws Exception {
        new ReadingContextTranslator("Not.A.Reading.Context").translate();
    }

    @RunWith(Parameterized.class)
    public static class TranslateTest {

        private final String input;
        private final ReadingContext expected;

        public TranslateTest(String input, ReadingContext expected) {
            this.input = input;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static List<Object[]> getParameters() {
            return Arrays.asList(new Object[][]{
                    {"Interruption.Begin", ReadingContext.BEGIN_INTERRUPTION},
                    {"Transaction.Begin", ReadingContext.BEGIN_TRANSACTION},
                    {"Sample.Clock", ReadingContext.CLOCK_SAMPLE},
                    {"Interruption.End", ReadingContext.END_INTERRUPTION},
                    {"Transaction.End", ReadingContext.END_TRANSACTION},
                    {"Sample.Periodic", ReadingContext.PERIODIC_SAMPLE},
                    {null, ReadingContext.PERIODIC_SAMPLE},
                    {"", ReadingContext.PERIODIC_SAMPLE}
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
