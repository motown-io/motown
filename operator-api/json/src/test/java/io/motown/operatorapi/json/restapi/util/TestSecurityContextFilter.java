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

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Utility class for mocking the authentication on the charging station resource.
 * This filter gets executed for each request and injects the user principal into the request.
 * This class is for testing purposes only.
 */
public class TestSecurityContextFilter implements ContainerRequestFilter {

    @Override
    public ContainerRequest filter(final ContainerRequest containerRequest) {
        containerRequest.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {

                    /**
                     * Returns the name of the user principal.
                     *
                     * @return {@code root} if the request doesn't contain the header {@code Non-Authorized-User}, else {@code non-root} is returned.
                     */
                    @Override
                    public String getName() {
                        return containerRequest.getHeaderValue("Non-Authorized-User") == null ? "root" : "non-root";
                    }
                };
            }

            @Override
            public boolean isUserInRole(String s) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return true;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        });
        return containerRequest;
    }
}