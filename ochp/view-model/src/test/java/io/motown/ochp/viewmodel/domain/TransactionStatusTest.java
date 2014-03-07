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
package io.motown.ochp.viewmodel.domain;

import io.motown.ochp.viewmodel.persistence.TransactionStatus;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class TransactionStatusTest {

    @RunWith(Parameterized.class)
    public static class FromValueTest {
        private final TransactionStatus transactionStatus;
        private final String value;

        public FromValueTest(TransactionStatus transactionStatus, String value) {
            this.transactionStatus = transactionStatus;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {TransactionStatus.STARTED, "STARTED"},
                    {TransactionStatus.STOPPED, "STOPPED"},
                    {TransactionStatus.STARTED, "Started"},
                    {TransactionStatus.STOPPED, "Stopped"}
            });
        }

        @Test
        public void testFromValue() {
            assertEquals(transactionStatus, TransactionStatus.fromValue(value));
        }

        @Test(expected = IllegalArgumentException.class)
        public void testIllegalValue() {
            TransactionStatus.fromValue("Running");
        }

        @Test(expected = NullPointerException.class)
        public void testNullValue() {
            TransactionStatus.fromValue(null);
        }
    }

    @RunWith(Parameterized.class)
    public static class ToStringTest {
        private final TransactionStatus transactionStatus;
        private final String value;

        public ToStringTest(TransactionStatus transactionStatus, String value) {
            this.transactionStatus = transactionStatus;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {TransactionStatus.STARTED, "Started"},
                    {TransactionStatus.STOPPED, "Stopped"}
            });
        }

        @Test
        public void testToString() {
            assertTrue(transactionStatus.toString().equals(value));
        }
    }

}
