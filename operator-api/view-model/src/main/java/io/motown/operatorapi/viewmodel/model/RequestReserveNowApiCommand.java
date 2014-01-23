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
package io.motown.operatorapi.viewmodel.model;

import io.motown.domain.api.chargingstation.ConnectorId;
import io.motown.domain.api.chargingstation.TextualToken;

public class RequestReserveNowApiCommand implements ApiCommand {
    private ConnectorId connectorId;
    private TextualToken identifyingToken;
    private String expiryDate;

    public RequestReserveNowApiCommand() {
    }

    public ConnectorId getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(ConnectorId connectorId) {
        this.connectorId = connectorId;
    }

    public TextualToken getIdentifyingToken() {
        return identifyingToken;
    }

    public void setIdentifyingToken(TextualToken identifyingToken) {
        this.identifyingToken = identifyingToken;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
