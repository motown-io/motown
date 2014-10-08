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
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.NullUserIdentity;
import io.motown.domain.api.security.UserIdentity;
import io.motown.domain.utils.axon.EventWaitingGateway;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.entities.ReservationIdentifier;
import io.motown.ocpp.viewmodel.persistence.entities.Transaction;
import io.motown.ocpp.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.ocpp.viewmodel.persistence.repositories.ReservationIdentifierRepository;
import io.motown.ocpp.viewmodel.persistence.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import java.util.*;

public class DomainService {

    public static final int CHARGING_STATION_EVSE_ID = 0;
    public static final String ERROR_CODE_KEY = "errorCode";
    public static final String INFO_KEY = "info";
    public static final String VENDOR_ID_KEY = "vendorId";
    public static final String VENDOR_ERROR_CODE_KEY = "vendorErrorCode";
    public static final String VENDOR_KEY = "vendor";
    public static final String MODEL_KEY = "model";
    public static final String ADDRESS_KEY = "address";
    public static final String CHARGING_STATION_SERIALNUMBER_KEY = "chargingStationSerialNumber";
    public static final String CHARGE_BOX_SERIALNUMBER_KEY = "chargeBoxSerialNumber";
    public static final String FIRMWARE_VERSION_KEY = "firmwareVersion";
    public static final String ICCID_KEY = "iccid";
    public static final String IMSI_KEY = "imsi";
    public static final String METER_TYPE_KEY = "meterType";
    public static final String METER_SERIALNUMBER_KEY = "meterSerialNumber";
    public static final String RESERVATION_ID_KEY = "reservationId";

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

    /**
     * Adds the attribute to the map using the key and value if the value is not null.
     *
     * @param attributes map of keys and values.
     * @param key        key of the attribute.
     * @param value      value of the attribute.
     */
    public static void addAttributeIfNotNull(Map<String, String> attributes, String key, String value) {
        if (value != null) {
            attributes.put(key, value);
        }
    }

    public BootChargingStationResult bootChargingStation(ChargingStationId chargingStationId, String chargingStationAddress, String vendor, String model,
                                                         String protocol, String chargingStationSerialNumber, String chargeBoxSerialNumber, String firmwareVersion, String iccid,
                                                         String imsi, String meterType, String meterSerialNumber, AddOnIdentity addOnIdentity) {
        // Check if we already know the charging station, or have to create one
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId.getId());

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        if (chargingStation == null) {
            LOG.debug("Not a known charging station on boot notification, we send a CreateChargingStationCommand.");

            commandGateway.send(new CreateChargingStationCommand(chargingStationId, userIdentitiesWithAllPermissions, identityContext), new CreateChargingStationCommandCallback(
                    chargingStationId, chargingStationAddress, vendor, model, protocol, chargingStationSerialNumber, chargeBoxSerialNumber, firmwareVersion, iccid,
                    imsi, meterType, meterSerialNumber, addOnIdentity, chargingStationRepository, this));

            // we didn't know the charging station when this bootNotification occurred so we reject it.
            return new BootChargingStationResult(false, heartbeatInterval, new Date());
        }

        // Keep track of the address on which we can reach the charging station
        chargingStation.setIpAddress(chargingStationAddress);
        chargingStation = chargingStationRepository.createOrUpdate(chargingStation);

        Map<String, String> attributes = Maps.newHashMap();

        addAttributeIfNotNull(attributes, ADDRESS_KEY, chargingStationAddress);
        addAttributeIfNotNull(attributes, VENDOR_KEY, vendor);
        addAttributeIfNotNull(attributes, MODEL_KEY, model);
        addAttributeIfNotNull(attributes, CHARGING_STATION_SERIALNUMBER_KEY, chargingStationSerialNumber);
        addAttributeIfNotNull(attributes, CHARGE_BOX_SERIALNUMBER_KEY, chargeBoxSerialNumber);
        addAttributeIfNotNull(attributes, FIRMWARE_VERSION_KEY, firmwareVersion);
        addAttributeIfNotNull(attributes, ICCID_KEY, iccid);
        addAttributeIfNotNull(attributes, IMSI_KEY, imsi);
        addAttributeIfNotNull(attributes, METER_TYPE_KEY, meterType);
        addAttributeIfNotNull(attributes, METER_SERIALNUMBER_KEY, meterSerialNumber);

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

    public void statusNotification(ChargingStationId chargingStationId, StatusNotification statusNotification, AddOnIdentity addOnIdentity) {
        this.checkChargingStationExistsAndIsRegisteredAndConfigured(chargingStationId);

        StatusNotificationCommand command;

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        if (statusNotification.getEvseId().getNumberedId() == CHARGING_STATION_EVSE_ID) {
            command = new ChargingStationStatusNotificationCommand(chargingStationId, statusNotification.getStatus(),
                    statusNotification.getTimeStamp(), statusNotification.getAttributes(), identityContext);
        } else {
            ChargingStationComponent component = ChargingStationComponent.EVSE;
            command = new ComponentStatusNotificationCommand(chargingStationId, component, statusNotification.getEvseId(),
                    statusNotification.getStatus(), statusNotification.getTimeStamp(), statusNotification.getAttributes(), identityContext);
        }

        commandGateway.send(command);
    }

    /**
     * Initiates a StartTransactionCommand without authorizing the identification. Normally this method is called by the
     * futureEventCallback of {@link #startTransaction}.
     *
     * @param transactionId     transaction identifier.
     * @param evseId            evse identifier.
     * @param chargingStationId charging station identifier.
     * @param reservationId     reservation identifier.
     * @param addOnIdentity     add on identifier.
     * @param idTag             identification that started the transaction.
     * @param meterStart        meter value in Wh for the evse when the transaction started.
     * @param timestamp         the time at which the transaction started.
     */
    public void startTransactionNoAuthorize(TransactionId transactionId, EvseId evseId, ChargingStationId chargingStationId, ReservationId reservationId,
                                            AddOnIdentity addOnIdentity, IdentifyingToken idTag, int meterStart, Date timestamp) {
        Map<String, String> attributes = Maps.newHashMap();
        if (reservationId != null) {
            attributes.put(RESERVATION_ID_KEY, reservationId.getId());
        }

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        StartTransactionCommand command = new StartTransactionCommand(chargingStationId, transactionId, evseId, idTag,
                meterStart, timestamp, attributes, identityContext);
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

    public void informReserved(ChargingStationId chargingStationId, ReservationId reservationId, EvseId evseId, Date expiryDate, CorrelationToken correlationToken, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new ReserveNowCommand(chargingStationId, reservationId, evseId, expiryDate, identityContext), correlationToken);
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
