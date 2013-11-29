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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class RegisterJsonCommandHandlerTest {

    private RegisterJsonCommandHandler handler = new RegisterJsonCommandHandler();

    @Before
    public void setUp() {
        handler.setGson(new GsonBuilder().create());
        handler.setCommandGateway(new TestDomainCommandGateway());

        // setup mocking for JPA / spring repo.
        ChargingStationRepository repo = mock(ChargingStationRepository.class);
        ChargingStation registeredStation = mock(ChargingStation.class);
        when(registeredStation.getRegistered()).thenReturn(true);
        ChargingStation unregisteredStation = mock(ChargingStation.class);
        when(unregisteredStation.getRegistered()).thenReturn(false);
        when(repo.findOne("TEST_REGISTERED")).thenReturn(registeredStation);
        when(repo.findOne("TEST_UNREGISTERED")).thenReturn(unregisteredStation);

        handler.setRepository(repo);
    }

    @Test
    public void testHandleComplete() {
        String json = "['Register',{'configuration' : {'connectors' : [{'connectorId' : 1, 'connectorType' : 'Type2', 'maxAmp' : 16 },{'connectorId' : 2, 'connectorType' : 'Combo', 'maxAmp' : 32}], 'settings' : {'key':'value', 'key2':'value2'}}}]";
        handler.handle("TEST_UNREGISTERED", json);
    }

    @Test(expected=IllegalStateException.class)
    public void testHandleCompleteUnRegistered() {
        String json = "['Register',{'configuration' : {'connectors' : [{'connectorId' : 1, 'connectorType' : 'Type2', 'maxAmp' : 16 },{'connectorId' : 2, 'connectorType' : 'Combo', 'maxAmp' : 32}], 'settings' : {'key':'value', 'key2':'value2'}}}]";
        handler.handle("TEST_REGISTERED", json);
    }

      //TODO

      //    @Test
//    public void testHandleConnectorsOnly() {
//        String json = "['Configure',{'connectors' : [{'connectorId' : 1, 'connectorType' : 'Type2', 'maxAmp' : 16 },{'connectorId' : 2, 'connectorType' : 'Combo', 'maxAmp' : 32}]}]";
//        handler.handle("TEST_CP", json);
//    }
//
//    @Test(expected=IllegalArgumentException.class)
//    public void testHandleNull() {
//        String json = "['Configure', null]";
//        handler.handle("TEST_CP", json);
//    }
//
//    @Test(expected=IllegalArgumentException.class)
//    public void testHandleFaultyPayload() {  // connector not in a list
//        String json = "['Configure',{'connectors' : {'connectorId' : 1, 'connectorType' : 'Type2', 'maxAmp' : 16 }}]";
//        handler.handle("TEST_CP", json);
//    }
//
//    @Test(expected=JsonSyntaxException.class)
//    public void testHandleFaultyJson() {  // list instead of array
//        String json = "['Configure',{'connectors' : [{['connectorId' : 1, 'connectorType' : 'Type2', 'maxAmp' : 16 ],{'connectorId' : 2, 'connectorType' : 'Combo', 'maxAmp' : 32}]}]";
//        handler.handle("TEST_CP", json);
//    }

}