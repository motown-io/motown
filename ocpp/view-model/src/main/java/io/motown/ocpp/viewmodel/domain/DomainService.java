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
import io.motown.ocpp.viewmodel.persistence.entities.ReservationIdentifier;
import io.motown.ocpp.viewmodel.persistence.entities.Transaction;
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ocpp.viewmodel.persistence.repostories.ReservationIdentifierRepository;
import io.motown.ocpp.viewmodel.persistence.repostories.TransactionRepository;
import org.axonframework.commandhandling.gateway.EventWaitingGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DomainService {

    private static final Logger LOG = LoggerFactory.getLogger(DomainService.class);
    public static final int CHARGING_STATION_EVSE_ID = 0;
    public static final String ERROR_CODE_KEY = "errorCode";
    public static final String INFO_KEY = "info";
    public static final String VENDOR_ID_KEY = "vendorId";
    public static final String VENDOR_ERROR_CODE_KEY = "vendorErrorCode";

    public static final String VENDOR_KEY = "vendor";
    public static final String MODEL_KEY = "model";
    public static final String ADDRESS_KEY = "address";
    public static final String CHARGING_STATION_SERIALNUMBER_KEY = "chargingStationSerialNumber";
    public static final String FIRMWARE_VERSION_KEY = "firmwareVersion";
    public static final String ICCID_KEY = "iccid";
    public static final String IMSI_KEY = "imsi";
    public static final String METER_TYPE_KEY = "meterType";
    public static final String METER_SERIALNUMBER_KEY = "meterSerialNumber";

    public static final String RESERVATION_ID_KEY = "reservationId";

    @Resource(name = "domainCommandGateway")
    private DomainCommandGateway commandGateway;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReservationIdentifierRepository reservationIdentifierRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Value("${io.motown.ocpp.viewmodel.heartbeat.interval}")
    private int heartbeatInterval;

    @Autowired
    private EventWaitingGateway eventWaitingGateway;

    public BootChargingStationResult bootChargingStation(ChargingStationId chargingStationId, String chargingStationAddress, String vendor, String model,
                                                         String protocol, String chargingStationSerialNumber, String firmwareVersion, String iccid,
                                                         String imsi, String meterType, String meterSerialNumber) {
        // In case there is no charging station address specified there is no point in continuing, since we will not be able to reach the charging station later on
        if (chargingStationAddress == null || chargingStationAddress.isEmpty()) {
            LOG.error("Rejecting bootnotification, no charging station address has been specified.");
            return new BootChargingStationResult(false, heartbeatInterval, new Date());
        }

        // Check if we already know the charging station, or have to create one
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId.getId());

        if (chargingStation == null) {
            LOG.debug("Not a known charging station on boot notification, we send a CreateChargingStationCommand.");

            commandGateway.send(new CreateChargingStationCommand(chargingStationId), new CreateChargingStationCommandCallback(
                    chargingStationId, chargingStationAddress, vendor, model, protocol, chargingStationSerialNumber, firmwareVersion, iccid,
                    imsi, meterType, meterSerialNumber, chargingStationRepository, this));

            // we didn't know the charging station when this bootNotification occurred so we reject it.
            return new BootChargingStationResult(false, heartbeatInterval, new Date());
        }

        // Keep track of the address on which we can reach the charging station
        chargingStation.setIpAddress(chargingStationAddress);
        chargingStationRepository.save(chargingStation);

        Map<String, String> attributes = Maps.newHashMap();
        attributes.put(ADDRESS_KEY, chargingStationAddress);
        if (vendor != null) {
            attributes.put(VENDOR_KEY, vendor);
        }
        if (model != null) {
            attributes.put(MODEL_KEY, model);
        }
        if (chargingStationSerialNumber != null) {
            attributes.put(CHARGING_STATION_SERIALNUMBER_KEY, chargingStationSerialNumber);
        }
        if (firmwareVersion != null) {
            attributes.put(FIRMWARE_VERSION_KEY, firmwareVersion);
        }
        if (iccid != null) {
            attributes.put(ICCID_KEY, iccid);
        }
        if (imsi != null) {
            attributes.put(IMSI_KEY, imsi);
        }
        if (meterType != null) {
            attributes.put(METER_TYPE_KEY, meterType);
        }
        if (meterSerialNumber != null) {
            attributes.put(METER_SERIALNUMBER_KEY, meterSerialNumber);
        }

        commandGateway.send(new BootChargingStationCommand(chargingStationId, protocol, attributes));

        return new BootChargingStationResult(chargingStation.isRegistered(), heartbeatInterval, new Date());
    }

    public void dataTransfer(ChargingStationId chargingStationId, String data, String vendorId, String messageId) {
        commandGateway.send(new IncomingDataTransferCommand(chargingStationId, vendorId, messageId, data));
    }

    public void heartbeat(ChargingStationId chargingStationId) {
        commandGateway.send(new HeartbeatCommand(chargingStationId));
    }

    public void meterValues(ChargingStationId chargingStationId, TransactionId transactionId, EvseId evseId, List<MeterValue> meterValues) {
        commandGateway.send(new ProcessMeterValueCommand(chargingStationId, transactionId, evseId, meterValues));
    }

    public void diagnosticsFileNameReceived(ChargingStationId chargingStationId, String diagnosticsFileName) {
        commandGateway.send(new DiagnosticsFileNameReceivedCommand(chargingStationId, diagnosticsFileName));
    }

    public void authorizationListVersionReceived(ChargingStationId chargingStationId, int currentVersion) {
        commandGateway.send(new AuthorizationListVersionReceivedCommand(chargingStationId, currentVersion));
    }

    public void authorize(ChargingStationId chargingStationId, String idTag, FutureEventCallback future) {
        eventWaitingGateway.sendAndWaitForEvent(new AuthorizeCommand(chargingStationId, new TextualToken(idTag)), future);
    }

    public void configureChargingStation(ChargingStationId chargingStationId, Map<String, String> configurationItems) {
        ConfigureChargingStationCommand command = new ConfigureChargingStationCommand(chargingStationId, configurationItems);
        commandGateway.send(command);
    }

    public void diagnosticsUploadStatusUpdate(ChargingStationId chargingStationId, boolean diagnosticsUploaded) {
        UpdateDiagnosticsUploadStatusCommand command = new UpdateDiagnosticsUploadStatusCommand(chargingStationId, diagnosticsUploaded);
        commandGateway.send(command);
    }

    public void firmwareStatusUpdate(ChargingStationId chargingStationId, FirmwareStatus firmwareStatus) {
        UpdateFirmwareStatusCommand command = new UpdateFirmwareStatusCommand(chargingStationId, firmwareStatus);
        commandGateway.send(command);
    }

    public void statusNotification(ChargingStationId chargingStationId, EvseId evseId, String errorCode, ComponentStatus status, String info, Date timeStamp, String vendorId, String vendorErrorCode) {
        //TODO: The attributes map can contain protocol specific key values, how to know what keys to expect on receiving end - Ingo Pak, 09 Jan 2014
        Map<String, String> attributes = new HashMap<>();
        if (errorCode != null) {
            attributes.put(ERROR_CODE_KEY, errorCode);
        }
        if (info != null) {
            attributes.put(INFO_KEY, info);
        }
        if (vendorId != null) {
            attributes.put(VENDOR_ID_KEY, vendorId);
        }
        if (vendorErrorCode != null) {
            attributes.put(VENDOR_ERROR_CODE_KEY, vendorErrorCode);
        }

        StatusNotificationCommand command;

        if (evseId.getNumberedId() == CHARGING_STATION_EVSE_ID) {
            command = new ChargingStationStatusNotificationCommand(chargingStationId, status, timeStamp, attributes);
        } else {
            ChargingStationComponent component = ChargingStationComponent.CONNECTOR;
            command = new ComponentStatusNotificationCommand(chargingStationId, component, evseId, status, timeStamp, attributes);
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
    public int startTransaction(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken idTag, int meterStart, Date timestamp, ReservationId reservationId, String protocolIdentifier) {
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
            //TODO: Fix this magic key value? - Mark van den Bergh, 10 Jan 2014
            attributes.put(RESERVATION_ID_KEY, reservationId.getId());
        }

        StartTransactionCommand command = new StartTransactionCommand(chargingStationId, transactionId, evseId, idTag, meterStart, timestamp, attributes);
        commandGateway.send(command);

        return transactionId.getNumber();
    }

    public void stopTransaction(ChargingStationId chargingStationId, NumberedTransactionId transactionId, IdentifyingToken idTag, int meterValueStop, Date timeStamp, List<MeterValue> meterValues) {
        StopTransactionCommand command = new StopTransactionCommand(chargingStationId, transactionId, idTag, meterValueStop, timeStamp);
        commandGateway.send(command);

        if (meterValues != null && meterValues.size() > 0) {
            Transaction transaction = transactionRepository.findTransactionById((long) transactionId.getNumber());
            commandGateway.send(new ProcessMeterValueCommand(chargingStationId, transactionId, transaction.getEvseId(), meterValues));
        }
    }

    public void reservationStatusChanged(ChargingStationId chargingStationId, ReservationId reservationId, ReservationStatus newStatus) {
        commandGateway.send(new ReservationStatusChangedCommand(chargingStationId, reservationId, newStatus));
    }

    public void stopTransactionStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new StopTransactionStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void softResetStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new SoftResetStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void hardResetStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new HardResetStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void startTransactionStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new StartTransactionStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void unlockEvseStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new UnlockEvseStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void changeAvailabilityToOperativeStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new ChangeAvailabilityToOperativeStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void changeAvailabilityToInoperativeStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new ChangeAvailabilityToInoperativeStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void dateTransferStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new DataTransferStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void changeConfigurationStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new ChangeConfigurationStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void clearCacheStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new ClearCacheStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void sendAuthorizationListStatusChanged(ChargingStationId chargingStationId, RequestStatus requestStatus) {
        commandGateway.send(new SendAuthorizationListStatusChangedCommand(chargingStationId, requestStatus));
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
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

        reservationIdentifierRepository.saveAndFlush(reservationIdentifier);

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
     * @param chargingStationId  charging station identifier to use when generating a transaction identifier.
     * @param protocolIdentifier identifier of the protocol, used when generating a transaction identifier.
     * @param evseId             evse identifier that's stored in the transaction
     * @return transaction
     */
    private Transaction createTransaction(EvseId evseId) {
        Transaction transaction = new Transaction();
        transaction.setEvseId(evseId);

        // flush to make sure the generated id is populated
        transactionRepository.saveAndFlush(transaction);

        return transaction;
    }

}
