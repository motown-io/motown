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
import org.axonframework.commandhandling.CommandHandlerInterceptor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.InterceptorChain;
import org.axonframework.unitofwork.UnitOfWork;
import org.axonframework.unitofwork.UnitOfWorkListener;

import java.util.Collections;

/**
 * {@code CommandHandlerInterceptor} which ensures resulting events will contain the same {@code correlationId} as the
 * the command being handled. This is used to enable the tracking of results (i.e. events) of certain actions (i.e.
 * commands).
 */
public class CorrelationCommandHandlerInterceptor implements CommandHandlerInterceptor {

    /**
     * Handler method which registers a {@link CorrelationUnitOfWorkListener} if the given command contains the required
     * meta data to correlate this command to the resulting events.
     *
     * @throws Throwable no checked exceptions will be thrown. This is required to implement the {@code CommandHandlerInterceptor} interface.
     */
    @Override
    public Object handle(CommandMessage<?> command, UnitOfWork unitOfWork, InterceptorChain chain) throws Throwable {
        if (!command.getMetaData().containsKey(CorrelationToken.KEY)) {
            command = command.andMetaData(Collections.singletonMap(CorrelationToken.KEY, new CorrelationToken()));
        }
        UnitOfWorkListener listener = new CorrelationUnitOfWorkListener(command);
        unitOfWork.registerListener(listener);
        return chain.proceed();
    }
}
