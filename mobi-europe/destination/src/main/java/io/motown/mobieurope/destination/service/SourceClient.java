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
package io.motown.mobieurope.destination.service;

import io.motown.mobieurope.destination.entities.*;
import io.motown.mobieurope.source.soap.schema.*;

import javax.xml.ws.BindingProvider;

public class SourceClient {

    PmsServerPortType sourceWebService;
    private String endpoint;

    public SourceClient(String endpoint) {
        this.endpoint = endpoint;
    }

    public DestinationEndTransactionResponse endTransaction(DestinationEndTransactionRequest destinationEndTransactionRequest) {
        EndTransactionRequest endTransactionRequest = destinationEndTransactionRequest.getEndTransactionRequest();
        EndTransactionResponse endTransactionResponse = getSourceWebService().endTransaction(endTransactionRequest);

        return new DestinationEndTransactionResponse(endTransactionResponse);
    }

    public DestinationNotifyRequestResultResponse notifyRequestResult(DestinationNotifyRequestResultRequest destinationNotifyRequestResultRequest) {
        NotifyRequestResultRequest notifyRequestResultRequest = destinationNotifyRequestResultRequest.getNotifyRequestResultRequest();
        NotifyRequestResultResponse notifyRequestResultResponse = getSourceWebService().notifyRequestResult(notifyRequestResultRequest);

        return new DestinationNotifyRequestResultResponse(notifyRequestResultResponse);
    }

    public DestinationTransactionProgressUpdateResponse transactionProgressUpdate(DestinationTransactionProgressUpdateRequest destinationTransactionProgressUpdateRequest) {
        TransactionProgressUpdateRequest transactionProgressUpdateRequest = destinationTransactionProgressUpdateRequest.getTransactionProgressUpdateRequest();
        TransactionProgressUpdateResponse transactionProgressUpdateResponse = getSourceWebService().transactionProgressUpdate(transactionProgressUpdateRequest);

        return new DestinationTransactionProgressUpdateResponse(transactionProgressUpdateResponse);
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;

        if (sourceWebService != null) {
            ((BindingProvider) sourceWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.endpoint);
        }
    }

    private PmsServerPortType getSourceWebService() {
        if (sourceWebService == null) {
            PmsServerPortType sourceWebService = new PmsServerService().getPmsServerBinding();
            ((BindingProvider) sourceWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
            this.sourceWebService = sourceWebService;
        }
        return sourceWebService;
    }

    public void setSourceWebService(PmsServerPortType sourceWebService) {
        this.sourceWebService = sourceWebService;
    }
}
