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
package io.motown.mobieurope.source.entities;

import io.motown.mobieurope.destination.soap.schema.AuthorizeResponse;
import io.motown.mobieurope.destination.soap.schema.ResponseError;

import static com.google.common.base.Preconditions.checkNotNull;

public class SourceAuthorizeResponse {

    private String authorizationIdentifier;

    private ResponseError responseError;

    public SourceAuthorizeResponse(ResponseError responseError) {
        checkNotNull(responseError);
        this.responseError = responseError;
    }

    public SourceAuthorizeResponse(AuthorizeResponse authorizeResponse) {
        checkNotNull(authorizeResponse);

        this.responseError = authorizeResponse.getResponseError();
        this.authorizationIdentifier = authorizeResponse.getAuthorizationIdentifier();
    }

    public String getAuthorizationIdentifier() {
        return authorizationIdentifier;
    }

    public ResponseError getResponseError() {
        return responseError;
    }

    public boolean hasError() {
        return responseError != null;
    }
}
