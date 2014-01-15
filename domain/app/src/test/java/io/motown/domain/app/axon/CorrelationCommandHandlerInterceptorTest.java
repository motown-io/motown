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

import io.motown.domain.api.chargingstation.AuthorizeCommand;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.InterceptorChain;
import org.axonframework.unitofwork.UnitOfWork;
import org.junit.Test;

import java.util.Collections;

import static io.motown.domain.app.axon.DomainAppTestUtils.getChargingStationId;
import static io.motown.domain.app.axon.DomainAppTestUtils.getTextualToken;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CorrelationCommandHandlerInterceptorTest {

    @Test
    public void verifyCorrelationUnitOfWorkListenerIsRegistered() throws Throwable {
        CommandMessage<AuthorizeCommand> command = new GenericCommandMessage<>(new AuthorizeCommand(getChargingStationId(), getTextualToken()))
                .withMetaData(Collections.singletonMap("correlationId", "12345"));
        UnitOfWork unitOfWork = mock(UnitOfWork.class);
        InterceptorChain chain = mock(InterceptorChain.class);

        new CorrelationCommandHandlerInterceptor().handle(command, unitOfWork, chain);

        verify(unitOfWork).registerListener(any(CorrelationUnitOfWorkListener.class));
    }

    @Test
    public void verifyCorrelationUnitOfWorkListenerIsNotRegistered() throws Throwable {
        CommandMessage<AuthorizeCommand> command = new GenericCommandMessage<>(new AuthorizeCommand(getChargingStationId(), getTextualToken()));
        UnitOfWork unitOfWork = mock(UnitOfWork.class);
        InterceptorChain chain = mock(InterceptorChain.class);

        new CorrelationCommandHandlerInterceptor().handle(command, unitOfWork, chain);

        verifyZeroInteractions(unitOfWork);
    }

    @Test
    public void verifyInterceptorChainIsProceeded() throws Throwable {
        CommandMessage<AuthorizeCommand> command = new GenericCommandMessage<>(new AuthorizeCommand(getChargingStationId(), getTextualToken()))
                .withMetaData(Collections.singletonMap("correlationId", "12345"));
        UnitOfWork unitOfWork = mock(UnitOfWork.class);
        InterceptorChain chain = mock(InterceptorChain.class);

        new CorrelationCommandHandlerInterceptor().handle(command, unitOfWork, chain);

        verify(chain).proceed();
    }
}
