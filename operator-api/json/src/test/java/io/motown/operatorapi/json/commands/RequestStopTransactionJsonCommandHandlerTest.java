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
package io.motown.operatorapi.json.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.motown.operatorapi.json.gson.RequestStopTransactionTypeAdapter;
import io.motown.operatorapi.viewmodel.model.RequestStopTransactionApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestStopTransactionJsonCommandHandlerTest {

    private Gson gson;

    private RequestStopTransactionJsonCommandHandler handler = new RequestStopTransactionJsonCommandHandler();

    @Before
    public void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(RequestStopTransactionApiCommand.class, new RequestStopTransactionTypeAdapter())
                .create();

        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());

        // setup mocking for JPA / spring repo.
        ChargingStationRepository repo = mock(ChargingStationRepository.class);
        ChargingStation registeredStation = mock(ChargingStation.class);
        when(registeredStation.getProtocol()).thenReturn("OCPPS15");
        when(registeredStation.isAccepted()).thenReturn(true);
        ChargingStation unregisteredStation = mock(ChargingStation.class);
        when(unregisteredStation.isAccepted()).thenReturn(false);
        when(repo.findOne("TEST_REGISTERED")).thenReturn(registeredStation);
        when(repo.findOne("TEST_UNREGISTERED")).thenReturn(unregisteredStation);

        handler.setRepository(repo);
    }

    @Test
    public void testHandleStopTransactionOnRegisteredStation() {
        JsonObject commandObject = gson.fromJson("{'id' : 123}", JsonObject.class);
        handler.handle("TEST_REGISTERED", commandObject);
    }

    //TODO: Add more tests scenarios when the RequestStopTransactionJsonCommandHandler is more final - Ingo Pak, 04 dec 2013
}
