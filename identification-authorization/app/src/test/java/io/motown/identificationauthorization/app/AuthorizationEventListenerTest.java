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
import io.motown.domain.api.chargingstation.DenyAuthorizationCommand;
import io.motown.domain.api.chargingstation.GrantAuthorizationCommand;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.Collections;

import static io.motown.identificationauthorization.app.TestUtils.*;
import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

public class AuthorizationEventListenerTest {

    private AuthorizationEventListener eventListener;

    private IdentificationAuthorizationService service;

    private AuthorizationCommandGateway gateway;

    @Before
    public void setUp() {
        eventListener = new AuthorizationEventListener();

        service = mock(IdentificationAuthorizationService.class);
        when(service.isValid(getInvalidIdentifyingToken())).thenReturn(false);
        when(service.isValid(getValidIdentifyingToken())).thenReturn(true);
        eventListener.setService(service);

        gateway = mock(AuthorizationCommandGateway.class);
        eventListener.setCommandGateway(gateway);
    }

    @Test
    public void testValidIdentification() {
        eventListener.onEvent(new AuthorizationRequestedEvent(getChargingStationId(), getValidIdentifyingToken()), getCorrelationId());

        verify(service).isValid(getValidIdentifyingToken());

        final CommandMessage command = asCommandMessage(
                new GrantAuthorizationCommand(getChargingStationId(), getValidIdentifyingToken())).andMetaData(Collections.singletonMap("correlationId", getCorrelationId()));

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
        eventListener.onEvent(new AuthorizationRequestedEvent(getChargingStationId(), getInvalidIdentifyingToken()), getCorrelationId());

        verify(service).isValid(getInvalidIdentifyingToken());

        final CommandMessage command = asCommandMessage(
                new DenyAuthorizationCommand(getChargingStationId(), getInvalidIdentifyingToken())).andMetaData(Collections.singletonMap("correlationId", getCorrelationId()));

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
        eventListener.onEvent(new AuthorizationRequestedEvent(getChargingStationId(), getValidIdentifyingToken()), null);

        verify(service).isValid(getValidIdentifyingToken());

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
