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
    /**
     * Meter value context attribute key.
     */
    public static final String CONTEXT_KEY = "context";
    /**
     * Meter value format attribute key.
     */
    public static final String FORMAT_KEY = "format";
    /**
     * Meter value measurand attribute key.
     */
    public static final String MEASURAND_KEY = "measurand";
    /**
     * Meter value location attribute key.
     */
    public static final String LOCATION_KEY = "location";
    /**
     * Meter value unit attribute key.
     */
    public static final String UNIT_KEY = "unit";
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
     * The timeout in milliseconds to wait for the dataTransfer response events
     */
    private long dataTransferTimeoutInMillis = 10000;

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
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        eventWaitingGateway.sendAndWaitForEvent(new IncomingDataTransferCommand(chargingStationId, vendorId, messageId, data, identityContext), future, dataTransferTimeoutInMillis);
    }

    public void heartbeat(ChargingStationId chargingStationId, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        commandGateway.send(new HeartbeatCommand(chargingStationId, identityContext));
    }

    public void meterValues(ChargingStationId chargingStationId, TransactionId transactionId, EvseId evseId, List<MeterValue> meterValues, AddOnIdentity addOnIdentity) {
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
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        eventWaitingGateway.sendAndWaitForEvent(new AuthorizeCommand(chargingStationId, new TextualToken(idTag), identityContext), future, authorizationTimeoutInMillis);
    }

    public void receiveConfigurationItems(ChargingStationId chargingStationId, Set<ConfigurationItem> configurationItems, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        ReceiveConfigurationItemsCommand command = new ReceiveConfigurationItemsCommand(chargingStationId, configurationItems, identityContext);

        commandGateway.send(command);
    }

    public void diagnosticsUploadStatusUpdate(ChargingStationId chargingStationId, boolean diagnosticsUploaded, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        UpdateDiagnosticsUploadStatusCommand command = new UpdateDiagnosticsUploadStatusCommand(chargingStationId, diagnosticsUploaded, identityContext);
        commandGateway.send(command);
    }

    public void firmwareStatusUpdate(ChargingStationId chargingStationId, FirmwareStatus firmwareStatus, AddOnIdentity addOnIdentity) {
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        UpdateFirmwareStatusCommand command = new UpdateFirmwareStatusCommand(chargingStationId, firmwareStatus, identityContext);
        commandGateway.send(command);
    }

    public void statusNotification(ChargingStationId chargingStationId, EvseId evseId, String errorCode, ComponentStatus status,
                                   String info, Date timeStamp, String vendorId, String vendorErrorCode, AddOnIdentity addOnIdentity) {
        Map<String, String> attributes = new HashMap<>();

        addAttributeIfNotNull(attributes, ERROR_CODE_KEY, errorCode);
        addAttributeIfNotNull(attributes, INFO_KEY, info);
        addAttributeIfNotNull(attributes, VENDOR_ID_KEY, vendorId);
        addAttributeIfNotNull(attributes, VENDOR_ERROR_CODE_KEY, vendorErrorCode);

        StatusNotificationCommand command;

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        if (evseId.getNumberedId() == CHARGING_STATION_EVSE_ID) {
            command = new ChargingStationStatusNotificationCommand(chargingStationId, status, timeStamp, attributes, identityContext);
        } else {
            ChargingStationComponent component = ChargingStationComponent.EVSE;
            command = new ComponentStatusNotificationCommand(chargingStationId, component, evseId, status, timeStamp, attributes, identityContext);
        }

        commandGateway.send(command);
    }

    /**
     * Generates a transaction identifier and starts a transaction by dispatching a StartTransactionCommand.
     *
     * @param chargingStationId  identifier of the charging station
     * @param evseId             evse identifier on which the transaction is started
     * @param idTag              the identifier which started the transaction
     * @param meterStart         meter value in Wh for the evse at start of the transaction
     * @param timestamp          date and time on which the transaction started
     * @param reservationId      optional identifier of the reservation that terminates as a result of this transaction
     * @param protocolIdentifier identifier of the protocol that starts the transaction  @throws IllegalStateException when the charging station cannot be found, is not registered and configured, or the evseId is unknown for this charging station
     * @return transaction identifier
     */
    public int startTransaction(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken idTag, int meterStart,
                                Date timestamp, ReservationId reservationId, String protocolIdentifier, AddOnIdentity addOnIdentity) {
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId.getId());
        if (chargingStation == null) {
            throw new IllegalStateException("Cannot start transaction for an unknown charging station.");
        }

        if (!chargingStation.isRegisteredAndConfigured()) {
            throw new IllegalStateException("Cannot start transaction for charging station that has not been registered/configured.");
        }

        if (evseId.getNumberedId() > chargingStation.getNumberOfEvses()) {
            throw new IllegalStateException("Cannot start transaction on a unknown evse.");
        }

        Transaction transaction = createTransaction(evseId);
        NumberedTransactionId transactionId = new NumberedTransactionId(chargingStationId, protocolIdentifier, transaction.getId().intValue());

        Map<String, String> attributes = Maps.newHashMap();
        if (reservationId != null) {
            attributes.put(RESERVATION_ID_KEY, reservationId.getId());
        }

        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

        StartTransactionCommand command = new StartTransactionCommand(chargingStationId, transactionId, evseId, idTag, meterStart, timestamp, attributes, identityContext);
        commandGateway.send(command);

        return transactionId.getNumber();
    }

    public void stopTransaction(ChargingStationId chargingStationId, NumberedTransactionId transactionId, IdentifyingToken idTag, int meterValueStop, Date timeStamp,
                                List<MeterValue> meterValues, AddOnIdentity addOnIdentity) {
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
     * Creates a transaction based on the charging station and protocol identifier. The evse identifier is
     * stored for later usage.
     *
     * @param evseId evse identifier that's stored in the transaction
     * @return transaction
     */
    private Transaction createTransaction(EvseId evseId) {
        Transaction transaction = new Transaction();
        transaction.setEvseId(evseId);

        transactionRepository.insert(transaction);

        return transaction;
    }
}
