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
package io.motown.identificationauthorization.cirplugin;

import io.motown.identificationauthorization.cirplugin.cir.schema.ArrayOfCard;
import io.motown.identificationauthorization.cirplugin.cir.schema.InquireResult;
import io.motown.identificationauthorization.cirplugin.cir.schema.ServiceSoap;
import io.motown.identificationauthorization.cirplugin.cir.schema.WebServiceHeader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.xml.ws.Holder;

import static io.motown.identificationauthorization.cirplugin.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CirAuthenticationTest {

    private CirAuthentication cirAuthentication;

    private ServiceSoap serviceSoap;

    @Before
    public void setup() {
        cirAuthentication = new CirAuthentication();

        cirAuthentication.setUsername(getCirServiceUsername());
        cirAuthentication.setPassword(getCirServicePassword());

        serviceSoap = mock(ServiceSoap.class);
        cirAuthentication.setCirService(serviceSoap);
    }

    @Test
    public void testIsValid() {
        when(serviceSoap.inquire(any(ArrayOfCard.class), any(Holder.class))).thenReturn(getInquireResult(true));

        assertTrue(cirAuthentication.isValid(getIdentifyingToken()));

        ArgumentCaptor<ArrayOfCard> arrayOfCardArgument = ArgumentCaptor.forClass(ArrayOfCard.class);
        ArgumentCaptor<Holder> holderArgument = ArgumentCaptor.forClass(Holder.class);
        verify(serviceSoap).inquire(arrayOfCardArgument.capture(), holderArgument.capture());

        assertEquals( arrayOfCardArgument.getValue().getCard().get(0).getCardID(), getIdentifyingToken().getToken() );
        WebServiceHeader header = (WebServiceHeader) holderArgument.getValue().value;
        assertEquals(header.getUsername(), getCirServiceUsername());
        assertEquals(header.getPassword(), getCirServicePassword());
    }

    @Test
    public void testIsInvalid() {
        when(serviceSoap.inquire(any(ArrayOfCard.class), any(Holder.class))).thenReturn(getInquireResult(false));

        assertFalse(cirAuthentication.isValid(getIdentifyingToken()));
    }

    @Test
    public void testErrorStillProcessesResult() {
        when(serviceSoap.inquire(any(ArrayOfCard.class), any(Holder.class))).thenReturn(getInquireResultValidWithError());

        assertTrue(cirAuthentication.isValid(getIdentifyingToken()));
    }

    @Test
    public void testEmptyWebServiceResponse() {
        when(serviceSoap.inquire(any(ArrayOfCard.class), any(Holder.class))).thenReturn(null);

        assertFalse(cirAuthentication.isValid(getIdentifyingToken()));
    }

    @Test
    public void testWebServiceResponseEmptyArrayOfCards() {
        when(serviceSoap.inquire(any(ArrayOfCard.class), any(Holder.class))).thenReturn(new InquireResult());

        assertFalse(cirAuthentication.isValid(getIdentifyingToken()));
    }


}
