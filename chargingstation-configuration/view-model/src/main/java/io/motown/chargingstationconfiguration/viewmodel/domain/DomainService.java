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
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ManufacturerRepository;
import io.motown.domain.api.chargingstation.EvseId;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DomainService {

    private ChargingStationTypeRepository chargingStationTypeRepository;

    private ConnectorRepository connectorRepository;

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
     * Creates a Evse in a charging station type.
     *
     * @param chargingStationTypeId    charging station type identifier.
     * @param evse                     evse object
     * @return created Evse
     */
    public Evse createEvse(Long chargingStationTypeId, Evse evse) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);

        if (getEvseByIdentifier(chargingStationType, evse.getIdentifier()) != null) {
            //TODO error
            return null;
        }

        chargingStationType.getEvses().add(evse);
        chargingStationType = chargingStationTypeRepository.createOrUpdate(chargingStationType);

        return getEvseByIdentifier(chargingStationType, evse.getIdentifier());
    }

    /**
     * Gets the Evses of a charging station type.
     *
     * @param chargingStationTypeId    charging station type identifier.
     * @return set of Evses
     */
    public Set<Evse> getEvses(Long chargingStationTypeId) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);

        return chargingStationType.getEvses();
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
     * Gets the connectors for a charging station type evse.
     *
     * @param chargingStationTypeId    charging station identifier.
     * @param evseId                   evse id.
     * @return set of connectors.
     */
    public Set<Connector> getConnectors(Long chargingStationTypeId, Long evseId) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);
        Evse evse = getEvseById(chargingStationType, evseId);

        return evse.getConnectors();
    }

    /**
     * Creates a connector in a charging station type evse.
     *
     * @param chargingStationTypeId    charging station identifier.
     * @param evseId                   evse id.
     * @param connector                connector to be created.
     * @return created connector.
     */
    public Connector createConnector(Long chargingStationTypeId, Long evseId, Connector connector) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);
        Evse evse = getEvseById(chargingStationType, evseId);

        evse.getConnectors().add(connector);

        chargingStationType = chargingStationTypeRepository.createOrUpdate(chargingStationType);

        // TODO get created connector
        return null;
    }

    /**
     * Update a connector.
     *
     * @param chargingStationTypeId charging station type identifier.
     * @param evseId                the id of the evse that contains the connector.
     * @param connector             the payload from the request.
     * @return updated connector.
     */
    public Connector updateConnector(Long chargingStationTypeId, Long evseId, Connector connector) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);
        Evse evse = getEvseById(chargingStationType, evseId);

        Connector existingConnector = getConnectorById(evse, connector.getId(), false);
        if (existingConnector != null) {
            evse.getConnectors().remove(existingConnector);
        }

        evse.getConnectors().add(connector);
        chargingStationType = chargingStationTypeRepository.createOrUpdate(chargingStationType);
        evse = getEvseById(chargingStationType, evseId);

        return getConnectorById(evse, connector.getId());
    }

    /**
     * Find a connector based on its id.
     *
     * @param id the id of the entity to find.
     * @return the connector.
     */
    public Connector getConnector(Long chargingStationTypeId, Long evseId, Long id) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);
        Evse evse = getEvseById(chargingStationType, evseId);

        return getConnectorById(evse, id);
    }

    /**
     * Delete a connector.
     *
     * @param id the id of the entity to delete.
     */
    public void deleteConnector(Long chargingStationTypeId, Long evseId, Long id) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);
        Evse evse = getEvseById(chargingStationType, evseId);

        Connector connector = getConnectorById(evse, id);
        evse.getConnectors().remove(connector);

        chargingStationTypeRepository.createOrUpdate(chargingStationType);
    }

    /**
     * Update an evse.
     *
     * @param evse evse object to update.
     * @return updated evse.
     */
    public Evse updateEvse(Long chargingStationTypeId, Evse evse) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);

        Evse existingEvse = getEvseById(chargingStationType, evse.getId());

        chargingStationType.getEvses().remove(existingEvse);
        chargingStationType.getEvses().add(evse);

        ChargingStationType updatedChargingStationType = chargingStationTypeRepository.createOrUpdate(chargingStationType);

        return getEvseById(updatedChargingStationType, evse.getId());
    }

    /**
     * Find an evse based on its id.
     *
     * @param chargingStationTypeId charging station identifier.
     * @param id                    the id of the evse to find.
     * @return the evse.
     */
    public Evse getEvse(Long chargingStationTypeId, Long id) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);

        return getEvseById(chargingStationType, id);
    }

    /**
     * Delete an evse.
     *
     * @param chargingStationTypeId charging station identifier.
     * @param id                    the id of the evse to delete.
     */
    public void deleteEvse(Long chargingStationTypeId, Long id) {
        ChargingStationType chargingStationType = chargingStationTypeRepository.findOne(chargingStationTypeId);

        chargingStationType.getEvses().remove(getEvseById(chargingStationType, id));

        updateChargingStationType(chargingStationType.getId(), chargingStationType);
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
     * Sets the manufacturer repository.
     *
     * @param manufacturerRepository repository to use.
     */
    public void setManufacturerRepository(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    /**
     * Gets a Evse by id.
     *
     * @param chargingStationType    charging station type.
     * @param id                     evse id.
     * @return evse
     * @throws EntityNotFoundException if the Evse cannot be found.
     */
    private Evse getEvseById(ChargingStationType chargingStationType, Long id) {
        for (Evse evse:chargingStationType.getEvses()) {
            if(id.equals(evse.getId())) {
                return evse;
            }
        }
        throw new EntityNotFoundException(String.format("Unable to find evse with id '%s'", id));
    }

    /**
     * Gets a Connector by id.
     *
     * @param evse                  evse which should contain the connector.
     * @param id                    connector id.
     * @return connector.
     * @throws EntityNotFoundException if the Connector cannot be found.
     */
    private Connector getConnectorById(Evse evse, Long id) {
        return getConnectorById(evse, id, true);
    }

    /**
     * Gets a Connector by id.
     *
     * @param evse                  evse which should contain the connector.
     * @param id                    connector id.
     * @param exceptionIfNotFound   throw EntityNotFoundException if connector cannot be found.
     * @return connector or null if it cannot be found and exceptionIfNotFound is false.
     * @throws EntityNotFoundException if exceptionIfNotFound is true and the Connector cannot be found.
     */
    private Connector getConnectorById(Evse evse, Long id, boolean exceptionIfNotFound) {
        for (Connector connector:evse.getConnectors()) {
            if (id.equals(connector.getId())) {
                return connector;
            }
        }
        if (exceptionIfNotFound) {
            throw new EntityNotFoundException(String.format("Unable to find connector with id '%s'", id));
        } else {
            return null;
        }
    }

    /**
     * Gets a Evse by identifier.
     *
     * @param chargingStationType    charging station type.
     * @param identifier             evse identifier.
     * @return evse or null if not found.
     */
    private Evse getEvseByIdentifier(ChargingStationType chargingStationType, int identifier) {
        for (Evse evse:chargingStationType.getEvses()) {
            if(identifier == evse.getIdentifier()) {
                return evse;
            }
        }
        return null;
    }

}
