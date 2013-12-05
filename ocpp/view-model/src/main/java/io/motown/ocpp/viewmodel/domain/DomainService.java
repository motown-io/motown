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

import com.google.common.collect.Maps;
import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.entities.TransactionIdentifier;
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ocpp.viewmodel.persistence.repostories.TransactionIdentifierRepository;
import org.axonframework.commandhandling.CommandCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DomainService {

    private static final Logger log = LoggerFactory.getLogger(DomainService.class);

    @Resource(name = "domainCommandGateway")
    private DomainCommandGateway commandGateway;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private TransactionIdentifierRepository transactionIdentifierRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public BootChargingStationResult bootChargingStation(final ChargingStationId chargingStationId, final String chargingStationAddress, final String vendor, final String model) {

        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId.getId());

        if(chargingStation == null) {
            log.debug("Not a known charging station on boot notification, we send a CreateChargingStationCommand.");

            // TODO do we need a callback which sends a BootChargingStationCommand? - Mark van den Bergh, December 5th 2013
            commandGateway.send(new CreateChargingStationCommand(chargingStationId, false));

            // we didn't know the charging station when this bootNotification occurred so we reject it.
            return new BootChargingStationResult(false, 60, new Date());
        } else {
            log.debug("We know this charging station, let's continue handling the boot notification.");
        }

        // Keep track of the address on which we can reach the charging station
        chargingStation.setIpAddress(chargingStationAddress);
        chargingStationRepository.save(chargingStation);

        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("vendor", vendor);
        attributes.put("model", model);
        attributes.put("address", chargingStationAddress);

        commandGateway.send(new BootChargingStationCommand(chargingStationId, attributes));

        // TODO: Where should the heartbeat-interval (60) come from? - Mark van den Bergh, November 15th 2013
        return new BootChargingStationResult(chargingStation.isRegistered(), 60, new Date());
    }

    public AuthorizationResult authorize(ChargingStationId chargingStationId, String idTag){
        AuthorizeCommand command = new AuthorizeCommand(chargingStationId, idTag);
        AuthorizationResultStatus resultStatus = commandGateway.sendAndWait(command, 60, TimeUnit.SECONDS);

        return new AuthorizationResult(idTag, resultStatus);
    }
    
    public void configureChargingStation(ChargingStationId chargingStationId, Map<String, String> configurationItems) {
        ConfigureChargingStationCommand command = new ConfigureChargingStationCommand(chargingStationId, configurationItems);
        commandGateway.send(command);
    }

    public int startTransaction(ChargingStationId chargingStationId, int connectorId, String idTag, int meterStart, Date timestamp) {
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId.getId());
        if(chargingStation == null) {
            throw new IllegalStateException("Cannot start transaction for an unknown charging station.");
        }

        if(!chargingStation.isRegisteredAndConfigured()) {
            throw new IllegalStateException("Cannot start transaction for charging station that has not been registered/configured.");
        }

        if(connectorId <= 0 || connectorId > chargingStation.getNumberOfConnectors()) {
            throw new IllegalStateException("Cannot start transaction on a unknown connector.");
        }

        String transactionId = generateTransactionIdentifier(chargingStationId);

        StartTransactionCommand command = new StartTransactionCommand(chargingStationId, transactionId, connectorId, idTag, meterStart, timestamp);
        commandGateway.send(command);

        return TransactionIdentifierTranslator.toInt(transactionId);
    }

    public void stopTransaction(ChargingStationId chargingStationId, int transactionId, String idTag, int meterValueStop, Date timeStamp){
        StopTransactionCommand command = new StopTransactionCommand(chargingStationId, Integer.toString(transactionId), idTag, meterValueStop, timeStamp);
        commandGateway.send(command);
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }

    public void setTransactionIdentifierRepository(TransactionIdentifierRepository transactionIdentifierRepository) {
        this.transactionIdentifierRepository = transactionIdentifierRepository;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public String retrieveChargingStationAddress(ChargingStationId id) {
        ChargingStation chargingStation = chargingStationRepository.findOne(id.getId());

        return chargingStation != null? chargingStation.getIpAddress() : "";
    }

    /**
     * Generates a transaction identifier based on the charging station, the module (OCPP) and a auto-incremented number.
     *
     * @param chargingStationId charging station identifier to use when generating a transaction identifier.
     * @return transaction identifier based on the charging station, module and auto-incremented number.
     */
    private String generateTransactionIdentifier(ChargingStationId chargingStationId) {
        TransactionIdentifier identifier = new TransactionIdentifier();

        transactionIdentifierRepository.saveAndFlush(identifier); // flush to make sure the generated id is populated

        return TransactionIdentifierTranslator.toString(chargingStationId, (Long) entityManagerFactory.getPersistenceUnitUtil().getIdentifier(identifier));
    }

}
