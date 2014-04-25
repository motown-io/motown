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
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.repositories.ChargingStationRepository;
import org.axonframework.commandhandling.CommandCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateChargingStationCommandCallback implements CommandCallback<Object> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateChargingStationCommandCallback.class);

    private ChargingStationId chargingStationId;
    private String chargingStationAddress;
    private String vendor;
    private String model;
    private String protocol;
    private ChargingStationRepository chargingStationRepository;
    private DomainService domainService;
    private String chargePointSerialNumber;
    private String chargeBoxSerialNumber;
    private String firmwareVersion;
    private String iccid;
    private String imsi;
    private String meterType;
    private String meterSerialNumber;
    private AddOnIdentity addOnIdentity;

    public CreateChargingStationCommandCallback(ChargingStationId chargingStationId, String chargingStationAddress, String vendor, String model, String protocol, String chargePointSerialNumber, String chargeBoxSerialNumber, String firmwareVersion, String iccid, String imsi, String meterType, String meterSerialNumber,
                                                AddOnIdentity addOnIdentity, ChargingStationRepository chargingStationRepository, DomainService domainService) {
        this.chargingStationId = chargingStationId;
        this.chargingStationAddress = chargingStationAddress;
        this.vendor = vendor;
        this.model = model;
        this.chargePointSerialNumber = chargePointSerialNumber;
        this.chargeBoxSerialNumber = chargeBoxSerialNumber;
        this.firmwareVersion = firmwareVersion;
        this.iccid = iccid;
        this.imsi = imsi;
        this.meterType = meterType;
        this.meterSerialNumber = meterSerialNumber;
        this.protocol = protocol;
        this.addOnIdentity = addOnIdentity;
        this.chargingStationRepository = chargingStationRepository;
        this.domainService = domainService;
    }

    @Override
    public void onSuccess(Object o) {
        chargingStationRepository.createOrUpdate(new ChargingStation(chargingStationId.getId()));

        domainService.bootChargingStation(chargingStationId, chargingStationAddress, vendor, model, protocol, chargePointSerialNumber, chargeBoxSerialNumber, firmwareVersion, iccid, imsi, meterType, meterSerialNumber, addOnIdentity);
    }

    @Override
    public void onFailure(Throwable throwable) {
        LOG.error("CreateChargingStationCommand failed. " + throwable.getMessage());
    }
}
