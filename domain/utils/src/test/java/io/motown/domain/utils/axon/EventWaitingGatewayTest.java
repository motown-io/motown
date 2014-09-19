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
package io.motown.domain.utils.axon;

import io.motown.domain.api.chargingstation.CorrelationToken;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class EventWaitingGatewayTest {

    private static final int HIGH_TIMEOUT_IN_MILLIS = 1000;

    private static final int LOW_TIMEOUT_IN_MILLIS = 1;

    private EventWaitingGateway gateway;

    private CommandBus commandBus;

    @Before
    public void setup() {
        commandBus = mock(CommandBus.class);

        gateway = new EventWaitingGateway();
        gateway.setCommandBus(commandBus);
        gateway.setEventBus(mock(EventBus.class));
    }

    @Test
    public void testIfCommandIsDispatchedAndCorrelationIdIsNotNull() {
        CommandMessage<Object> command = new GenericCommandMessage<>(new Object());

        gateway.sendAndWaitForEvent(command, mock(EventCallback.class), HIGH_TIMEOUT_IN_MILLIS);

        CorrelationToken correlationToken = verifyDispatchAndCaptureCorrelationToken();
        assertTrue(correlationToken != null);
    }

    @Test
    public void testIfCommandIsDispatchedAndEventCallbackIsSuccessfullyCalled() {
        EventCallback eventCallback = mock(EventCallback.class);
        when(eventCallback.onEvent(any(EventMessage.class))).thenReturn(true);
        gateway.sendAndWaitForEvent(new GenericCommandMessage<>(new Object()), eventCallback, HIGH_TIMEOUT_IN_MILLIS);

        CorrelationToken correlationToken = verifyDispatchAndCaptureCorrelationToken();
        EventMessage eventMessage = mock(EventMessage.class);
        gateway.onEvent(eventMessage, correlationToken);

        verify(eventCallback).onEvent(eventMessage);
    }

    @Test
    public void testIfCommandIsDispatchedAndEventCallbackIsUnsuccessfullyCalled() {
        EventCallback eventCallback = mock(EventCallback.class);
        when(eventCallback.onEvent(any(EventMessage.class))).thenReturn(false);
        gateway.sendAndWaitForEvent(new GenericCommandMessage<>(new Object()), eventCallback, HIGH_TIMEOUT_IN_MILLIS);

        CorrelationToken correlationToken = verifyDispatchAndCaptureCorrelationToken();
        EventMessage eventMessage = mock(EventMessage.class);
        gateway.onEvent(eventMessage, correlationToken);

        verify(eventCallback).onEvent(eventMessage);
    }

    @Test
    public void testIfEventCallbackIsNotCalledIfCorrelationIdIsIncorrect() {
        EventCallback eventCallback = mock(EventCallback.class);
        gateway.sendAndWaitForEvent(new GenericCommandMessage<>(new Object()), eventCallback, HIGH_TIMEOUT_IN_MILLIS);

        gateway.onEvent(mock(EventMessage.class), new CorrelationToken());

        verify(eventCallback, never()).onEvent(any(EventMessage.class));
    }

    @Test
    public void testIfEventCallbackIsNotCalledBecauseTimerHasFired() throws InterruptedException {
        EventCallback eventCallback = mock(EventCallback.class);
        gateway.sendAndWaitForEvent(new GenericCommandMessage<>(new Object()), eventCallback, LOW_TIMEOUT_IN_MILLIS);

        CorrelationToken correlationToken = verifyDispatchAndCaptureCorrelationToken();
        EventMessage eventMessage = mock(EventMessage.class);

        Thread.sleep(2);

        gateway.onEvent(eventMessage, correlationToken);

        verify(eventCallback, never()).onEvent(any(EventMessage.class));
    }

    /**
     * Captures the {@code CorrelationToken} which is metadata of the command which is dispatched on the bus.
     *
     * @return the captured {@code CorrelationToken}.
     */
    private CorrelationToken verifyDispatchAndCaptureCorrelationToken() {
        ArgumentCaptor<CommandMessage> commandMessageArgumentCaptor = ArgumentCaptor.forClass(CommandMessage.class);
        verify(commandBus).dispatch(commandMessageArgumentCaptor.capture());
        assertNotNull(commandMessageArgumentCaptor.getValue().getMetaData().get(CorrelationToken.KEY));
        return (CorrelationToken) commandMessageArgumentCaptor.getValue().getMetaData().get(CorrelationToken.KEY);
    }
}
