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
package io.motown.chargingstationconfiguration.viewmodel.restapi;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

public class ConfigurationApiResponseTest {

    @Test
    public void testConfigurationApiResponse() {
        new ConfigurationApiResponse<>(Maps.<String, Object>newHashMap(), Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testConfigurationApiResponseWithNullMetadata() {
        new ConfigurationApiResponse<>(null, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testConfigurationApiResponseWithNullRecords() {
        new ConfigurationApiResponse<>(Maps.<String, Object>newHashMap(), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConfigurationApiResponseMetadataIsImmutable() {
        ConfigurationApiResponse<String> response = new ConfigurationApiResponse<>(Maps.<String, Object>newHashMap(), Lists.<String>newArrayList());
        response.getMetadata().put("key", "value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConfigurationApiResponseRecordsIsImmutable() {
        ConfigurationApiResponse<String> response = new ConfigurationApiResponse<>(Maps.<String, Object>newHashMap(), Lists.<String>newArrayList());
        response.getRecords().add("value");
    }
}
