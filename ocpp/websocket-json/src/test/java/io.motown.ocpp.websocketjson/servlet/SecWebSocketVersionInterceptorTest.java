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
package io.motown.ocpp.websocketjson.servlet;

import org.atmosphere.cpr.HeaderConfig;
import org.atmosphere.util.FilterConfigImpl;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SecWebSocketVersionInterceptorTest {

    @Test
    public void initInterceptorOcpp15() throws ServletException {
        new SecWebSocketVersionInterceptor().init(getFilterConfig("ocpp1.5"));
    }

    @Test
    public void initInterceptorOcpp12() throws ServletException {
        new SecWebSocketVersionInterceptor().init(getFilterConfig("ocpp1.2"));
    }

    @Test
    public void initInterceptorMultipleSubProtocols() throws ServletException {
        new SecWebSocketVersionInterceptor().init(getFilterConfig("ocpp1.5,ocpp1.2"));
    }

    @Test
    public void destroyInterceptor() {
        new SecWebSocketVersionInterceptor().destroy();
    }

    @Test
    public void doFilterOcpp15() throws ServletException, IOException {
        String protocol = "ocpp1.5";
        SecWebSocketVersionInterceptor interceptor = new SecWebSocketVersionInterceptor();
        interceptor.init(getFilterConfig(protocol));
        HttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HeaderConfig.X_ATMO_WEBSOCKET_PROXY, true);
        request.addHeader(SecWebSocketVersionInterceptor.SEC_WEB_SOCKET_PROTOCOL_HEADER, protocol);

        interceptor.doFilter(request, response, new MockFilterChain());

        assertEquals(response.getHeader(SecWebSocketVersionInterceptor.SEC_WEB_SOCKET_PROTOCOL_HEADER), protocol);
    }

    @Test
    public void doFilterNoProtocol() throws ServletException, IOException {
        String protocol = "ocpp1.5";
        SecWebSocketVersionInterceptor interceptor = new SecWebSocketVersionInterceptor();
        interceptor.init(getFilterConfig(protocol));
        HttpServletResponse response = mock(HttpServletResponse.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HeaderConfig.X_ATMO_WEBSOCKET_PROXY, true);

        interceptor.doFilter(request, response, new MockFilterChain());

        assertNull(response.getHeader(SecWebSocketVersionInterceptor.SEC_WEB_SOCKET_PROTOCOL_HEADER));
        verify(response, never()).sendError(eq(HttpServletResponse.SC_NOT_IMPLEMENTED), anyString());
    }

    @Test
    public void doFilterUnsupportedProtocol() throws ServletException, IOException {
        String supportedProtocol = "ocpp1.5";
        String unsupportedProtocol = "ocpp2.0";
        SecWebSocketVersionInterceptor interceptor = new SecWebSocketVersionInterceptor();
        interceptor.init(getFilterConfig(supportedProtocol));
        HttpServletResponse response = mock(HttpServletResponse.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HeaderConfig.X_ATMO_WEBSOCKET_PROXY, true);
        request.addHeader(SecWebSocketVersionInterceptor.SEC_WEB_SOCKET_PROTOCOL_HEADER, unsupportedProtocol);

        interceptor.doFilter(request, response, new MockFilterChain());

        assertNull(response.getHeader(SecWebSocketVersionInterceptor.SEC_WEB_SOCKET_PROTOCOL_HEADER));
        verify(response).sendError(eq(HttpServletResponse.SC_NOT_IMPLEMENTED), anyString());
    }

    @Test
    public void doFilterVerifyChainFilter() throws ServletException, IOException {
        String protocol = "ocpp1.5";
        SecWebSocketVersionInterceptor interceptor = new SecWebSocketVersionInterceptor();
        interceptor.init(getFilterConfig(protocol));
        HttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HeaderConfig.X_ATMO_WEBSOCKET_PROXY, true);
        request.addHeader(SecWebSocketVersionInterceptor.SEC_WEB_SOCKET_PROTOCOL_HEADER, protocol);
        FilterChain filterChain = mock(FilterChain.class);

        interceptor.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    private FilterConfig getFilterConfig(String subProtocol) {
        MockServletConfig mockServletConfig = new MockServletConfig();
        mockServletConfig.addInitParameter(SecWebSocketVersionInterceptor.SUPPORTED_WEB_SOCKET_PROTOCOL_PARAM, subProtocol);
        return new FilterConfigImpl(mockServletConfig);
    }

}
