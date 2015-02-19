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
package io.motown.mobieurope.source.api.soap;

import io.motown.mobieurope.shared.persistence.entities.SessionInfo;
import io.motown.mobieurope.source.persistence.repository.SourceSessionRepository;
import io.motown.mobieurope.source.soap.schema.*;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(
        serviceName = "PmsServerService",
        portName = "PmsServerBinding",
        targetNamespace = "http://webservice.pms.mobieurope.com",
        endpointInterface = "io.motown.mobieurope.source.soap.schema.PmsServerPortType",
        wsdlLocation = "wsdl/SourcePMS.wsdl"
)
public class SourceWebService implements PmsServerPortType {

    private SourceSessionRepository sourceSessionRepository;

    public void setSourceSessionRepository(SourceSessionRepository sourceSessionRepository) {
        this.sourceSessionRepository = sourceSessionRepository;
    }

    @Override
    public NotifyRequestResultResponse notifyRequestResult(NotifyRequestResultRequest notifyRequestResultRequest) {
        SessionInfo sessionInfo = sourceSessionRepository.findSessionInfoByRequestId(notifyRequestResultRequest.getRequestIdentifier());
        NotifyRequestResultResponse notifyRequestResultResponse = new NotifyRequestResultResponse();

        if (notifyRequestResultRequest.isRequestSuccess()) {
            if (sessionInfo.getSessionStateMachine().hasStateStartTransactionRequested()) {
                sessionInfo.getSessionStateMachine().eventStartOk();
                sourceSessionRepository.insertOrUpdateSessionInfo(sessionInfo);
            } else if (sessionInfo.getSessionStateMachine().hasStateStopTransactionRequested()) {
                sessionInfo.getSessionStateMachine().eventStopOk();
                sourceSessionRepository.insertOrUpdateSessionInfo(sessionInfo);
            } else {
                ResponseError responseError = new ResponseError();
                responseError.setErrorCode("400");
                responseError.setErrorMsg("Unexpected event in current state");
                notifyRequestResultResponse.setResponseError(responseError);
            }
        }
        return notifyRequestResultResponse;
    }

    @Override
    public TransactionProgressUpdateResponse transactionProgressUpdate(@WebParam(partName = "parameters", name = "transactionProgressUpdateRequest", targetNamespace = "http://webservice.pms.mobieurope.com") TransactionProgressUpdateRequest parameters) {
        ResponseError responseError = new ResponseError();
        responseError.setErrorMsg("Not implemented yet");
        responseError.setErrorCode("500");

        TransactionProgressUpdateResponse transactionProgressUpdateResponse = new TransactionProgressUpdateResponse();
        transactionProgressUpdateResponse.setResponseError(responseError);
        return transactionProgressUpdateResponse;
    }

    @Override
    public EndTransactionResponse endTransaction(@WebParam(partName = "parameters", name = "endTransactionRequest", targetNamespace = "http://webservice.pms.mobieurope.com") EndTransactionRequest parameters) {
        ResponseError responseError = new ResponseError();
        responseError.setErrorMsg("Not implemented yet");
        responseError.setErrorCode("500");

        EndTransactionResponse endTransactionResponse = new EndTransactionResponse();
        endTransactionResponse.setResponseError(responseError);
        return endTransactionResponse;
    }
}
