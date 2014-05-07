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
package io.motown.operatorapi.viewmodel;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import io.motown.domain.api.chargingstation.*;
import io.motown.operatorapi.viewmodel.persistence.entities.Availability;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.entities.Evse;
import io.motown.operatorapi.viewmodel.persistence.entities.LocalAuthorization;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChargingStationEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationEventListener.class);

    private static final String TIME_FORMAT = "%02d:%02d";

    private ChargingStationRepository repository;

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        LOG.debug("ChargingStationCreatedEvent creates [{}] in operator api repo", event.getChargingStationId());
        ChargingStation station = new ChargingStation(event.getChargingStationId().getId());
        repository.createOrUpdate(station);
    }

    @EventHandler
    public void handle(ChargingStationBootedEvent event) {
        LOG.debug("ChargingStationBootedEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setProtocol(event.getProtocol());
            chargingStation.setAttributes(event.getAttributes());
            repository.createOrUpdate(chargingStation);
        } else {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and mark it as booted", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(ChargingStationAcceptedEvent event) {
        LOG.debug("ChargingStationAcceptedEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setAccepted(true);
            repository.createOrUpdate(chargingStation);
        } else {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and mark it as accepted", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(ChargingStationPlacedEvent event) {
        LOG.debug("ChargingStationPlacedEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationLocation(event);
    }

    @EventHandler
    public void handle(ChargingStationMovedEvent event) {
        LOG.debug("ChargingStationMovedEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationLocation(event);
    }

    @EventHandler
    public void handle(ChargingStationLocationImprovedEvent event) {
        LOG.debug("ChargingStationLocationImprovedEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationLocation(event);
    }

    @EventHandler
    public void handle(ChargingStationOpeningTimesSetEvent event) {
        LOG.debug("ChargingStationOpeningTimesSetEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationOpeningTimes(event, true);
    }

    @EventHandler
    public void handle(ChargingStationOpeningTimesAddedEvent event) {
        LOG.debug("ChargingStationOpeningTimesAddedEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationOpeningTimes(event, false);
    }

    @EventHandler
    public void handle(ChargingStationConfiguredEvent event) {
        LOG.debug("ChargingStationConfiguredEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());
        if (chargingStation != null) {
            for (io.motown.domain.api.chargingstation.Evse coreEvse : event.getEvses()) {
                Evse evse = new Evse(coreEvse.getEvseId().getId());

                for (Connector coreConnector : coreEvse.getConnectors()) {
                    io.motown.operatorapi.viewmodel.persistence.entities.Connector connector = new io.motown.operatorapi.viewmodel.persistence.entities.Connector(
                            coreConnector.getMaxAmp(), coreConnector.getPhase(), coreConnector.getVoltage(), coreConnector.getChargingProtocol(), coreConnector.getCurrent(), coreConnector.getConnectorType()
                    );
                    evse.getConnectors().add(connector);
                }
                chargingStation.getEvses().add(evse);
            }

            chargingStation.setConfigured(true);

            repository.createOrUpdate(chargingStation);
        } else {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and configure it", event.getChargingStationId());
        }
    }

    /**
     * Handles the {@link ChargingStationMadeReservableEvent}.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(ChargingStationMadeReservableEvent event) {
        setReservable(event.getChargingStationId(), true);
    }

    /**
     * Handles the {@link ChargingStationMadeNotReservableEvent}.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(ChargingStationMadeNotReservableEvent event) {
        setReservable(event.getChargingStationId(), false);
    }

    /**
     * Handles the {@link ChargingStationStatusNotificationReceivedEvent}.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(ChargingStationStatusNotificationReceivedEvent event) {
        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setStatus(event.getStatus());
            repository.createOrUpdate(chargingStation);
        }
    }


    /**
     * Handles the {@link ComponentStatusNotificationReceivedEvent}.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(ComponentStatusNotificationReceivedEvent event) {
        if (event.getComponent() == ChargingStationComponent.EVSE) {
            ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

            if (chargingStation != null) {
                updateEvseStatus(chargingStation, event.getComponentId().getId(), event.getStatus());
                repository.createOrUpdate(chargingStation);
            }
        }
    }

    /**
     * Handles the {@link ConfigurationItemsReceivedEvent}.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(ConfigurationItemsReceivedEvent event) {
        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setConfigurationItems(toConfigurationItemMap(event.getConfigurationItems()));
            repository.createOrUpdate(chargingStation);
        }
    }

    /**
     * Handles the {@link AuthorizationListVersionReceivedEvent}.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(AuthorizationListVersionReceivedEvent event) {
        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setLocalAuthorizationListVersion(event.getVersion());
            repository.createOrUpdate(chargingStation);
        }
    }

    /**
     * Handles the {@link AuthorizationListChangedEvent}.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(AuthorizationListChangedEvent event) {
        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            Set<LocalAuthorization> updatedLocalAuthorizations = toLocalAuthorizationSet(event.getIdentifyingTokens());

            if(AuthorizationListUpdateType.FULL.equals(event.getUpdateType())) {
                chargingStation.setLocalAuthorizations(updatedLocalAuthorizations);
            } else {
                updateAuthorizationList(chargingStation, updatedLocalAuthorizations);
            }
            chargingStation.setLocalAuthorizationListVersion(event.getVersion());
            repository.createOrUpdate(chargingStation);
        }
    }

    private Set<LocalAuthorization> toLocalAuthorizationSet(Set<IdentifyingToken> identifyingTokens) {
        Set<LocalAuthorization> localAuthorizations = Sets.newHashSet();
        for (IdentifyingToken identifyingToken : identifyingTokens) {
            LocalAuthorization localAuthorization = new LocalAuthorization();
            localAuthorization.setToken(identifyingToken.getToken());
            localAuthorization.setAuthenticationStatus(identifyingToken.getAuthenticationStatus());

            localAuthorizations.add(localAuthorization);
        }
        return localAuthorizations;
    }

    /**
     * Updates the local representation of the charging station authorization list.
     * Tokens in the updatedIdentificationTokens will be removed from the local operator api list.
     *
     * @param chargingStation               the chargingstation.
     * @param updatedLocalAuthorizations    the updated tokens.
     */
    private void updateAuthorizationList(final ChargingStation chargingStation, final Set<LocalAuthorization> updatedLocalAuthorizations) {
        Set<LocalAuthorization> authorizationList =  chargingStation.getLocalAuthorizations();
        Iterables.removeIf(authorizationList, new Predicate<LocalAuthorization>() {
            @Override
            public boolean apply(@Nullable LocalAuthorization identifyingToken) {
                return updatedLocalAuthorizations != null && updatedLocalAuthorizations.contains(identifyingToken);
            }
        });

        authorizationList.addAll(updatedLocalAuthorizations);
    }

    /**
     * Handles the {@code ChargingStationAvailabilityChangedToInoperativeEvent}.
     * <p/>
     * Sets the charging station to inoperative.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(ChargingStationAvailabilityChangedToInoperativeEvent event) {
        updateChargingStationAvailability(event.getChargingStationId(), Availability.INOPERATIVE);
    }

    /**
     * Handles the {@code ChargingStationAvailabilityChangedToOperativeEvent}.
     * <p/>
     * Sets the charging station to operative.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(ChargingStationAvailabilityChangedToOperativeEvent event) {
        updateChargingStationAvailability(event.getChargingStationId(), Availability.OPERATIVE);
    }

    /**
     * Handles the {@code ComponentAvailabilityChangedToInoperativeEvent}.
     * <p/>
     * Sets the charging station's component to inoperative.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(ComponentAvailabilityChangedToInoperativeEvent event) {
        updateComponentAvailability(event.getChargingStationId(), event.getComponentId(), event.getComponent(), Availability.INOPERATIVE);
    }

    /**
     * Handles the {@code ComponentAvailabilityChangedToOperativeEvent}.
     * <p/>
     * Sets the charging station's component to operative.
     *
     * @param event the event to handle.
     */
    @EventHandler
    public void handle(ComponentAvailabilityChangedToOperativeEvent event) {
        updateComponentAvailability(event.getChargingStationId(), event.getComponentId(), event.getComponent(), Availability.OPERATIVE);
    }

    /**
     * Updates the status of a Evse in the charging station object if the evse id matches the component id.
     *
     * @param chargingStation    charging stationidentifier.
     * @param componentId        component identifier.
     * @param status             new status.
     */
    private void updateEvseStatus(ChargingStation chargingStation, String componentId, ComponentStatus status) {
        for (Evse evse : chargingStation.getEvses()) {
            if (evse.getEvseId().equals(componentId)) {
                evse.setStatus(status);
            }
        }
    }

    /**
     * Updates the charging station's availability.
     *
     * @param chargingStationId the charging station's id.
     * @param availability      the charging station's new availability.
     */
    private void updateChargingStationAvailability(ChargingStationId chargingStationId, Availability availability) {
        ChargingStation chargingStation = repository.findOne(chargingStationId.getId());

        if (chargingStation != null) {
            chargingStation.setAvailability(availability);
            repository.createOrUpdate(chargingStation);
        }
    }

    /**
     * Updates a charging station's component availability.
     *
     * @param chargingStationId the charging station's id.
     * @param componentId       the component's id.
     * @param component         the component type.
     * @param availability      the the charging station's new availability.
     */
    private void updateComponentAvailability(ChargingStationId chargingStationId, ComponentId componentId, ChargingStationComponent component, Availability availability) {
        if (!component.equals(ChargingStationComponent.EVSE) || !(componentId instanceof EvseId)) {
            return;
        }

        ChargingStation chargingStation = repository.findOne(chargingStationId.getId());

        if (chargingStation != null) {
            for (Evse evse : chargingStation.getEvses()) {
                if (evse.getEvseId().equals(componentId.getId())) {
                    evse.setAvailability(availability);
                    break;
                }
            }
            repository.createOrUpdate(chargingStation);
        }
    }

    /**
     * Updates the opening times of the charging station.
     *
     * @param event The event which contains the opening times.
     * @param clear Whether to clear the opening times or not.
     * @return {@code true} if the update has been performed, {@code false} if the charging station can't be found.
     */
    private boolean updateChargingStationOpeningTimes(ChargingStationOpeningTimesChangedEvent event, boolean clear) {
        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            if (!event.getOpeningTimes().isEmpty()) {
                if (clear) {
                    chargingStation.getOpeningTimes().clear();
                }

                for (OpeningTime coreOpeningTime : event.getOpeningTimes()) {
                    Day dayOfWeek = coreOpeningTime.getDay();
                    String timeStart = String.format(TIME_FORMAT, coreOpeningTime.getTimeStart().getHourOfDay(), coreOpeningTime.getTimeStart().getMinutesInHour());
                    String timeStop = String.format(TIME_FORMAT, coreOpeningTime.getTimeStop().getHourOfDay(), coreOpeningTime.getTimeStop().getMinutesInHour());

                    io.motown.operatorapi.viewmodel.persistence.entities.OpeningTime openingTime = new io.motown.operatorapi.viewmodel.persistence.entities.OpeningTime(dayOfWeek, timeStart, timeStop);
                    chargingStation.getOpeningTimes().add(openingTime);
                }

                repository.createOrUpdate(chargingStation);
            }

        } else {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and update its opening times", event.getChargingStationId());
        }

        return chargingStation != null;
    }

    /**
     * Updates the location of the charging station.
     *
     * @param event The event which contains the data of the location.
     * @return {@code true} if the update has been performed, {@code false} if the charging station can't be found.
     */
    private boolean updateChargingStationLocation(ChargingStationLocationChangedEvent event) {
        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            if (event.getCoordinates() != null) {
                chargingStation.setLatitude(event.getCoordinates().getLatitude());
                chargingStation.setLongitude(event.getCoordinates().getLongitude());
            }

            if (event.getAddress() != null) {
                chargingStation.setAddressLine1(event.getAddress().getAddressLine1());
                chargingStation.setAddressLine2(event.getAddress().getAddressLine2());
                chargingStation.setPostalCode(event.getAddress().getPostalCode());
                chargingStation.setCity(event.getAddress().getCity());
                chargingStation.setRegion(event.getAddress().getRegion());
                chargingStation.setCountry(event.getAddress().getCountry());
            }

            chargingStation.setAccessibility(event.getAccessibility());

            repository.createOrUpdate(chargingStation);
        } else {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and update its location", event.getChargingStationId());
        }

        return chargingStation != null;
    }

    /**
     * Makes a charging station reservable or not reservable.
     *
     * @param chargingStationId the charging station to make reservable or not reservable.
     * @param reservable        true if reservable, false if not.
     */
    private void setReservable(ChargingStationId chargingStationId, boolean reservable) {
        ChargingStation chargingStation = repository.findOne(chargingStationId.getId());

        if (chargingStation != null) {
            chargingStation.setReservable(reservable);
            repository.createOrUpdate(chargingStation);
        }
    }

    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    /**
     * Converts a {@code Set} of {@code ConfigurationItem}s to a {@code Map} of {@code String} and {@code String}.
     *
     * @param configurationItems a {@code Set} of {@code ConfigurationItem}s.
     * @return a {@code Map} of {@code String} and {@code String}.
     */
    private Map<String, String> toConfigurationItemMap(Set<ConfigurationItem> configurationItems) {
        Map<String, String> map = new HashMap<>(configurationItems.size());

        for (ConfigurationItem configurationItem : configurationItems) {
            map.put(configurationItem.getKey(), configurationItem.getValue());
        }

        return map;
    }
}
