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
package org.axonframework.commandhandling.gateway;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EventWaitingGatewayTest {

    private EventWaitingGateway gateway;

    private CommandBus commandBus;

    private EventBus eventBus;

    private static final String CORRELATION_ID_KEY = "correlationId";

    @Before
    public void setup() {
        commandBus = mock(CommandBus.class);
        eventBus = mock(EventBus.class);

        gateway = new EventWaitingGateway();
        gateway.setCommandBus(commandBus);
        gateway.setEventBus(eventBus);
    }

    @Test
    public void sendAndWaitForEventVerifyCommandBusCall() {
        CommandMessage<Object> command = new GenericCommandMessage<>(new Object());
        ArgumentCaptor<CommandMessage> commandMessageArgumentCaptor = ArgumentCaptor.forClass(CommandMessage.class);

        gateway.sendAndWaitForEvent(command, mock(EventCallback.class));

        verify(commandBus).dispatch(commandMessageArgumentCaptor.capture());
        assertNotNull(commandMessageArgumentCaptor.getValue().getMetaData().get(CORRELATION_ID_KEY));
    }

    @Test
    public void onEventExistingCallbackHandled() {
        // create a callback in the gateway
        EventCallback eventCallback = mock(EventCallback.class);
        when(eventCallback.onEvent(any(EventMessage.class))).thenReturn(true);
        gateway.sendAndWaitForEvent(new GenericCommandMessage<>(new Object()), eventCallback);
        // capture the correlation id
        ArgumentCaptor<CommandMessage> commandMessageArgumentCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandBus).dispatch(commandMessageArgumentCaptor.capture());
        assertNotNull(commandMessageArgumentCaptor.getValue().getMetaData().get(CORRELATION_ID_KEY));
        String correlationId = (String) commandMessageArgumentCaptor.getValue().getMetaData().get(CORRELATION_ID_KEY);
        EventMessage eventMessage = mock(EventMessage.class);

        gateway.onEvent(eventMessage, correlationId);

        verify(eventCallback).onEvent(eventMessage);
    }

    @Test
    public void onEventExistingCallbackNotHandled() {
        // create a callback in the gateway
        EventCallback eventCallback = mock(EventCallback.class);
        when(eventCallback.onEvent(any(EventMessage.class))).thenReturn(false);
        gateway.sendAndWaitForEvent(new GenericCommandMessage<>(new Object()), eventCallback);
        // capture the correlation id
        ArgumentCaptor<CommandMessage> commandMessageArgumentCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandBus).dispatch(commandMessageArgumentCaptor.capture());
        assertNotNull(commandMessageArgumentCaptor.getValue().getMetaData().get(CORRELATION_ID_KEY));
        String correlationId = (String) commandMessageArgumentCaptor.getValue().getMetaData().get(CORRELATION_ID_KEY);
        EventMessage eventMessage = mock(EventMessage.class);

        gateway.onEvent(eventMessage, correlationId);

        verify(eventCallback).onEvent(eventMessage);
    }

    @Test
    public void onEventNoCallback() {
        EventCallback eventCallback = mock(EventCallback.class);
        gateway.sendAndWaitForEvent(new GenericCommandMessage<>(new Object()), eventCallback);

        gateway.onEvent(mock(EventMessage.class), "unknownCorrelationId");

        verify(eventCallback, never()).onEvent(any(EventMessage.class));
    }

}
