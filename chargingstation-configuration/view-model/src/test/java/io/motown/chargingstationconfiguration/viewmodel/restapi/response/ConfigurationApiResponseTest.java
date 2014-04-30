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
package io.motown.chargingstationconfiguration.viewmodel.restapi.response;

import com.google.common.collect.Lists;
import org.junit.Test;

public class ConfigurationApiResponseTest {

    @Test
    public void testConfigurationApiResponse() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>("href", item, item, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testConfigurationApiResponseWithNullHref() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>(null, item, item, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testConfigurationApiResponseWithNullPrevious() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>("href", null, item, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testConfigurationApiResponseWithNullNext() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>("href", item, null, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testConfigurationApiResponseWithNullFirst() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>("href", item, item, null, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testConfigurationApiResponseWithNullLast() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>("href", item, item, item, null, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testConfigurationApiResponseWithNullElements() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>("href", item, item, item, item, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationApiResponseWithEmptyHref() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>("", item, item, item, item, Lists.newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationApiResponseWithEmptyFirst() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>("href", item, item, new NavigationItem(""), item, Lists.newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationApiResponseWithEmptyLast() {
        NavigationItem item = new NavigationItem("item");
        new ConfigurationApiResponse<>("href", item, item, item, new NavigationItem(""), Lists.newArrayList());
    }
}
