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

import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.NullUserIdentity;
import io.motown.domain.api.security.UserIdentity;
import io.motown.domain.utils.AttributeMap;
import io.motown.domain.utils.AttributeMapKeys;
import io.motown.domain.utils.axon.EventWaitingGateway;
import io.motown.domain.utils.axon.FutureEventCallback;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.entities.ReservationIdentifier;
import io.motown.ocpp.viewmodel.persistence.entities.Transaction;
import io.motown.ocpp.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.ocpp.viewmodel.persistence.repositories.ReservationIdentifierRepository;
import io.motown.ocpp.viewmodel.persistence.repositories.TransactionRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import java.util.*;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

public class DomainService {

    public static final int CHARGING_STATION_EVSE_ID = 0;
    public static final String ERROR_CODE_KEY = "errorCode";
    public static final String INFO_KEY = "info";
    public static final String VENDOR_ID_KEY = "vendorId";
    public static final String VENDOR_ERROR_CODE_KEY = "vendorErrorCode";

    private static final Logger LOG = LoggerFactory.getLogger(DomainService.class);
    private DomainCommandGateway commandGateway;

    private ChargingStationRepository chargingStationRepository;

    private TransactionRepository transactionRepository;

    private ReservationIdentifierRepository reservationIdentifierRepository;

    private EntityManagerFactory entityManagerFactory;

    private int heartbeatInterval;

    private EventWaitingGateway eventWaitingGateway;

    /**
     * The timeout in milliseconds to wait for the authorization response events
     */
    private long authorizationTimeoutInMillis = 10000;

    /**
     * Set of user identities which shall be used in the {@code CreateChargingStationCommand} to indicate those users
     * are authorized to execute all commands on the created aggregate.
     */
    private Set<UserIdentity> userIdentitiesWithAllPermissions;

    public BootChargingStationResult bootChargingStation(ChargingStationId chargingStationId, String protocol,
                                                         AttributeMap<String, String> attributes, AddOnIdentity addOnIdentity) {
        // Check if we already know the charging station, or have to create one
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId.getId());

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        if (chargingStation == null) {
            LOG.debug("Not a known charging station on boot notification, we send a CreateChargingStationCommand.");

            commandGateway.send(new CreateChargingStationCommand(chargingStationId, userIdentitiesWithAllPermissions, identityContext), new CreateChargingStationCommandCallback(
                    chargingStationId, protocol, attributes, addOnIdentity, chargingStationRepository, this));

            // we didn't know the charging station when this bootNotification occurred so we reject it.
            return new BootChargingStationResult(false, heartbeatInterval, new Date());
        }

        // Keep track of the address on which we can reach the charging station
        chargingStation.setIpAddress(attributes.get(AttributeMapKeys.CHARGING_STATION_ADDRESS));
        chargingStation = chargingStationRepository.createOrUpdate(chargingStation);

        commandGateway.send(new BootChargingStationCommand(chargingStationId, protocol, attributes, identityContext));

