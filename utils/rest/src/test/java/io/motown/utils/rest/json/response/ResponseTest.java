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
package io.motown.utils.rest.json.response;

import com.google.common.collect.Lists;
import org.junit.Test;

public class ResponseTest {

    @Test
    public void testResponse() {
        NavigationItem item = new NavigationItem("item");
        new Response<>("href", item, item, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testResponseWithNullHref() {
        NavigationItem item = new NavigationItem("item");
        new Response<>(null, item, item, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testResponseWithNullPrevious() {
        NavigationItem item = new NavigationItem("item");
        new Response<>("href", null, item, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testResponseWithNullNext() {
        NavigationItem item = new NavigationItem("item");
        new Response<>("href", item, null, item, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testResponseWithNullFirst() {
        NavigationItem item = new NavigationItem("item");
        new Response<>("href", item, item, null, item, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testResponseWithNullLast() {
        NavigationItem item = new NavigationItem("item");
        new Response<>("href", item, item, item, null, Lists.newArrayList());
    }

    @Test(expected = NullPointerException.class)
    public void testResponseWithNullElements() {
        NavigationItem item = new NavigationItem("item");
        new Response<>("href", item, item, item, item, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseWithEmptyHref() {
        NavigationItem item = new NavigationItem("item");
        new Response<>("", item, item, item, item, Lists.newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseWithEmptyFirst() {
        NavigationItem item = new NavigationItem("item");
        new Response<>("href", item, item, new NavigationItem(""), item, Lists.newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseWithEmptyLast() {
        NavigationItem item = new NavigationItem("item");
        new Response<>("href", item, item, item, new NavigationItem(""), Lists.newArrayList());
    }
}
