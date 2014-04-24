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
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ChargingStationTypeRepository;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ConnectorRepository;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.EvseRepository;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ManufacturerRepository;
import io.motown.domain.api.chargingstation.EvseId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DomainService {

    private ChargingStationTypeRepository chargingStationTypeRepository;
    private ConnectorRepository connectorRepository;
    private EvseRepository evseRepository;
    private ManufacturerRepository manufacturerRepository;

    /**
     * Retrieves a set of {@code io.motown.domain.api.chargingstation.Evse}s based on the vendor and model.
     *
     * @param vendor vendor code.
     * @param model  model code.
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

        return result;
    }

    /**
     * Converts a set of {@code Connector}s to a list of {@code io.motown.domain.api.chargingstation.Connector}s for use
     * in commands.
     *
     * @param connectors set of viewmodel connectors
     * @return list of domain connectors
     */
    private List<io.motown.domain.api.chargingstation.Connector> convertViewModelConnectorsToDomain(Set<Connector> connectors) {
        List<io.motown.domain.api.chargingstation.Connector> resultList = new ArrayList<>(connectors.size());

        for (Connector connector : connectors) {
            resultList.add(new io.motown.domain.api.chargingstation.Connector(connector.getMaxAmp(), connector.getPhase(), connector.getVoltage(),
                    connector.getChargingProtocol(), connector.getCurrent(), connector.getConnectorType()));
        }

        return resultList;
    }

    /**
     * Create a charging station type.
     *
     * @param chargingStationType the entity to be persisted.
     */
    public ChargingStationType createChargingStationType(ChargingStationType chargingStationType) {
        return chargingStationTypeRepository.createOrUpdate(chargingStationType);
    }

    /**
     * Update a charging station type.
     *
     * @param id the id of the entity to find.
     * @param chargingStationType the payload from the request.
     */
    public ChargingStationType updateChargingStationType(Long id, ChargingStationType chargingStationType) {
        chargingStationType.setId(id);
        return chargingStationTypeRepository.createOrUpdate(chargingStationType);
    }

    /**
     * Find all charging station types.
     *
     * @return a list of charging station types.
     */
    public List<ChargingStationType> getChargingStationTypes() {
        return chargingStationTypeRepository.findAll();
    }

    /**
     * Find a charging station type based on its id.
     *
     * @param id the id of the entity to find.
     * @return the charging station type.
     */
    public ChargingStationType getChargingStationType(Long id) {
        return chargingStationTypeRepository.findOne(id);
    }

    /**
     * Delete a charging station type.
     *
     * @param id the id of the entity to delete.
     */
    public void deleteChargingStationType(Long id) {
        chargingStationTypeRepository.delete(id);
    }

    /**
     * Update a connector.
     *
     * @param id the id of the entity to find.
     * @param connector the payload from the request.
     */
    public Connector updateConnector(Long id, Connector connector) {
        connector.setId(id);
        return connectorRepository.createOrUpdate(connector);
    }

    /**
     * Find a connector based on its id.
     *
     * @param id the id of the entity to find.
     * @return the connector.
     */
    public Connector getConnector(Long id) {
        return connectorRepository.findOne(id);
    }

    /**
     * Delete a connector.
     *
     * @param id the id of the entity to delete.
     */
    public void deleteConnector(Long id) {
        connectorRepository.delete(id);
    }

    /**
     * Update an evse.
     *
     * @param id the id of the entity to find.
     * @param evse the payload from the request.
     */
    public Evse updateEvse(Long id, Evse evse) {
        evse.setId(id);
        return evseRepository.createOrUpdate(evse);
    }

    /**
     * Find an evse based on its id.
     *
     * @param id the id of the entity to find.
     * @return the evse.
     */
    public Evse getEvse(Long id) {
        return evseRepository.findOne(id);
    }

    /**
     * Delete an evse.
     *
     * @param id the id of the entity to delete.
     */
    public void deleteEvse(Long id) {
        evseRepository.delete(id);
    }

    /**
     * Create a manufacturer.
     *
     * @param manufacturer the entity to be persisted.
     */
    public Manufacturer createManufacturer(Manufacturer manufacturer) {
        return manufacturerRepository.createOrUpdate(manufacturer);
    }

    /**
     * Update a manufacturer.
     *
     * @param id the id of the entity to find.
     * @param manufacturer the payload from the request.
     */
    public Manufacturer updateManufacturer(Long id, Manufacturer manufacturer) {
        manufacturer.setId(id);
        return manufacturerRepository.createOrUpdate(manufacturer);
    }

    /**
     * Find all manufacturers.
     *
     * @return a list of manufacturers.
     */
    public List<Manufacturer> getManufacturers() {
        return manufacturerRepository.findAll();
    }

    /**
     * Find a manufacturer based on its id.
     *
     * @param id the id of the entity to find.
     * @return the manufacturer.
     */
    public Manufacturer getManufacturer(Long id) {
        return manufacturerRepository.findOne(id);
    }

    /**
     * Delete a manufacturer.
     *
     * @param id the id of the entity to delete.
     */
    public void deleteManufacturer(Long id) {
        manufacturerRepository.delete(id);
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
     * Sets the connector repository.
     *
     * @param connectorRepository repository to use.
     */
    public void setConnectorRepository(ConnectorRepository connectorRepository) {
        this.connectorRepository = connectorRepository;
    }

    /**
     * Sets the evse repository.
     *
     * @param evseRepository repository to use.
     */
    public void setEvseRepository(EvseRepository evseRepository) {
        this.evseRepository = evseRepository;
    }

    /**
     * Sets the manufacturer repository.
     *
     * @param manufacturerRepository repository to use.
     */
    public void setManufacturerRepository(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }
}
