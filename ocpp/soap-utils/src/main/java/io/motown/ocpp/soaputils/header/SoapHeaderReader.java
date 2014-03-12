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
package io.motown.ocpp.soaputils.header;

import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import javax.xml.ws.handler.MessageContext;
import java.util.List;

public class SoapHeaderReader {

    private static final Logger LOG = LoggerFactory.getLogger(SoapHeaderReader.class);

    /**
     * Gets the charging station address from the SOAP "From" header.
     *
     * @param messageContext message context
     * @return charging station address, or empty string if From header is empty or doesn't exist.
     */
    public String getChargingStationAddress(MessageContext messageContext) {
        if (!(messageContext instanceof WrappedMessageContext)) {
            LOG.warn("Unable to get message context, or message context is not the right type.");
            return "";
        }

        Message message = ((WrappedMessageContext) messageContext).getWrappedMessage();
        List<Header> headers = CastUtils.cast((List<?>) message.get(Header.HEADER_LIST));

        for (Header h : headers) {
            Element n = (Element) h.getObject();

            if (n.getLocalName().equals("From")) {
                return n.getTextContent();
            }
        }

        LOG.warn("No 'From' header found in request. Not able to determine charging station address.");
        return "";
    }

}
