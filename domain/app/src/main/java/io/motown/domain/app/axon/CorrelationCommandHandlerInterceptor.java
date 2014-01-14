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

import org.axonframework.commandhandling.CommandHandlerInterceptor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.InterceptorChain;
import org.axonframework.unitofwork.UnitOfWork;
import org.axonframework.unitofwork.UnitOfWorkListener;

/**
 * {@code CommandHandlerInterceptor} which ensures resulting events will contain the same {@code correlationId} as the
 * the command being handled. This is used to enable the tracking of results (i.e. events) of certain actions (i.e.
 * commands).
 */
public class CorrelationCommandHandlerInterceptor implements CommandHandlerInterceptor {

    /**
     * Handler method which registers a {@link CorrelationUnitOfWorkListener} if the given command contains the required
     * meta data to correlate this command to the resulting events.
     */
    @Override
    public Object handle(CommandMessage<?> command, UnitOfWork unitOfWork, InterceptorChain chain) throws Throwable {
        if (command.getMetaData().containsKey(CorrelationUnitOfWorkListener.CORRELATION_ID_KEY)) {
            UnitOfWorkListener auditListener = new CorrelationUnitOfWorkListener(command);
            unitOfWork.registerListener(auditListener);
        }
        return chain.proceed();
    }
}
