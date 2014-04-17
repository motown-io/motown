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

import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.UserIdentity;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.entities.Evse;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.*;

@ContextConfiguration("classpath:operator-api-view-model-test-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChargingStationEventListenerTest {

    @Autowired
    private ChargingStationRepository repository;

    private ChargingStationEventListener listener;

    @Before
    public void setUp() throws Exception {
        listener = new ChargingStationEventListener();
        listener.setRepository(repository);

        listener.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT));
    }

    @Test
    public void testHandleChargingStationCreatedEvent() {
        assertNotNull(repository.findOne(CHARGING_STATION_ID.getId()));
    }

    @Test
    public void testHandleChargingStationBootedEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertNull(cs.getProtocol());
        assertTrue(cs.getAttributes().isEmpty());
        assertNull(cs.getLastContact());

        listener.handle(new ConfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT));
        assertNotNull(cs.getProtocol());
        assertFalse(cs.getAttributes().isEmpty());
        assertNotNull(cs.getLastContact());
    }

    @Test
    public void testHandleChargingStationSentHeartBeatEvent() {
        Date start = new Date();
        listener.handle(new ChargingStationSentHeartbeatEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT));

        Date lastContact = repository.findOne(CHARGING_STATION_ID.getId()).getLastContact();
        assertTrue(lastContact.after(start) || lastContact.equals(start));
    }

    @Test
    public void testHandleChargingStationAcceptedEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertFalse(cs.isAccepted());

        listener.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT));
        assertTrue(cs.isAccepted());
    }

    @Test
    public void testHandleChargingStationPlacedEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertNull(cs.getLatitude());
        assertNull(cs.getLongitude());
        assertNull(cs.getAddressLine1());
        assertNull(cs.getAddressLine2());
        assertNull(cs.getPostalCode());
        assertNull(cs.getCity());
        assertNull(cs.getRegion());
        assertNull(cs.getCountry());
        assertNull(cs.getAccessibility());

        listener.handle(new ChargingStationPlacedEvent(CHARGING_STATION_ID, COORDINATES, null, Accessibility.PUBLIC, IDENTITY_CONTEXT));
        assertNotNull(cs.getLatitude());
        assertNotNull(cs.getLongitude());
        assertNotNull(cs.getAccessibility());

        listener.handle(new ChargingStationPlacedEvent(CHARGING_STATION_ID, null, ADDRESS, Accessibility.PUBLIC, IDENTITY_CONTEXT));
        assertNotNull(cs.getAddressLine1());
        assertNotNull(cs.getAddressLine2());
        assertNotNull(cs.getPostalCode());
        assertNotNull(cs.getCity());
        assertNotNull(cs.getRegion());
        assertNotNull(cs.getCountry());

        assertEquals(Accessibility.PUBLIC, cs.getAccessibility());
    }

    @Test
    public void testHandleChargingStationMovedEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertNull(cs.getLatitude());
        assertNull(cs.getLongitude());
        assertNull(cs.getAddressLine1());
        assertNull(cs.getAddressLine2());
        assertNull(cs.getPostalCode());
        assertNull(cs.getCity());
        assertNull(cs.getRegion());
        assertNull(cs.getCountry());
        assertNull(cs.getAccessibility());

        listener.handle(new ChargingStationMovedEvent(CHARGING_STATION_ID, COORDINATES, null, Accessibility.PAYING, IDENTITY_CONTEXT));
        assertNotNull(cs.getLatitude());
        assertNotNull(cs.getLongitude());
        assertNotNull(cs.getAccessibility());

        listener.handle(new ChargingStationMovedEvent(CHARGING_STATION_ID, null, ADDRESS, Accessibility.PAYING, IDENTITY_CONTEXT));
        assertNotNull(cs.getAddressLine1());
        assertNotNull(cs.getAddressLine2());
        assertNotNull(cs.getPostalCode());
        assertNotNull(cs.getCity());
        assertNotNull(cs.getRegion());
        assertNotNull(cs.getCountry());

        assertEquals(Accessibility.PAYING, cs.getAccessibility());
    }

    @Test
    public void testHandleChargingStationLocationImprovedEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertNull(cs.getLatitude());
        assertNull(cs.getLongitude());
        assertNull(cs.getAddressLine1());
        assertNull(cs.getAddressLine2());
        assertNull(cs.getPostalCode());
        assertNull(cs.getCity());
        assertNull(cs.getRegion());
        assertNull(cs.getCountry());
        assertNull(cs.getAccessibility());

        listener.handle(new ChargingStationLocationImprovedEvent(CHARGING_STATION_ID, COORDINATES, null, Accessibility.PRIVATE, IDENTITY_CONTEXT));
        assertNotNull(cs.getLatitude());
        assertNotNull(cs.getLongitude());
        assertNotNull(cs.getAccessibility());

        listener.handle(new ChargingStationLocationImprovedEvent(CHARGING_STATION_ID, null, ADDRESS, Accessibility.PRIVATE, IDENTITY_CONTEXT));
        assertNotNull(cs.getAddressLine1());
        assertNotNull(cs.getAddressLine2());
        assertNotNull(cs.getPostalCode());
        assertNotNull(cs.getCity());
        assertNotNull(cs.getRegion());
        assertNotNull(cs.getCountry());

        assertEquals(Accessibility.PRIVATE, cs.getAccessibility());
    }

    @Test
    public void testHandleChargingStationOpeningTimesSetEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.getOpeningTimes().isEmpty());

        listener.handle(new ChargingStationOpeningTimesSetEvent(CHARGING_STATION_ID, OPENING_TIMES, IDENTITY_CONTEXT));
        assertFalse(cs.getOpeningTimes().isEmpty());
        assertEquals(1, cs.getOpeningTimes().size());

        listener.handle(new ChargingStationOpeningTimesSetEvent(CHARGING_STATION_ID, OPENING_TIMES, IDENTITY_CONTEXT));
        assertFalse(cs.getOpeningTimes().isEmpty());
        assertEquals(1, cs.getOpeningTimes().size());
    }

    @Test
    public void testHandleChargingStationOpeningTimesAddedEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.getOpeningTimes().isEmpty());

        listener.handle(new ChargingStationOpeningTimesAddedEvent(CHARGING_STATION_ID, OPENING_TIMES, IDENTITY_CONTEXT));
        assertFalse(cs.getOpeningTimes().isEmpty());
        assertEquals(1, cs.getOpeningTimes().size());

        listener.handle(new ChargingStationOpeningTimesAddedEvent(CHARGING_STATION_ID, ImmutableSet.<OpeningTime>builder().add(new OpeningTime(Day.FRIDAY, new TimeOfDay(18,0), new TimeOfDay(21,0))).build(), IDENTITY_CONTEXT));
        assertFalse(cs.getOpeningTimes().isEmpty());
        assertEquals(2, cs.getOpeningTimes().size());
    }

    @Test
    public void testHandleChargingStationConfiguredEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.getEvses().isEmpty());

        listener.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, IDENTITY_CONTEXT));
        assertFalse(cs.getEvses().isEmpty());
    }

    @Test
    public void testHandleChargingStationMadeReservableEventAndChargingStationMadeNotReservableEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertFalse(cs.isReservable());

        listener.handle(new ChargingStationMadeReservableEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT));
        assertTrue(cs.isReservable());

        listener.handle(new ChargingStationMadeNotReservableEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT));
        assertFalse(cs.isReservable());
    }

    @Test
    public void testHandleChargingStationStatusNotificationReceivedEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertNull(cs.getStatus());

        listener.handle(new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, ComponentStatus.OCCUPIED, new Date(), BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT));
        assertNotNull(cs.getStatus());
        assertEquals(ComponentStatus.OCCUPIED, cs.getStatus());
    }

    @Test
    public void testHandleComponentStatusNotificationReceivedEvent() {
        listener.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, IDENTITY_CONTEXT));
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        for (Evse evse : cs.getEvses()) {
            assertNull(evse.getStatus());
        }

        listener.handle(new ComponentStatusNotificationReceivedEvent(CHARGING_STATION_ID, ChargingStationComponent.EVSE, EVSE_ID, ComponentStatus.FAULTED, new Date(), BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT));
        for (Evse evse : cs.getEvses()) {
            if (evse.getEvseId().equals("1")) {
                assertEquals(ComponentStatus.FAULTED, evse.getStatus());
            } else if (evse.getEvseId().equals("2")) {
                assertNull(evse.getStatus());
            }
        }
    }

    @Test
    public void testHandleConfigurationItemsReceivedEvent() {
        ChargingStation cs = repository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.getConfigurationItems().isEmpty());

        listener.handle(new ConfigurationItemsReceivedEvent(CHARGING_STATION_ID, CONFIGURATION_ITEMS, IDENTITY_CONTEXT));
        assertFalse(cs.getConfigurationItems().isEmpty());
    }


    @Test
    public void handlePermissionGrantedEventNullChargingStation() {
        // should throw no exception
        listener.handle(new PermissionGrantedEvent(UNKNOWN_CHARGING_STATION_ID, ROOT_USER_IDENTITY, AcceptChargingStationCommand.class, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void handlePermissionGrantedEventNoPriorAuthorizations() {
        Class command = AcceptChargingStationCommand.class;
        UserIdentity userIdentity = ROOT_USER_IDENTITY;
        // pre-condition, the userIdentity has no access to the command prior to the handling we're about to test
        assertFalse(repository.findOne(CHARGING_STATION_ID.getId()).hasAuthorization(userIdentity, command));

        listener.handle(new PermissionGrantedEvent(CHARGING_STATION_ID, userIdentity, command, ROOT_IDENTITY_CONTEXT));

        assertTrue(repository.findOne(CHARGING_STATION_ID.getId()).hasAuthorization(userIdentity, command));
    }

    @Test
    public void handlePermissionGrantedEventWithPriorAuthorizations() {
        UserIdentity userIdentity = ROOT_USER_IDENTITY;
        Class existingAuthorizationCommand = AcceptChargingStationCommand.class;
        Class newCommand = RequestUnlockEvseCommand.class;
        // make sure there is at least one existing authorization before we handle a new one
        listener.handle(new PermissionGrantedEvent(CHARGING_STATION_ID, userIdentity, existingAuthorizationCommand, ROOT_IDENTITY_CONTEXT));

        listener.handle(new PermissionGrantedEvent(CHARGING_STATION_ID, userIdentity, newCommand, ROOT_IDENTITY_CONTEXT));

        assertTrue(repository.findOne(CHARGING_STATION_ID.getId()).hasAuthorization(userIdentity, existingAuthorizationCommand));
        assertTrue(repository.findOne(CHARGING_STATION_ID.getId()).hasAuthorization(userIdentity, newCommand));
    }

    @Test
    public void handlePermissionRevokedEventNullChargingStation() {
        // should throw no exception
        listener.handle(new PermissionRevokedEvent(UNKNOWN_CHARGING_STATION_ID, ROOT_USER_IDENTITY, AcceptChargingStationCommand.class, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void handlePermissionRevokedEventNoAuthorizations() {
        // should throw no exception
        listener.handle(new PermissionRevokedEvent(CHARGING_STATION_ID, ROOT_USER_IDENTITY, AcceptChargingStationCommand.class, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void handlePermissionRevokedEvent() {
        UserIdentity userIdentity = ROOT_USER_IDENTITY;
        Class command = AcceptChargingStationCommand.class;
        // make sure the user has authorization for the command
        listener.handle(new PermissionGrantedEvent(CHARGING_STATION_ID, userIdentity, command, ROOT_IDENTITY_CONTEXT));
        assertTrue(repository.findOne(CHARGING_STATION_ID.getId()).hasAuthorization(userIdentity, command));

        listener.handle(new PermissionRevokedEvent(CHARGING_STATION_ID, userIdentity, command, ROOT_IDENTITY_CONTEXT));

        assertFalse(repository.findOne(CHARGING_STATION_ID.getId()).hasAuthorization(userIdentity, command));
    }
}
