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
package io.motown.datatransfer.handling;

import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.IncomingDataTransferReceivedEvent;
import io.motown.domain.api.chargingstation.IncomingDataTransferResponseCommand;
import io.motown.domain.api.chargingstation.IncomingDataTransferResultStatus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.common.annotation.MetaData;
import org.axonframework.eventhandling.annotation.EventHandler;

import java.util.Collections;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

public class IncomingDataTransferEventListener {

    private DataTransferHandlingCommandGateway commandGateway;

    /**
     * Listens for {@code IncomingDataTransferReceivedEvent} and handles the incoming datatransfer.
     * Sends a {@code IncomingDataTransferResponseCommand} containing the response status and optional
     * data to be sent back to the charging station.
     *
     * @param event            the incoming data transfer event.
     * @param correlationToken correlation token which will be added to outgoing command if it's not null or empty.
     */
    @EventHandler
    protected void onEvent(IncomingDataTransferReceivedEvent event,
                           @MetaData(value = CorrelationToken.KEY, required = false) CorrelationToken correlationToken) {
        /*
         * Handle the incoming datatransfer message here, to complete the loop we now
         * just return ACCEPTED and no response data.
        */

        IncomingDataTransferResultStatus processingStatus = IncomingDataTransferResultStatus.ACCEPTED;
        String responseData = "";
        CommandMessage commandMessage = asCommandMessage(new IncomingDataTransferResponseCommand(event.getChargingStationId(), responseData, processingStatus, event.getIdentityContext()));

        if (correlationToken != null) {
            commandMessage = commandMessage.andMetaData(Collections.singletonMap(CorrelationToken.KEY, correlationToken));
        }

        commandGateway.send(commandMessage);
    }

    public void setCommandGateway(DataTransferHandlingCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
}
