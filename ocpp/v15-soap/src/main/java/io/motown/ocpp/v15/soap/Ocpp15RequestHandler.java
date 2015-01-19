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
package io.motown.ocpp.v15.soap;

import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import io.motown.ocpp.viewmodel.OcppRequestHandler;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import org.axonframework.common.annotation.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class Ocpp15RequestHandler implements OcppRequestHandler {

    public static final String PROTOCOL_IDENTIFIER = "OCPPS15";
    public static final String ADD_ON_TYPE = "OCPPS15";
    private static final Logger LOG = LoggerFactory.getLogger(Ocpp15RequestHandler.class);
    private DomainService domainService;
    private ChargingStationOcpp15Client chargingStationOcpp15Client;
    private AddOnIdentity addOnIdentity;

    @Override
    public void handle(ConfigurationItemsRequestedEvent event) {
        LOG.info("Handling ConfigurationItemsRequestedEvent");
        Map<String, String> configurationItemMap = chargingStationOcpp15Client.getConfiguration(event.getChargingStationId(), event.getKeys());

        domainService.receiveConfigurationItems(event.getChargingStationId(), toConfigurationItems(configurationItemMap), addOnIdentity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(StopTransactionRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("StopTransactionRequestedEvent");

        if (event.getTransactionId() instanceof NumberedTransactionId) {
            NumberedTransactionId transactionId = (NumberedTransactionId) event.getTransactionId();
            chargingStationOcpp15Client.stopTransaction(event.getChargingStationId(), transactionId.getNumber());
        } else {
            LOG.warn("StopTransactionRequestedEvent does not contain a NumberedTransactionId. Event: {}", event);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(SoftResetChargingStationRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("SoftResetChargingStationRequestedEvent");
        chargingStationOcpp15Client.softReset(event.getChargingStationId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(HardResetChargingStationRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("HardResetChargingStationRequestedEvent");
        chargingStationOcpp15Client.hardReset(event.getChargingStationId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(StartTransactionRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("StartTransactionRequestedEvent");
        chargingStationOcpp15Client.startTransaction(event.getChargingStationId(), event.getIdentifyingToken(), event.getEvseId());
    }

    @Override
    public void handle(UnlockEvseRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("UnlockEvseRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.unlockConnector(event.getChargingStationId(), event.getEvseId());

        switch (requestResult) {
            case SUCCESS:
                domainService.informUnlockEvse(event.getChargingStationId(), event.getEvseId(), correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to unlock evse {} on chargingstation {}", event.getEvseId(), event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unkown unlock evse response status: '%s'", requestResult));
        }
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.5 ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
        EvseId chargingStationEvseId = new EvseId(0);
        RequestResult requestResult = chargingStationOcpp15Client.changeAvailabilityToInoperative(event.getChargingStationId(), chargingStationEvseId);

        switch (requestResult) {
            case SUCCESS:
                domainService.changeChargingStationAvailabilityToInoperative(event.getChargingStationId(), correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to set availability of chargingstation {} to inoperative", event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unkown status for change availability to inoperative: '%s'", requestResult));
        }
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken) {
        LOG.info("OCPP 1.5 ChangeChargingStationAvailabilityToOperativeRequestedEvent");
        EvseId chargingStationEvseId = new EvseId(0);
        RequestResult requestResult = chargingStationOcpp15Client.changeAvailabilityToOperative(event.getChargingStationId(), chargingStationEvseId);

        switch (requestResult) {
            case SUCCESS:
                domainService.changeChargingStationAvailabilityToOperative(event.getChargingStationId(), correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to set availability of chargingstation {} to operative", event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unkown status for change availability to operative: '%s'", requestResult));
        }
    }

    @Override
    public void handle(ChangeComponentAvailabilityToInoperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken) {
        LOG.info("OCPP 1.5 ChangeComponentAvailabilityToInoperativeRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.changeAvailabilityToInoperative(event.getChargingStationId(), (EvseId) event.getComponentId());

        switch (requestResult) {
            case SUCCESS:
                domainService.changeComponentAvailabilityToInoperative(event.getChargingStationId(), event.getComponentId(), ChargingStationComponent.EVSE, correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to set availability of evse {} on chargingstation {} to inoperative", event.getComponentId().getId(), event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unkown status for change component availability to inoperative: '%s'", requestResult));
        }
    }

    @Override
    public void handle(ChangeComponentAvailabilityToOperativeRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("OCPP 1.5 ChangeComponentAvailabilityToOperativeRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.changeAvailabilityToOperative(event.getChargingStationId(), (EvseId) event.getComponentId());

        switch (requestResult) {
            case SUCCESS:
                domainService.changeComponentAvailabilityToOperative(event.getChargingStationId(), event.getComponentId(), ChargingStationComponent.EVSE, correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to set availability of evse {} on chargingstation {} to operative", event.getComponentId().getId(), event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unkown status for change component availability to operative: '%s'", requestResult));
        }
    }

    @Override
    public void handle(DataTransferRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("DataTransferRequestedEvent");
        DataTransferRequestResult result = chargingStationOcpp15Client.dataTransfer(event.getChargingStationId(), event.getDataTransferMessage());

        if (result.isSuccessful()) {
            String responseData = result.getData();
            if (responseData != null) {
                domainService.informDataTransferResponse(event.getChargingStationId(), responseData, correlationToken, addOnIdentity);
            }
        } else {
            LOG.info("Failed to request datatransfer to chargingstation {}", event.getChargingStationId().getId());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(ChangeConfigurationItemRequestedEvent event, CorrelationToken correlationToken) {
        boolean hasConfigurationChanged = chargingStationOcpp15Client.changeConfiguration(event.getChargingStationId(), event.getConfigurationItem());

        if (hasConfigurationChanged) {
            domainService.changeConfiguration(event.getChargingStationId(), event.getConfigurationItem(), correlationToken, addOnIdentity);
        }
    }

    @Override
    public void handle(DiagnosticsRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("DiagnosticsRequestedEvent");
        String diagnosticsFilename = chargingStationOcpp15Client.getDiagnostics(event.getChargingStationId(), event.getDiagnosticsUploadSettings());

        domainService.diagnosticsFileNameReceived(event.getChargingStationId(), diagnosticsFilename, correlationToken, addOnIdentity);
    }

    @Override
    public void handle(ClearCacheRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("ClearCacheRequestedEvent");
        boolean result = chargingStationOcpp15Client.clearCache(event.getChargingStationId());

        if (result) {
            domainService.informCacheCleared(event.getChargingStationId(), correlationToken, addOnIdentity);
        } else {
            LOG.info("Unable to clear cache for [{}]", event.getChargingStationId());
        }
    }

    @Override
    public void handle(FirmwareUpdateRequestedEvent event) {
        LOG.info("FirmwareUpdateRequestedEvent");
        Map<String, String> attributes = event.getAttributes();

        String attrNumRetries = null;
        String attrRetryInterval = null;
        if (attributes != null) {
            attrNumRetries = attributes.get(FirmwareUpdateAttributeKey.NUM_RETRIES);
            attrRetryInterval = attributes.get(FirmwareUpdateAttributeKey.RETRY_INTERVAL);
        }
        Integer numRetries = (attrNumRetries != null && !"".equals(attrNumRetries)) ? Integer.parseInt(attrNumRetries) : null;
        Integer retryInterval = (attrRetryInterval != null && !"".equals(attrRetryInterval)) ? Integer.parseInt(attrRetryInterval) : null;

        chargingStationOcpp15Client.updateFirmware(event.getChargingStationId(), event.getUpdateLocation(), event.getRetrieveDate(), numRetries, retryInterval);
    }

    @Override
    public void handle(AuthorizationListVersionRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("AuthorizationListVersionRequestedEvent");

        int currentVersion = chargingStationOcpp15Client.getAuthorizationListVersion(event.getChargingStationId());

        domainService.authorizationListVersionReceived(event.getChargingStationId(), currentVersion, correlationToken, addOnIdentity);
    }

    @Override
    public void handle(SendAuthorizationListRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("SendAuthorizationListRequestedEvent");

        RequestResult requestResult = chargingStationOcpp15Client.sendAuthorizationList(event.getChargingStationId(), event.getAuthorizationListHash(), event.getAuthorizationListVersion(), event.getAuthorizationList(), event.getUpdateType());

        switch (requestResult) {
            case SUCCESS:
                domainService.authorizationListChange(event.getChargingStationId(), event.getAuthorizationListVersion(), event.getUpdateType(), event.getAuthorizationList(), correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to send authorization list to charging station {}", event.getChargingStationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unkown send authorization list response status: '%s'", requestResult));
        }
    }

    @Override
    public void handle(ReserveNowRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("ReserveNowRequestedEvent");

        NumberedReservationId reservationIdentifier = domainService.generateReservationIdentifier(event.getChargingStationId(), event.getProtocol());

        ReservationStatus reservationStatus = chargingStationOcpp15Client.reserveNow(event.getChargingStationId(), event.getEvseId(), event.getIdentifyingToken(), event.getExpiryDate(), event.getParentIdentifyingToken(), reservationIdentifier.getNumber());

        switch (reservationStatus) {
            case ACCEPTED:
                domainService.informReserved(event.getChargingStationId(), reservationIdentifier, event.getEvseId(), event.getExpiryDate(), correlationToken, addOnIdentity);
                break;
            case FAULTED:
            case OCCUPIED:
            case UNAVAILABLE:
            case REJECTED:
                String reservationStatusMessage = reservationStatus.name();
                LOG.info("Failed to reserve evse {} on charging station {}: {}", event.getEvseId().getId(), event.getChargingStationId().getId(), reservationStatusMessage);
                break;
            default:
                throw new AssertionError(String.format("Unkown reserve now response response status: '%s'", reservationStatus));
        }
    }

    @Override
    public void handle(CancelReservationRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("CancelReservationRequestedEvent");

        RequestResult requestResult = chargingStationOcpp15Client.cancelReservation(event.getChargingStationId(), ((NumberedReservationId) event.getReservationId()).getNumber());

        switch (requestResult) {
            case SUCCESS:
                domainService.informReservationCancelled(event.getChargingStationId(), event.getReservationId(), correlationToken, addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to cancel reservation with reservationId {}", event.getReservationId().getId());
                break;
            default:
                throw new AssertionError(String.format("Unkown cancel reservation response status: '%s'", requestResult));
        }
    }

    public void setChargingStationOcpp15Client(ChargingStationOcpp15Client chargingStationOcpp15Client) {
        this.chargingStationOcpp15Client = chargingStationOcpp15Client;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    /**
     * Sets the add-on id. The add-on is hardcoded, the add-on id should be different for every instance (in a distributed configuration)
     * to be able to differentiate between add-on instances.
     *
     * @param id add-on id.
     */
    public void setAddOnId(String id) {
        addOnIdentity = new TypeBasedAddOnIdentity(ADD_ON_TYPE, id);
    }

    /**
     * Converts a {@code Map} of {@code String}s and {@code String}s to a {@code Set} of {@code ConfigurationItem}s.
     *
     * @param configurationItemMap a {@code Map} of {@code String}s and {@code String}s
     * @return a {@code Set} of {@code ConfigurationItem}s.
     */
    private Set<ConfigurationItem> toConfigurationItems(Map<String, String> configurationItemMap) {
        ImmutableSet.Builder<ConfigurationItem> configurationItemsBuilder = ImmutableSet.builder();

        for (Map.Entry<String, String> item : configurationItemMap.entrySet()) {
            configurationItemsBuilder.add(new ConfigurationItem(item.getKey(), item.getValue()));
        }

        return configurationItemsBuilder.build();
    }
}
