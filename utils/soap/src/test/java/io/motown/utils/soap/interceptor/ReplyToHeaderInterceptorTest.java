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
package io.motown.utils.soap.interceptor;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.impl.AddressingPropertiesImpl;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class ReplyToHeaderInterceptorTest {

    private ReplyToHeaderInInterceptor headerInInterceptor;

    private static final String MAP_PROPERTY = "javax.xml.ws.addressing.context.inbound";

    private static final String REPLY_TO_ANONYMOUS = "http://www.w3.org/2005/08/addressing/anonymous";

    @Before
    public void setup() {
        headerInInterceptor = new ReplyToHeaderInInterceptor();
    }

    @Test
    public void handleMessageWithoutMapsVerifyNoMappingPropertiesAdded() {
        Message message = new MessageImpl();
        headerInInterceptor.handleMessage(new SoapMessage(message));

        assertNull(message.get(MAP_PROPERTY));
    }

    @Test
    public void handleMessageWithMapsVerifyReplyToHeaderAdded() {
        AddressingProperties properties = new AddressingPropertiesImpl();
        Message message = new MessageImpl();
        message.put(MAP_PROPERTY, properties);
        ReplyToHeaderInInterceptor headerInInterceptor = new ReplyToHeaderInInterceptor();

        headerInInterceptor.handleMessage(new SoapMessage(message));

        assertEquals(REPLY_TO_ANONYMOUS, properties.getReplyTo().getAddress().getValue());
    }
}
