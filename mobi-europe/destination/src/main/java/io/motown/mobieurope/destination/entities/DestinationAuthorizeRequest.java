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

import io.motown.mobieurope.destination.soap.schema.AuthorizeRequest;
import io.motown.mobieurope.shared.enums.ServiceTypeIdentifier;

import static com.google.common.base.Objects.toStringHelper;

public class DestinationAuthorizeRequest extends DestinationRequest {

    private String pmsIdentifier;

    private String userIdentifier;

    private String localServiceIdentifier;

    private String connectorIdentifier;

    private ServiceTypeIdentifier serviceTypeIdentifier;

    public DestinationAuthorizeRequest(AuthorizeRequest authorizeRequest) {
        this.pmsIdentifier = authorizeRequest.getPmsIdentifier();
        this.userIdentifier = authorizeRequest.getUserIdentifier();
        this.localServiceIdentifier = authorizeRequest.getLocalServiceIdentifier();
        this.connectorIdentifier = authorizeRequest.getConnectorIdentifier();
        this.serviceTypeIdentifier = ServiceTypeIdentifier.fromValue(authorizeRequest.getServiceTypeIdentifier().value());
    }

    public String getPmsIdentifier() {
        return pmsIdentifier;
    }

    public void setPmsIdentifier(String pmsIdentifier) {
        this.pmsIdentifier = pmsIdentifier;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getLocalServiceIdentifier() {
        return localServiceIdentifier;
    }

    public void setLocalServiceIdentifier(String localServiceIdentifier) {
        this.localServiceIdentifier = localServiceIdentifier;
    }

    public String getConnectorIdentifier() {
        return connectorIdentifier;
    }

    public void setConnectorIdentifier(String connectorIdentifier) {
        this.connectorIdentifier = connectorIdentifier;
    }

    public ServiceTypeIdentifier getServiceTypeIdentifier() {
        return serviceTypeIdentifier;
    }

    public void setServiceTypeIdentifier(ServiceTypeIdentifier serviceTypeIdentifier) {
        this.serviceTypeIdentifier = serviceTypeIdentifier;
    }

    @Override
    public boolean isValid() {
        return !empty(pmsIdentifier) && !empty(localServiceIdentifier) && !empty(userIdentifier) && !empty(connectorIdentifier);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("pmsIdentifier", pmsIdentifier)
                .add("userIdentifier", userIdentifier)
                .add("localServiceIdentifier", localServiceIdentifier)
                .add("connectorIdentifier", connectorIdentifier)
                .add("serviceTypeIdentifier", serviceTypeIdentifier)
                .toString();
    }
}
