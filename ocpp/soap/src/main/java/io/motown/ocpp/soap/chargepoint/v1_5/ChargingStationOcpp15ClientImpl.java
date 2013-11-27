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

package io.motown.ocpp.soap.chargepoint.v1_5;

import com.google.common.collect.Maps;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.ocpp.soap.chargepoint.v1_5.schema.ChargePointService;
import io.motown.ocpp.soap.chargepoint.v1_5.schema.GetConfigurationRequest;
import io.motown.ocpp.soap.chargepoint.v1_5.schema.GetConfigurationResponse;
import io.motown.ocpp.soap.chargepoint.v1_5.schema.KeyValue;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import io.motown.ocpp.viewmodel.domain.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class ChargingStationOcpp15ClientImpl implements ChargingStationOcpp15Client {

    private static final Logger log = LoggerFactory.getLogger(ChargingStationOcpp15ClientImpl.class);

    @Autowired
    private DomainService domainService;

    @Autowired
    private ChargePointService chargePointService;

    public void getConfiguration(ChargingStationId id) {
        log.info("Handling ConfigurationRequestedEvent from SOAP!");

        GetConfigurationRequest request = new GetConfigurationRequest();

        GetConfigurationResponse response =  chargePointService.getConfiguration(request, id.getId());

        HashMap<String, String> configurationItems = Maps.newHashMap();
        for (KeyValue keyValue : response.getConfigurationKey()){
            configurationItems.put(keyValue.getKey(), keyValue.getValue());
        }

        domainService.configureChargingStation(id, configurationItems);
    }

}
