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
package io.motown.ocpp.soap.util;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Provides methods to convert java dates to XML dates.
 */
public class DateConverter {

    /**
     * Converts the Java date object to XMLGregorianCalendar
     * @param date date to convert
     * @return converted date
     */
    public static XMLGregorianCalendar toXmlGregorianCalendar(Date date) {
        GregorianCalendar timeStamp = new GregorianCalendar();
        timeStamp.setTime(date);
        // TODO: Use a vendor-independent implementation of XMLGregorianCalendar. - Mark van den Bergh, November 15th 2013
        return new XMLGregorianCalendarImpl(timeStamp);
    }

}
