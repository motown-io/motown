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
package io.motown.identificationauthorization.app;

import io.motown.domain.api.chargingstation.AuthorizationRequestedEvent;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.DenyAuthorizationCommand;
import io.motown.domain.api.chargingstation.GrantAuthorizationCommand;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.NullUserIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.Collections;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;
import static org.mockito.Mockito.*;

public class AuthorizationEventListenerTest {

    private AuthorizationEventListener eventListener;

    private IdentificationAuthorizationService service;

    private AuthorizationCommandGateway gateway;

    private static final String ADD_ON_TYPE = "IDENTIFICATION-AUTHORIZATION";

    private IdentityContext identityContext;

    @Before
    public void setUp() {
        eventListener = new AuthorizationEventListener();

        service = mock(IdentificationAuthorizationService.class);
        when(service.isValid(INVALID_IDENTIFYING_TOKEN)).thenReturn(false);
        when(service.isValid(IDENTIFYING_TOKEN)).thenReturn(true);
        eventListener.setIdentificationAuthorizationService(service);

        gateway = mock(AuthorizationCommandGateway.class);
        eventListener.setCommandGateway(gateway);
        eventListener.setAddOnIdentity("1");

        identityContext = new IdentityContext(new TypeBasedAddOnIdentity(ADD_ON_TYPE, "1"), new NullUserIdentity());
    }

    @Test
    public void testValidIdentification() {
        CorrelationToken token = new CorrelationToken();
        eventListener.onEvent(new AuthorizationRequestedEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, identityContext), token);

        verify(service).isValid(IDENTIFYING_TOKEN);

        final CommandMessage command = asCommandMessage(
                new GrantAuthorizationCommand(CHARGING_STATION_ID, IDENTIFYING_TOKEN, identityContext)).andMetaData(Collections.singletonMap(CorrelationToken.KEY, token));

        // because GenericCommandMessage doesn't implement 'equals' method we have to provide a ArgumentMatcher to validate the argument
        verify(gateway).send(argThat(new ArgumentMatcher<CommandMessage>() {
            @Override
            public boolean matches(Object o) {
                if (!(o instanceof GenericCommandMessage)) {
                    return false;
                }
                GenericCommandMessage arg = (GenericCommandMessage) o;
                return command.getMetaData().equals((arg).getMetaData()) && command.getPayload().equals((arg).getPayload());
            }
        }));
    }

    @Test
    public void testInvalidIdentification() {
        CorrelationToken token = new CorrelationToken();

        eventListener.onEvent(new AuthorizationRequestedEvent(CHARGING_STATION_ID, INVALID_IDENTIFYING_TOKEN, identityContext), token);

        verify(service).isValid(INVALID_IDENTIFYING_TOKEN);

        final CommandMessage command = asCommandMessage(
                new DenyAuthorizationCommand(CHARGING_STATION_ID, INVALID_IDENTIFYING_TOKEN, identityContext)).andMetaData(Collections.singletonMap(CorrelationToken.KEY, token));

        // because GenericCommandMessage doesn't implement 'equals' method we have to provide a ArgumentMatcher to validate the argument
        verify(gateway).send(argThat(new ArgumentMatcher<CommandMessage>() {
            @Override
            public boolean matches(Object o) {
                if (!(o instanceof GenericCommandMessage)) {
                    return false;
                }
                GenericCommandMessage arg = (GenericCommandMessage) o;
                return command.getMetaData().equals((arg).getMetaData()) && command.getPayload().equals((arg).getPayload());

            }
        }));
    }

    @Test
    public void testNullCorrelationId() {
        eventListener.onEvent(new AuthorizationRequestedEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, identityContext), null);

        verify(service).isValid(IDENTIFYING_TOKEN);

        // because GenericCommandMessage doesn't implement 'equals' method we have to provide a ArgumentMatcher to validate the argument
        verify(gateway).send(argThat(new ArgumentMatcher<CommandMessage>() {
            @Override
            public boolean matches(Object o) {
                if (!(o instanceof GenericCommandMessage)) {
                    return false;
                }
                // just verify the meta data size
                return ((GenericCommandMessage) o).getMetaData().size() == 0;
            }
        }));
    }

}
