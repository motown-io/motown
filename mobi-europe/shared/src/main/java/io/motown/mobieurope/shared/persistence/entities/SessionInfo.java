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
package io.motown.mobieurope.shared.persistence.entities;

import io.motown.mobieurope.shared.enums.ServiceTypeIdentifier;
import io.motown.mobieurope.shared.session.SessionStateMachine;

import javax.persistence.*;

@Entity
public class SessionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int transactionId;

    private String userIdentifier;

    private String authorizationIdentifier;

    private String requestIdentifier;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 1000000)
    private SessionStateMachine sessionStateMachine;

    private String pmsIdentifier;

    private String servicePms;

    private String localServiceIdentifier;

    private String connectorIdentifier;

    private ServiceTypeIdentifier serviceTypeIdentifier;

    public SessionInfo() {
        /* needed for persistence framework */
    }

    public SessionInfo(SessionStateMachine sessionStateMachine) {
        this.sessionStateMachine = sessionStateMachine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getAuthorizationIdentifier() {
        return authorizationIdentifier;
    }

    public void setAuthorizationIdentifier(String authorizationIdentifier) {
        this.authorizationIdentifier = authorizationIdentifier;
    }

    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }

    public String getPmsIdentifier() {
        return pmsIdentifier;
    }

    public void setPmsIdentifier(String pmsIdentifier) {
        this.pmsIdentifier = pmsIdentifier;
    }

    public String getServicePms() {
        return servicePms;
    }

    public void setServicePms(String servicePms) {
        this.servicePms = servicePms;
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

    public SessionStateMachine getSessionStateMachine() {
        return sessionStateMachine;
    }


}