        return new BootChargingStationResult(chargingStation.isRegisteredAndConfigured(), heartbeatInterval, new Date());
    }

    public void incomingDataTransfer(ChargingStationId chargingStationId, String data, String vendorId, String messageId, FutureEventCallback future, AddOnIdentity addOnIdentity) {
        this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());
        DataTransferMessage dataTransferMessage = new DataTransferMessage(vendorId, messageId, data);

        eventWaitingGateway.sendAndWaitForEvent(new IncomingDataTransferCommand(chargingStationId, dataTransferMessage, identityContext), future, (long) 10000);
    }

    public void heartbeat(ChargingStationId chargingStationId, AddOnIdentity addOnIdentity) {
        this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new HeartbeatCommand(chargingStationId, identityContext));
    }

    public void meterValues(ChargingStationId chargingStationId, TransactionId transactionId, EvseId evseId, List<MeterValue> meterValues, AddOnIdentity addOnIdentity) {
        this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new ProcessMeterValueCommand(chargingStationId, transactionId, evseId, meterValues, identityContext));
    }

    public void diagnosticsFileNameReceived(ChargingStationId chargingStationId, String diagnosticsFileName, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new DiagnosticsFileNameReceivedCommand(chargingStationId, diagnosticsFileName, identityContext), correlationToken);
    }

    public void authorizationListVersionReceived(ChargingStationId chargingStationId, int currentVersion, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new AuthorizationListVersionReceivedCommand(chargingStationId, currentVersion, identityContext), correlationToken);
    }

    public void authorize(ChargingStationId chargingStationId, String idTag, FutureEventCallback future, AddOnIdentity addOnIdentity) {
        this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        eventWaitingGateway.sendAndWaitForEvent(new AuthorizeCommand(chargingStationId, new TextualToken(idTag), identityContext), future, authorizationTimeoutInMillis);
    }

    public void receiveConfigurationItems(ChargingStationId chargingStationId, Set<ConfigurationItem> configurationItems, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        ReceiveConfigurationItemsCommand command = new ReceiveConfigurationItemsCommand(chargingStationId, configurationItems, identityContext);

        commandGateway.send(command);
    }

    public void diagnosticsUploadStatusUpdate(ChargingStationId chargingStationId, boolean diagnosticsUploaded, AddOnIdentity addOnIdentity) {
        this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        UpdateDiagnosticsUploadStatusCommand command = new UpdateDiagnosticsUploadStatusCommand(chargingStationId, diagnosticsUploaded, identityContext);
        commandGateway.send(command);
    }

    public void firmwareStatusUpdate(ChargingStationId chargingStationId, FirmwareStatus firmwareStatus, AddOnIdentity addOnIdentity) {
        this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        UpdateFirmwareStatusCommand command = new UpdateFirmwareStatusCommand(chargingStationId, firmwareStatus, identityContext);
        commandGateway.send(command);
    }

    public void statusNotification(ChargingStationId chargingStationId, EvseId evseId, StatusNotification statusNotification, AddOnIdentity addOnIdentity) {
        this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        StatusNotificationCommand command;

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        if (evseId.getNumberedId() == CHARGING_STATION_EVSE_ID) {
            command = new ChargingStationStatusNotificationCommand(chargingStationId, statusNotification, identityContext);
        } else {
            ChargingStationComponent component = ChargingStationComponent.EVSE;
            command = new ComponentStatusNotificationCommand(chargingStationId, component, evseId, statusNotification, identityContext);
        }

        commandGateway.send(command);
    }

    /**
     * Initiates a StartTransactionCommand without authorizing the identification. Normally this method is called by the
     * futureEventCallback of {@link #startTransaction}.
     *
     * @param chargingStationId charging station identifier.
     * @param transactionId     transaction identifier.
     * @param startTransactionInfo information about the started transaction.
     * @param addOnIdentity     add on identifier.
     */
    public void startTransactionNoAuthorize(ChargingStationId chargingStationId, TransactionId transactionId,
                                            StartTransactionInfo startTransactionInfo, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        StartTransactionCommand command = new StartTransactionCommand(chargingStationId, transactionId, startTransactionInfo, identityContext);
        commandGateway.send(command);
    }

    /**
     * Generates a transaction identifier and starts a transaction by dispatching a StartTransactionCommand.
     *
     * @param chargingStationId   identifier of the charging station.
     * @param evseId              evse identifier on which the transaction is started.
     * @param idTag               the identification which started the transaction.
     * @param futureEventCallback will be called once the authorize result event occurs.
     * @param addOnIdentity       identity of the add on that calls this method.
     */
    public void startTransaction(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken idTag, FutureEventCallback futureEventCallback,
                                 AddOnIdentity addOnIdentity) {
        ChargingStation chargingStation = this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        if (evseId.getNumberedId() > chargingStation.getNumberOfEvses()) {
            throw new IllegalStateException("Cannot start transaction on a unknown evse.");
        }

        // authorize the token, the future contains the call to start the transaction
        authorize(chargingStationId, idTag.getToken(), futureEventCallback, addOnIdentity);
    }

    public void stopTransaction(ChargingStationId chargingStationId, NumberedTransactionId transactionId, IdentifyingToken idTag, int meterValueStop, Date timeStamp,
                                List<MeterValue> meterValues, AddOnIdentity addOnIdentity) {
        this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        StopTransactionCommand command = new StopTransactionCommand(chargingStationId, transactionId, idTag, meterValueStop, timeStamp, identityContext);
        commandGateway.send(command);

        if (meterValues != null && !meterValues.isEmpty()) {
            Transaction transaction = transactionRepository.findTransactionById((long) transactionId.getNumber());
            commandGateway.send(new ProcessMeterValueCommand(chargingStationId, transactionId, transaction.getEvseId(), meterValues, identityContext));
        }
    }

    public void informRequestStartTransactionAccepted(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken identifyingToken, IdentityContext identityContext, CorrelationToken correlationToken) {
        CommandMessage commandMessage = asCommandMessage(new RequestStartTransactionAcceptedCommand(chargingStationId, evseId, identifyingToken, identityContext));

        if (correlationToken != null) {
            commandMessage = commandMessage.andMetaData(Collections.singletonMap(CorrelationToken.KEY, correlationToken));
        }

        commandGateway.send(commandMessage);
    }

    public void informRequestStartTransactionRejected(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken identifyingToken, IdentityContext identityContext, CorrelationToken correlationToken) {
        CommandMessage commandMessage = asCommandMessage(new RequestStartTransactionRejectedCommand(chargingStationId, evseId, identifyingToken, identityContext));

        if (correlationToken != null) {
            commandMessage = commandMessage.andMetaData(Collections.singletonMap(CorrelationToken.KEY, correlationToken));
        }

        commandGateway.send(commandMessage);
    }

    public void informRequestStopTransactionAccepted(ChargingStationId chargingStationId, TransactionId transactionId, IdentityContext identityContext, CorrelationToken correlationToken) {
        CommandMessage commandMessage = asCommandMessage(new RequestStopTransactionAcceptedCommand(chargingStationId, transactionId, identityContext));

        if (correlationToken != null) {
            commandMessage = commandMessage.andMetaData(Collections.singletonMap(CorrelationToken.KEY, correlationToken));
        }

        commandGateway.send(commandMessage);
    }

    public void informRequestStopTransactionRejected(ChargingStationId chargingStationId, TransactionId transactionId, IdentityContext identityContext, CorrelationToken correlationToken) {
        CommandMessage commandMessage = asCommandMessage(new RequestStopTransactionRejectedCommand(chargingStationId, transactionId, identityContext));

        if (correlationToken != null) {
            commandMessage = commandMessage.andMetaData(Collections.singletonMap(CorrelationToken.KEY, correlationToken));
        }

        commandGateway.send(commandMessage);
    }

    public void informReserved(ChargingStationId chargingStationId, ReservationId reservationId, EvseId evseId, Date expiryDate, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new ReserveNowCommand(chargingStationId, reservationId, evseId, expiryDate, identityContext), correlationToken);
    }

    public void informReservationFaulted(ChargingStationId chargingStationId, EvseId evseId, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());
        commandGateway.send(new ReservationFaultedCommand(chargingStationId, evseId, identityContext), correlationToken);
    }
    
    public void informReservationRejected(ChargingStationId chargingStationId, EvseId evseId, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());
        commandGateway.send(new ReservationRejectedCommand(chargingStationId, evseId, identityContext), correlationToken);
    }
    
    public void informChargingStationUnavailable(ChargingStationId chargingStationId, EvseId evseId, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());
        commandGateway.send(new ChargingStationUnavailableCommand(chargingStationId, evseId, identityContext), correlationToken);
    }
    public void informChargingStationOccupied(ChargingStationId chargingStationId, EvseId evseId, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());
        commandGateway.send(new ChargingStationOccupiedCommand(chargingStationId, evseId, identityContext), correlationToken);
    }
    
    public void informReservationCancelled(ChargingStationId chargingStationId, ReservationId reservationId, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new CancelReservationCommand(chargingStationId, reservationId, identityContext), correlationToken);
    }

    public void informCacheCleared(ChargingStationId chargingStationId, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new ClearCacheCommand(chargingStationId, identityContext), correlationToken);
    }

    public void changeComponentAvailabilityToOperative(ChargingStationId chargingStationId, ComponentId componentId, ChargingStationComponent component, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new ChangeComponentAvailabilityToOperativeCommand(chargingStationId, componentId, component, identityContext), correlationToken);
    }

    public void changeComponentAvailabilityToInoperative(ChargingStationId chargingStationId, ComponentId componentId, ChargingStationComponent component, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new ChangeComponentAvailabilityToInoperativeCommand(chargingStationId, componentId, component, identityContext), correlationToken);
    }

    public void changeChargingStationAvailabilityToOperative(ChargingStationId chargingStationId, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new ChangeChargingStationAvailabilityToOperativeCommand(chargingStationId, identityContext), correlationToken);
    }

    public void changeChargingStationAvailabilityToInoperative(ChargingStationId chargingStationId, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new ChangeChargingStationAvailabilityToInoperativeCommand(chargingStationId, identityContext), correlationToken);
    }

    /**
     * Change the configuration in the charging station. It has already happened on the physical charging station, but
     * the Domain has not been updated yet.
     *
     * @param chargingStationId the charging station id.
     * @param configurationItem the configuration item which has changed.
     * @param correlationToken  the token to correlate commands and events that belong together.
     * @param addOnIdentity     the identity of the add-on.
     */
    public void changeConfiguration(ChargingStationId chargingStationId, ConfigurationItem configurationItem, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new ChangeConfigurationItemCommand(chargingStationId, configurationItem, identityContext), correlationToken);
    }

    public void informDataTransferResponse(ChargingStationId chargingStationId, String data, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new DataTransferResponseCommand(chargingStationId, data, identityContext), correlationToken);
    }

    public void informUnlockEvse(ChargingStationId chargingStationId, EvseId evseId, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new UnlockEvseCommand(chargingStationId, evseId, identityContext), correlationToken);
    }

    public void authorizationListChange(ChargingStationId chargingStationId, int version, AuthorizationListUpdateType updateType, Set<IdentifyingToken> identifyingTokens, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());
        commandGateway.send(new ChangeAuthorizationListCommand(chargingStationId, version, updateType, identifyingTokens, identityContext), correlationToken);
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void setEventWaitingGateway(EventWaitingGateway eventWaitingGateway) {
        this.eventWaitingGateway = eventWaitingGateway;
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void setReservationIdentifierRepository(ReservationIdentifierRepository reservationIdentifierRepository) {
        this.reservationIdentifierRepository = reservationIdentifierRepository;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public void setAuthorizationTimeoutInMillis(long authorizationTimeoutInMillis) {
        this.authorizationTimeoutInMillis = authorizationTimeoutInMillis;
    }

    public void setUserIdentitiesWithAllPermissions(Set<UserIdentity> userIdentitiesWithAllPermissions) {
        this.userIdentitiesWithAllPermissions = userIdentitiesWithAllPermissions;
    }

    public String retrieveChargingStationAddress(ChargingStationId id) {
        ChargingStation chargingStation = chargingStationRepository.findOne(id.getId());

        return chargingStation != null ? chargingStation.getIpAddress() : "";
    }

    /**
     * Generates a reservation identifier based on the charging station, the module (OCPP) and a auto-incremented number.
     *
     * @param chargingStationId  charging station identifier to use when generating a reservation identifier.
     * @param protocolIdentifier identifier of the protocol, used when generating a reservation identifier.
     * @return reservation identifier based on the charging station, module and auto-incremented number.
     */
    public NumberedReservationId generateReservationIdentifier(ChargingStationId chargingStationId, String protocolIdentifier) {
        ReservationIdentifier reservationIdentifier = new ReservationIdentifier();

        reservationIdentifierRepository.insert(reservationIdentifier);

        /* TODO JPA's identity generator creates longs, while OCPP and Motown supports ints. Where should we translate
         * between these and how should we handle error cases? - Mark van den Bergh, Januari 7th 2013
         */
        Long identifier = (Long) entityManagerFactory.getPersistenceUnitUtil().getIdentifier(reservationIdentifier);
        return new NumberedReservationId(chargingStationId, protocolIdentifier, identifier.intValue());
    }

    /**
     * Creates a transaction identifier. The EVSE identifier is stored for later usage.
     *
     * @param evseId evse identifier that's stored in the transaction
     * @return transaction
     */
    public Transaction createTransaction(EvseId evseId) {
        Transaction transaction = new Transaction();
        transaction.setEvseId(evseId);

        transactionRepository.insert(transaction);

        return transaction;
    }

    /**
     * Checks if the charging station exists in the repository and if it has been registered and configured. If not a
     * IllegalStateException will be thrown.
     *
     * @param chargingStationId      charging station identifier.
     * @return ChargingStation       if the charging station exists and is registered and configured.
     * @throws IllegalStateException if the charging station does not exist in the repository, or it has not been
     *                               registered and configured.
     */
    private ChargingStation checkChargingStationExistsAndIsRegisteredAndConfigured(ChargingStationId chargingStationId) {
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId.getId());

        if (chargingStation == null) {
            throw new IllegalStateException("Unknown charging station.");
        }

        if (!chargingStation.isRegisteredAndConfigured()) {
            throw new IllegalStateException("Charging station has not been registered/configured.");
        }

        return chargingStation;
    }
}
