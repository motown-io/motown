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
package io.motown.ocpp.viewmodel.domain;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.utils.AttributeMap;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.repositories.ChargingStationRepository;
import org.axonframework.commandhandling.CommandCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateChargingStationCommandCallback implements CommandCallback<Object> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateChargingStationCommandCallback.class);

    private ChargingStationId chargingStationId;
    private String protocol;
    private AttributeMap<String, String> attributes;
    private ChargingStationRepository chargingStationRepository;
    private DomainService domainService;
    private AddOnIdentity addOnIdentity;

    public CreateChargingStationCommandCallback(ChargingStationId chargingStationId, String protocol, AttributeMap<String, String> attributes,
                                                AddOnIdentity addOnIdentity, ChargingStationRepository chargingStationRepository, DomainService domainService) {
        this.chargingStationId = chargingStationId;
        this.protocol = protocol;
        this.attributes = attributes;
        this.addOnIdentity = addOnIdentity;
        this.chargingStationRepository = chargingStationRepository;
        this.domainService = domainService;
    }

    @Override
    public void onSuccess(Object o) {
        chargingStationRepository.createOrUpdate(new ChargingStation(chargingStationId.getId()));

        domainService.bootChargingStation(chargingStationId, protocol, attributes, addOnIdentity);
    }

    @Override
    public void onFailure(Throwable throwable) {
        LOG.error("CreateChargingStationCommand failed. " + throwable.getMessage());
    }
}
