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
package io.motown.operatorapi.json.restapi.response;

import com.google.common.collect.Lists;
import org.junit.Test;

public class OperatorApiResponseTest {

    @Test
    public void testOperatorApiResponse() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>("href", item, item, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testOperatorApiResponseWithNullHref() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>(null, item, item, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testOperatorApiResponseWithNullPrevious() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>("href", null, item, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testOperatorApiResponseWithNullNext() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>("href", item, null, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testOperatorApiResponseWithNullFirst() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>("href", item, item, null, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testOperatorApiResponseWithNullLast() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>("href", item, item, item, null, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testOperatorApiResponseWithNullElements() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>("href", item, item, item, item, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperatorApiResponseWithEmptyHref() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>("", item, item, item, item, Lists.newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperatorApiResponseWithEmptyFirst() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>("href", item, item, new NavigationItem(""), item, Lists.newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperatorApiResponseWithEmptyLast() {
        NavigationItem item = new NavigationItem("item");
        new OperatorApiResponse<>("href", item, item, item, new NavigationItem(""), Lists.newArrayList());
    }
}
