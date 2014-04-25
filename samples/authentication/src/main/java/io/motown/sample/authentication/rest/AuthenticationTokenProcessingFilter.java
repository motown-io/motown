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
package io.motown.sample.authentication.rest;

import io.motown.sample.authentication.userdetails.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    private static final String AUTH_TOKEN_HEADER_KEY = "X-Auth-Token";
    private static final String AUTH_TOKEN_PARAMETER_KEY = "token";
    private final UserDetailsServiceImpl userService;

    public AuthenticationTokenProcessingFilter(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Retrieves the authorization token from the request and validates it against the {@code UserService}.
     *
     * @param request  servlet request.
     * @param response servlet response.
     * @param chain    filter chain.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String authToken = getAuthTokenFromRequest(httpRequest);
        String userName = TokenUtils.getUserNameFromToken(authToken);

        if (userName != null) {
            UserDetails userDetails = userService.loadUserByUsername(userName);

            if (TokenUtils.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Gets the authorization token from the request. First tries the header, if that's empty the parameter is checked.
     *
     * @param httpRequest request.
     * @return authorization token if it exists in the request.
     */
    private String getAuthTokenFromRequest(HttpServletRequest httpRequest) {
        String authToken = httpRequest.getHeader(AUTH_TOKEN_HEADER_KEY);

        if (authToken == null) {
            // token can also exist as request parameter
            authToken = httpRequest.getParameter(AUTH_TOKEN_PARAMETER_KEY);
        }

        return authToken;
    }
}
