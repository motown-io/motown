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
package io.motown.soaputils.interceptor;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.Message;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPHeader;
import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageIdHeaderInterceptorTest {

    private static final String MESSAGE_ID_LOCAL_NAME = "MessageID";

    private static final String OTHER_HEADER_LOCAL_NAME = "OtherHeader";

    private MessageIdHeaderInterceptor interceptor;

    private Message message;

    @Before
    public void setup() {
        interceptor = new MessageIdHeaderInterceptor();
        message = mock(Message.class);
    }

    @Test
    public void handleMessageWithoutHeaders() {
        when(message.get(anyString())).thenReturn(new ArrayList<SOAPHeader>());

        interceptor.handleMessage(message);
    }

    @Test
    public void handleMessageWithoutHeadersVerifyHeadersNotAltered() {
        ArrayList<SoapHeader> soapHeaders = new ArrayList<>();
        when(message.get(anyString())).thenReturn(soapHeaders);

        interceptor.handleMessage(message);

        assertEquals(0, soapHeaders.size());
    }

    @Test
    public void handleMessageWithMessageIdHeaderVerifyNoNewHeaderAdded() {
        ArrayList<SoapHeader> soapHeaders = new ArrayList<>();
        SoapHeader soapHeader = mock(SoapHeader.class);
        when(soapHeader.getName()).thenReturn(new QName(MESSAGE_ID_LOCAL_NAME));
        soapHeaders.add(soapHeader);
        when(message.get(anyString())).thenReturn(soapHeaders);

        interceptor.handleMessage(message);

        assertEquals(1, soapHeaders.size());
    }

    @Test
    public void handleMessageWithoutMessageIdHeaderVerifyNewHeaderAdded() {
        // lots of stubbing here because of specific type casting in the tested functionality
        SoapHeader soapHeader = mock(SoapHeader.class);
        when(soapHeader.getName()).thenReturn(new QName(OTHER_HEADER_LOCAL_NAME));
        Element element = mock(Element.class);
        try {
            when(element.getOwnerDocument()).thenReturn(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        } catch (ParserConfigurationException e) {
            fail();
        }
        when(soapHeader.getObject()).thenReturn(element);
        ArrayList<SoapHeader> soapHeaders = new ArrayList<>();
        soapHeaders.add(soapHeader);
        when(message.get(anyString())).thenReturn(soapHeaders);

        interceptor.handleMessage(message);

        assertEquals(2, soapHeaders.size());
        SoapHeader messageIdHeader = soapHeaders.get(1);
        assertEquals(Header.Direction.DIRECTION_IN, messageIdHeader.getDirection());
        assertEquals(MESSAGE_ID_LOCAL_NAME, ((Element) messageIdHeader.getObject()).getLocalName());
        // generated message id value (UUID) is stored here:
        assertNotNull(((Element) messageIdHeader.getObject()).getFirstChild().getNodeValue());
    }

}
