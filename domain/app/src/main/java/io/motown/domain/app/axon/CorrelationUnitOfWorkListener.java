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
package io.motown.domain.app.axon;

import io.motown.domain.api.chargingstation.CorrelationToken;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.domain.EventMessage;
import org.axonframework.unitofwork.UnitOfWork;
import org.axonframework.unitofwork.UnitOfWorkListenerAdapter;

import java.util.Collections;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code UnitOfWorkListener} which adds correlation meta data to registered events. This is used to enable the tracking
 * of results (i.e. events) of certain actions (i.e.  * commands).
 */
public class CorrelationUnitOfWorkListener extends UnitOfWorkListenerAdapter {

    private final Object correlationId;

    /**
     * Creates a {@code CorrelationUnitOfWorkListener} which adds correlation meta data to registered events.
     *
     * @param command the command for which
     * @throws NullPointerException     if {@code command} is {@code null}.
     * @throws IllegalArgumentException if {@code command} does not contain the {@code correlationId} or if the
     * {@code correlationId} is not a {@code CorrelationToken}.
     */
    public CorrelationUnitOfWorkListener(CommandMessage<?> command) {
        checkNotNull(command);
        checkArgument(command.getMetaData().containsKey(CorrelationToken.KEY));
        checkArgument(command.getMetaData().get(CorrelationToken.KEY) instanceof CorrelationToken);

        correlationId = command.getMetaData().get(CorrelationToken.KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> EventMessage<T> onEventRegistered(UnitOfWork unitOfWork, EventMessage<T> event) {
        return event.withMetaData(Collections.singletonMap(CorrelationToken.KEY, correlationId));
    }
}
