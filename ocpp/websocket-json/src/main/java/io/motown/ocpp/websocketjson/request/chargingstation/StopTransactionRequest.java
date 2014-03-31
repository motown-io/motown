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
import java.util.Date;
import java.util.List;

@Deprecated
public class StopTransactionRequest {

    private int transactionId;

    private String idTag;

    private Date timestamp;

    private int meterStop;

    private List<TransactionData> transactionData = new ArrayList<>();

    public StopTransactionRequest(int transactionId, String idTag, Date timestamp, int meterStop, List<TransactionData> transactionData) {
        this.transactionId = transactionId;
        this.idTag = idTag;
        this.timestamp = timestamp != null ? new Date(timestamp.getTime()) : null;
        this.meterStop = meterStop;
        this.transactionData = transactionData;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getIdTag() {
        return idTag;
    }

    public Date getTimestamp() {
        return timestamp != null ? new Date(timestamp.getTime()) : null;
    }

    public int getMeterStop() {
        return meterStop;
    }

    public List<TransactionData> getTransactionData() {
        return transactionData;
    }
}
