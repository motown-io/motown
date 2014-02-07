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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DomainService {

    private static final Logger LOG = LoggerFactory.getLogger(DomainService.class);

    @Autowired
    private ChargingStationTypeRepository chargingStationTypeRepository;

    /**
     * Retrieves a set of {@code io.motown.domain.api.chargingstation.Evse}s based on the vendor and model.
     *
     * @param vendor vendor code.
     * @param model model code.
     * @return set of Evses if they can be found, otherwise an empty set.
     */
    public Set<io.motown.domain.api.chargingstation.Evse> getEvses(String vendor, String model) {
        List<ChargingStationType> items = chargingStationTypeRepository.findByCodeAndManufacturerCode(model, vendor);

        Set<io.motown.domain.api.chargingstation.Evse> result = new HashSet<>();
        int resultSize = items.size();

        if (resultSize > 0) {
            ChargingStationType chargingStationType = items.get(0);

            for (Evse evse : chargingStationType.getEvses()) {
                result.add(new io.motown.domain.api.chargingstation.Evse(new EvseId(evse.getIdentifier()), convertViewModelConnectorsToDomain(evse.getConnectors())));
            }
        }

        if (LOG.isWarnEnabled() && resultSize > 1) {
            LOG.warn("Found more than 1 charging station type for vendor [{}] and model [{}]", vendor, model);
        }

        return result;
    }

    /**
     * Sets the charging station type repository.
     *
     * @param chargingStationTypeRepository repository to use.
     */
    public void setChargingStationTypeRepository(ChargingStationTypeRepository chargingStationTypeRepository) {
        this.chargingStationTypeRepository = chargingStationTypeRepository;
    }

    /**
     * Converts a list of {@code Connector}s to a list of {@code io.motown.domain.api.chargingstation.Connector}s for use
     * in commands.
     *
     * @param connectors list of viewmodel connectors
     * @return list of domain connectors
     */
    private List<io.motown.domain.api.chargingstation.Connector> convertViewModelConnectorsToDomain(List<Connector> connectors) {
        List<io.motown.domain.api.chargingstation.Connector> resultList = new ArrayList<>(connectors.size());

        for (Connector connector : connectors) {
            resultList.add(new io.motown.domain.api.chargingstation.Connector(connector.getMaxAmp(), connector.getPhase(), connector.getVoltage(),
                    connector.getChargingProtocol(), connector.getCurrent(), connector.getConnectorType()));
        }

        return resultList;
    }

}
