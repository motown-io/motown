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

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.common.annotation.MetaData;
import org.axonframework.domain.EventMessage;
import org.axonframework.domain.IdentifierFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerAdapter;
import org.axonframework.eventhandling.annotation.EventHandler;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

public class EventWaitingGateway {

    private static final String CORRELATION_ID_KEY = "correlationId";

    private CommandBus commandBus;
    private EventBus eventBus;

    private final Map<String, TimedEventCallback> callbacks = new ConcurrentHashMap<>();

    private AtomicBoolean started = new AtomicBoolean(false);

    public void sendAndWaitForEvent(Object command, final EventCallback callback, final long timeoutInMillis) {
        final String correlationId = IdentifierFactory.getInstance().generateIdentifier();

        final TimedEventCallback timedEventCallback = new TimedEventCallback(callback);
        callbacks.put(correlationId, timedEventCallback);
        timedEventCallback.scheduleTimer(new Runnable() {
            @Override
            public void run() {
                callbacks.remove(correlationId);
            }
        }, timeoutInMillis);

        if (started.compareAndSet(false, true)) {
            eventBus.subscribe(new AnnotationEventListenerAdapter(this));
        }

        final CommandMessage commandMessage = asCommandMessage(command)
                .andMetaData(Collections.singletonMap(CORRELATION_ID_KEY, correlationId));
        commandBus.dispatch(commandMessage);
    }

    @EventHandler
    protected void onEvent(EventMessage<?> message,
                           @MetaData(value = CORRELATION_ID_KEY, required = true) String correlationId) {
        final TimedEventCallback timedEventCallback = callbacks.get(correlationId);
        if (timedEventCallback != null) {
            boolean handled = timedEventCallback.onEvent(message);
            if (handled) {
                timedEventCallback.cancelTimer();
                callbacks.remove(correlationId);
            }
        }
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    private static class TimedEventCallback implements EventCallback {

        private final EventCallback callback;
        private final ScheduledExecutorService scheduledExecutorService;

        public TimedEventCallback(EventCallback callback) {
            this.callback = callback;
            this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        public void scheduleTimer(Runnable task, long delayInMillis) {
            this.scheduledExecutorService.schedule(task, delayInMillis, TimeUnit.MILLISECONDS);
        }

        public void cancelTimer() {
            this.scheduledExecutorService.shutdownNow();
        }

        @Override
        public boolean onEvent(EventMessage<?> event) {
            return callback.onEvent(event);
        }
    }
}
