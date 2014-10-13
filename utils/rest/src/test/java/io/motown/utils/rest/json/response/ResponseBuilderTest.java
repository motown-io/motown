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
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseBuilderTest {
    private HttpServletRequest request;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getQueryString()).thenReturn("offset=0&limit=10");
    }

    @Test
    public void testResponseBuilder() {
        ResponseBuilder.buildResponse(request, 0, 1, 0, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseBuilderInvalidOffset() {
        ResponseBuilder.buildResponse(request, -1, 1, 0, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseBuilderInvalidLimit() {
        ResponseBuilder.buildResponse(request, 0, 0, 1, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseBuilderInvalidTotal() {
        ResponseBuilder.buildResponse(request, 0, 1, -1, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResponseBuilderOffsetEqualToTotalWhenTotalGreaterThanZero() {
        ResponseBuilder.buildResponse(request, 1, 1, 1, Lists.<String>newArrayList());
    }
}
