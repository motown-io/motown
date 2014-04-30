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
package io.motown.operatorapi.json.restapi.util;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperatorApiResponseBuilderTest {

    private HttpServletRequest request;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getQueryString()).thenReturn("offset=0&limit=10");
    }

    @Test
    public void testOperatorApiResponseBuilder() {
        OperatorApiResponseBuilder.buildResponse(request, 0, 1, 0, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperatorApiResponseBuilderInvalidOffset() {
        OperatorApiResponseBuilder.buildResponse(request, -1, 1, 0, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperatorApiResponseBuilderInvalidLimit() {
        OperatorApiResponseBuilder.buildResponse(request, 0, 0, 1, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperatorApiResponseBuilderInvalidTotal() {
        OperatorApiResponseBuilder.buildResponse(request, 0, 1, -1, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOperatorApiResponseBuilderOffsetEqualToTotalWhenTotalGreaterThanZero() {
        OperatorApiResponseBuilder.buildResponse(request, 1, 1, 1, Lists.<String>newArrayList());
    }
}
