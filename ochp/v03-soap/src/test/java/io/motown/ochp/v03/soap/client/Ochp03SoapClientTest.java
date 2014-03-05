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
package io.motown.ochp.v03.soap.client;

import io.motown.ochp.v03.soap.schema.Echs;
import org.junit.Before;
import org.junit.Test;

import static org.jgroups.util.Util.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class Ochp03SoapClientTest {

    private Ochp03SoapClient client;

    @Before
    public void setUp() {
        OchpProxyFactory ochpClientProxyFactory = mock(OchpProxyFactory.class);
        Echs ochp03SoapClient = mock(Echs.class);

        when(ochpClientProxyFactory.createOchpService(anyString(), anyBoolean())).thenReturn(ochp03SoapClient);

        client = new Ochp03SoapClient();
        client.setOchpProxyFactory(ochpClientProxyFactory);
    }

    //TODO: Add tests - Ingo Pak, 05 Mar 2014
//    @Test
//    public void authenticateVerifyReturnValue() {
//        when(client.authenticate()).thenReturn("token123");
//
//        String authenticationToken = client.authenticate();
//
//        assertNotNull(authenticationToken);
//    }

}
