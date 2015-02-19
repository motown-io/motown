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

import io.motown.mobieurope.source.soap.schema.EndTransactionRequest;

import javax.xml.datatype.XMLGregorianCalendar;

public class DestinationEndTransactionRequest extends DestinationRequest {

    private String authorizationIdentifier;

    private XMLGregorianCalendar startTransactionTimestamp;

    private XMLGregorianCalendar endTransactionTimestamp;

    private TransactionData transactionData;

    public DestinationEndTransactionRequest(String authorizationIdentifier, XMLGregorianCalendar startTransactionTimestamp, XMLGregorianCalendar endTransactionTimestamp, TransactionData transactionData) {
        this.authorizationIdentifier = authorizationIdentifier;
        this.startTransactionTimestamp = startTransactionTimestamp;
        this.endTransactionTimestamp = endTransactionTimestamp;
        this.transactionData = transactionData;
    }

    public EndTransactionRequest getEndTransactionRequest() {
        EndTransactionRequest endTransactionRequest = new EndTransactionRequest();
        endTransactionRequest.setAuthorizationIdentifier(this.authorizationIdentifier);
        endTransactionRequest.setStartTransactionTimestamp(this.startTransactionTimestamp);
        endTransactionRequest.setEndTransactionTimestamp(this.endTransactionTimestamp);
        endTransactionRequest.setTransactionData(this.transactionData.getTransactionData());
        return endTransactionRequest;
    }


    public boolean isValid() {
        return !empty(authorizationIdentifier) && startTransactionTimestamp != null && transactionData.isValid();
    }
}
