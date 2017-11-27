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
package io.motown.identificationauthorization.localplugin;

import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.domain.api.chargingstation.TextualToken;
import io.motown.identificationauthorization.localplugin.persistence.entities.LocalAuthChargingStation;
import io.motown.identificationauthorization.localplugin.persistence.entities.LocalToken;
import io.motown.identificationauthorization.localplugin.persistence.repositories.ChargingStationRepository;
import io.motown.identificationauthorization.localplugin.persistence.repositories.TokenRepository;
import io.motown.identificationauthorization.pluginapi.AuthorizationProvider;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class LocalAuthorization implements AuthorizationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(LocalAuthorization.class);

    private TokenRepository tokenRepository;

    private ChargingStationRepository chargingStationRepository;

    @EventHandler
    void onEvent(ChargingStationCreatedEvent event) {
        String chargingStationId = event.getChargingStationId().getId();
        LocalAuthChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId);

        if (chargingStation == null) {
            chargingStationRepository.createOrUpdate(new LocalAuthChargingStation(chargingStationId));
        }
    }

    @Override
    public IdentifyingToken validate(IdentifyingToken identifyingToken, @Nullable ChargingStationId chargingStationId) {
        LOG.debug("validate({}, {})", identifyingToken.getToken(), chargingStationId);

        String stationIdentifier = null;
        if (chargingStationId != null) {
            stationIdentifier = chargingStationId.getId();
        }

        LocalToken localToken = tokenRepository.findToken(identifyingToken.getToken(), stationIdentifier);

        if (localToken != null && localToken.isValid()) {
            LOG.debug("validation success on local token {}", localToken.getHiddenId());
            return new TextualToken(localToken.getHiddenId(), IdentifyingToken.AuthenticationStatus.ACCEPTED, null, localToken.getVisualId());
        } else {
            LOG.debug("validation failed on token {}", identifyingToken.getToken());
            return identifyingToken;
        }
    }

    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }
}
