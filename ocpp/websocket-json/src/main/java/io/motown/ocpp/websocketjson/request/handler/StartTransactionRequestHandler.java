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
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.utils.AttributeMap;
import io.motown.domain.utils.AttributeMapKeys;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.domain.utils.axon.FutureEventCallback;
import io.motown.ocpp.websocketjson.schema.generated.v15.Starttransaction;
import org.atmosphere.websocket.WebSocket;

public class StartTransactionRequestHandler extends RequestHandler {

    private Gson gson;

    private DomainService domainService;

    private String protocolIdentifier;

    private AddOnIdentity addOnIdentity;

    public StartTransactionRequestHandler(Gson gson, DomainService domainService, String protocolIdentifier, AddOnIdentity addOnIdentity) {
        this.gson = gson;
        this.domainService = domainService;
        this.protocolIdentifier = protocolIdentifier;
        this.addOnIdentity = addOnIdentity;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        Starttransaction request = gson.fromJson(payload, Starttransaction.class);

        AttributeMap<String, String> attributes = new AttributeMap<String, String>().
                putIfValueNotNull(AttributeMapKeys.RESERVATION_ID, request.getReservationId() != null ? request.getReservationId().toString() : null);

        StartTransactionInfo startTransactionInfo = new StartTransactionInfo(new EvseId(request.getConnectorId()),
                request.getMeterStart(), request.getTimestamp(), new TextualToken(request.getIdTag()), attributes);

        FutureEventCallback futureEventCallback = new StartTransactionFutureEventCallback(callId, webSocket, gson, chargingStationId,
                protocolIdentifier, startTransactionInfo, domainService, addOnIdentity);

        // futureEventCallback will handle authorize result and trigger a startTransaction command
        domainService.authorize(chargingStationId, request.getIdTag(), futureEventCallback, addOnIdentity);
    }
}
