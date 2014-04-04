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
package io.motown.chargingstationconfiguration.app;

import io.motown.chargingstationconfiguration.viewmodel.domain.DomainService;
import io.motown.domain.api.chargingstation.ConfigureChargingStationCommand;
import io.motown.domain.api.chargingstation.Evse;
import io.motown.domain.api.chargingstation.UnconfiguredChargingStationBootedEvent;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.NullUserIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.mockito.Mockito.*;

public class ConfigurationEventListenerTest {

    public static final int NUMBER_OF_EVSES = 3;
    private ConfigurationEventListener eventListener;

    private DomainService domainService;

    private ConfigurationCommandGateway gateway;

    private static final String addOnId = "1";

    private AddOnIdentity addOnIdentity = new TypeBasedAddOnIdentity(ConfigurationEventListener.ADD_ON_TYPE, addOnId);

    @Before
    public void setUp() {
        eventListener = new ConfigurationEventListener();

        domainService = mock(DomainService.class);
        eventListener.setDomainService(domainService);

        gateway = mock(ConfigurationCommandGateway.class);
        eventListener.setCommandGateway(gateway);
        eventListener.setAddOnId(addOnId);
    }

    @Test
    public void testUnknownConfiguration() {
        String unknownManufacturer = "UNKNOWN";
        String unknownModel = "MODEL";
        when(domainService.getEvses(unknownManufacturer, unknownModel)).thenReturn(new HashSet<Evse>());

        eventListener.onEvent(new UnconfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, getAttributes(unknownManufacturer, unknownModel), IDENTITY_CONTEXT));

        verify(domainService).getEvses(unknownManufacturer, unknownModel);
        verify(gateway, never()).send(any(ConfigureChargingStationCommand.class));
    }

    @Test
    public void testDomainServiceReturningNull() {
        String unknownManufacturer = "UNKNOWN";
        String unknownModel = "MODEL";
        when(domainService.getEvses(unknownManufacturer, unknownModel)).thenReturn(null);

        eventListener.onEvent(new UnconfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, getAttributes(unknownManufacturer, unknownModel), IDENTITY_CONTEXT));

        verify(domainService).getEvses(unknownManufacturer, unknownModel);
        verify(gateway, never()).send(any(ConfigureChargingStationCommand.class));
    }

    @Test
    public void testConfigurationFound() {
        String manufacturer = "MOTOWN";
        String model = "MODEL1";
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());
        when(domainService.getEvses(manufacturer, model)).thenReturn(getEvses(NUMBER_OF_EVSES));

        eventListener.onEvent(new UnconfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, getAttributes(manufacturer, model), identityContext));

        verify(domainService).getEvses(manufacturer, model);
        verify(gateway).send(new ConfigureChargingStationCommand(CHARGING_STATION_ID, getEvses(NUMBER_OF_EVSES), identityContext));
    }

}
