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
package io.motown.ocpp.websocketjson.response.handler;

import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.ChargingStationComponent;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.EvseId;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.Changeavailability;
import io.motown.ocpp.websocketjson.schema.generated.v15.ChangeavailabilityResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeAvailabilityResponseHandler extends ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeAvailabilityResponseHandler.class);

    private final EvseId evseId;

    private final Changeavailability.Type availabilityType;

    public ChangeAvailabilityResponseHandler(EvseId evseId, Changeavailability.Type availabilityType, CorrelationToken correlationToken) {
        this.evseId = evseId;
        this.availabilityType = availabilityType;
        this.setCorrelationToken(correlationToken);
    }

    @Override
    public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        ChangeavailabilityResponse response = gson.fromJson(wampMessage.getPayloadAsString(), ChangeavailabilityResponse.class);

        switch (response.getStatus()) {
            case ACCEPTED:
            case SCHEDULED:
                //Upon a successfull change in availability we inform about the new state the evse is in
                if (Changeavailability.Type.INOPERATIVE.equals(availabilityType)) {
                    if (evseId.getNumberedId() == 0) {
                        domainService.changeChargingStationAvailabilityToInoperative(chargingStationId, getCorrelationToken(), addOnIdentity);
                    } else {
                        domainService.changeComponentAvailabilityToInoperative(chargingStationId, evseId, ChargingStationComponent.EVSE, getCorrelationToken(), addOnIdentity);
                    }
                } else {
                    if (evseId.getNumberedId() == 0) {
                        domainService.changeChargingStationAvailabilityToOperative(chargingStationId, getCorrelationToken(), addOnIdentity);
                    } else {
                        domainService.changeComponentAvailabilityToOperative(chargingStationId, evseId, ChargingStationComponent.EVSE, getCorrelationToken(), addOnIdentity);
                    }
                }
                break;
            case REJECTED:
                LOG.info("Failed to set availability of evse {} on chargingstation {} to {}", evseId, chargingStationId, availabilityType.toString());
                break;
            default:
                throw new AssertionError(String.format("Unexpected status: {}", response.getStatus()));
        }
    }
}
