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
package io.motown.chargingstationconfiguration.viewmodel.domain;

import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Connector;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ChargingStationTypeRepository;
import io.motown.domain.api.chargingstation.EvseId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DomainService {

    @Autowired
    private ChargingStationTypeRepository chargingStationTypeRepository;

    public Set<io.motown.domain.api.chargingstation.Evse> getEvses(String vendor, String model) {
        //TODO how will the database be filled? - Mark van den Bergh, Februari 5th 2014
        List<ChargingStationType> items = chargingStationTypeRepository.findByCodeAndManufacturerCode(model, vendor);

        Set<io.motown.domain.api.chargingstation.Evse> result = new HashSet<>();
        if (items.size() > 0) {
            ChargingStationType chargingStationType = items.get(0);

            for (Evse evse : chargingStationType.getEvses()) {
                List<io.motown.domain.api.chargingstation.Connector> connectors = new ArrayList<>(evse.getConnectors().size());
                for (Connector conn : evse.getConnectors()) {
                    connectors.add(new io.motown.domain.api.chargingstation.Connector(conn.getMaxAmp(), conn.getPhase(), conn.getVoltage(),
                            conn.getChargingProtocol(), conn.getCurrent(), conn.getConnectorType()));
                }

                result.add(new io.motown.domain.api.chargingstation.Evse(new EvseId(evse.getIdentifier()), connectors));
            }
        }

        return result;
    }

    public void setChargingStationTypeRepository(ChargingStationTypeRepository chargingStationTypeRepository) {
        this.chargingStationTypeRepository = chargingStationTypeRepository;
    }

}
