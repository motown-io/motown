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
package io.motown.mobieurope.destination.entities;

import com.google.common.base.Objects;
import io.motown.mobieurope.destination.soap.schema.RequestStartTransactionRequest;

public class DestinationRequestStartTransactionRequest extends DestinationRequest {

    private String authorizationIdentifier;

    private String requestIdentifier;

    public DestinationRequestStartTransactionRequest(RequestStartTransactionRequest requestStartTransactionRequest) {
        this.authorizationIdentifier = requestStartTransactionRequest.getAuthorizationIdentifier();
        this.requestIdentifier = requestStartTransactionRequest.getRequestIdentifier();
    }

    public String getAuthorizationIdentifier() {
        return authorizationIdentifier;
    }

    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    public boolean isValid() {
        return !empty(authorizationIdentifier) && !empty(requestIdentifier);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("authorizationIdentifier", authorizationIdentifier)
                .add("requestIdentifier", requestIdentifier)
                .toString();
    }
}
