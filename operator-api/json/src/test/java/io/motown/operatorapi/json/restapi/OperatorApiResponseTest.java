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
package io.motown.operatorapi.json.restapi;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

public class OperatorApiResponseTest {

    @Test
    public void testOperatorApiResponse() {
        new OperatorApiResponse<>(Maps.<String, Object>newHashMap(), Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testOperatorApiResponseWithNullMetadata() {
        new OperatorApiResponse<>(null, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testOperatorApiResponseWithNullRecords() {
        new OperatorApiResponse<>(Maps.<String, Object>newHashMap(), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOperatorApiResponseMetadataIsImmutable() {
        OperatorApiResponse<String> response = new OperatorApiResponse<>(Maps.<String, Object>newHashMap(), Lists.<String>newArrayList());
        response.getMetadata().put("key", "value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOperatorApiResponseRecordsIsImmutable() {
        OperatorApiResponse<String> response = new OperatorApiResponse<>(Maps.<String, Object>newHashMap(), Lists.<String>newArrayList());
        response.getRecords().add("value");
    }
}
