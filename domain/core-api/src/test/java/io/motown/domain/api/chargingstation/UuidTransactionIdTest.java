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

import java.util.UUID;

import static junit.framework.Assert.assertEquals;

public class UuidTransactionIdTest {

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingWithEmptyTextualUuid() {
        new UuidTransactionId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingWithIncorrectTextualUuid() {
        new UuidTransactionId("this-is-not-a-uuid");
    }

    @Test
    public void testConstructors() {
        UUID uuid = UUID.randomUUID();
        UuidTransactionId id = new UuidTransactionId(uuid);
        String uuidString = "9287bafe-cba6-472a-bffe-9823751095ba";
        UuidTransactionId idFromString = new UuidTransactionId(uuidString);

        assertEquals(uuid.toString(), id.getId());
        assertEquals(uuidString, idFromString.getId());
    }
}
