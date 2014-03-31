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
package io.motown.ocpp.websocketjson.request.chargingstation;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class MeterValuesRequest {

    private int connectorId;

    private Integer transactionId;

    private List<MeterValue> values = new ArrayList<>();

    public MeterValuesRequest(int connectorId, Integer transactionId, List<MeterValue> values) {
        this.connectorId = connectorId;
        this.transactionId = transactionId;
        this.values = values;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public List<MeterValue> getValues() {
        return values;
    }

}
