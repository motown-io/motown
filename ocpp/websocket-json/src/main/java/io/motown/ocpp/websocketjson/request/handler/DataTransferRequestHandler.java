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
package io.motown.ocpp.websocketjson.request.handler;

import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.chargingstation.DataTransferRequest;
import io.motown.ocpp.websocketjson.response.centralsystem.DataTransferResponse;
import io.motown.ocpp.websocketjson.response.centralsystem.DataTransferStatus;

public class DataTransferRequestHandler implements RequestHandler {

    private Gson gson;

    private DomainService domainService;

    public DataTransferRequestHandler(Gson gson, DomainService domainService) {
        this.gson = gson;
        this.domainService = domainService;
    }

    @Override
    public DataTransferResponse handleRequest(ChargingStationId chargingStationId, String payload) {
        DataTransferRequest request = gson.fromJson(payload, DataTransferRequest.class);

        domainService.dataTransfer(chargingStationId, request.getData(), request.getVendorId(), request.getMessageId());

        return new DataTransferResponse(DataTransferStatus.ACCEPTED, null);
    }
}
