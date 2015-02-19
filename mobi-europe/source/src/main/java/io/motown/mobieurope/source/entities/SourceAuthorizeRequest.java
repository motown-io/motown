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

import io.motown.mobieurope.destination.soap.schema.AuthorizeRequest;
import io.motown.mobieurope.destination.soap.schema.ServiceType;
import io.motown.mobieurope.shared.enums.ServiceTypeIdentifier;

import static com.google.common.base.Objects.toStringHelper;

public class SourceAuthorizeRequest extends SourceRequest {

    private String pmsIdentifier;                           // The user PMS

    private String servicePms;                              // PMS of the Service (Charging station)

    private String userIdentifier;                          // IdTag of the user

    private String localServiceIdentifier;                  // Name of the Service (Charging station name)

    private String connectorIdentifier;                     // Position of the connector

    private ServiceTypeIdentifier serviceTypeIdentifier;    // The type of service this local service provides

    public AuthorizeRequest getAuthorizeRequest() {
        AuthorizeRequest authorizeRequest = new AuthorizeRequest();
        authorizeRequest.setPmsIdentifier(this.pmsIdentifier);
        authorizeRequest.setUserIdentifier(this.userIdentifier);
        authorizeRequest.setServiceTypeIdentifier(ServiceType.fromValue(this.serviceTypeIdentifier.value()));
        authorizeRequest.setLocalServiceIdentifier(this.localServiceIdentifier);
        authorizeRequest.setConnectorIdentifier(this.connectorIdentifier);
        return authorizeRequest;
    }

    public String getServicePms() {
        return servicePms;
    }

    public void setServicePms(String servicePms) {
        this.servicePms = servicePms;
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

    public String getPmsIdentifier() {
        return pmsIdentifier;
    }

    public void setPmsIdentifier(String pmsIdentifier) {
        this.pmsIdentifier = pmsIdentifier;
    }

    public ServiceTypeIdentifier getServiceTypeIdentifier() {
        return serviceTypeIdentifier;
    }

    public void setServiceTypeIdentifier(ServiceTypeIdentifier serviceTypeIdentifier) {
        this.serviceTypeIdentifier = serviceTypeIdentifier;
    }

    @Override
    public boolean isValid() {
        return !empty(pmsIdentifier) && !empty(servicePms) && !empty(userIdentifier) && !empty(localServiceIdentifier);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("pmsIdentifier", pmsIdentifier)
                .add("servicePms", servicePms)
                .add("userIdentifier", userIdentifier)
                .add("localServiceIdentifier", localServiceIdentifier)
                .add("connectorIdentifier", connectorIdentifier)
                .add("serviceTypeIdentifier", serviceTypeIdentifier)
                .toString();
    }
}
