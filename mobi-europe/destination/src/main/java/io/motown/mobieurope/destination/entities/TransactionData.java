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
package io.motown.mobieurope.destination.entities;

import io.motown.mobieurope.shared.enums.EnergyUnit;
import io.motown.mobieurope.shared.enums.ServiceTypeIdentifier;
import io.motown.mobieurope.shared.enums.TransactionStatus;
import io.motown.mobieurope.source.soap.schema.ServiceType;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TransactionData {

    ServiceTypeIdentifier serviceType = ServiceTypeIdentifier.EV_CHARGING;

    Date startTimestamp;

    Date endTimestamp;

    TransactionStatus transactionStatus;

    double energyConsumed;

    EnergyUnit energyUnit = EnergyUnit.WH;

    String localOperatorIdentifier;

    String localServiceIdentifier;

    String homeOperatorIdentifier;

    String userIdentifier;

    String authorizationIdentifier;

    public TransactionData() {

    }

    public io.motown.mobieurope.source.soap.schema.TransactionData getTransactionData() {
        io.motown.mobieurope.source.soap.schema.TransactionData transactionData = new io.motown.mobieurope.source.soap.schema.TransactionData();
        transactionData.setServiceType(ServiceType.fromValue(this.serviceType.value()));
        transactionData.setTransactionStatus(io.motown.mobieurope.source.soap.schema.TransactionStatus.fromValue(this.transactionStatus.value()));
        transactionData.setEnergyConsumed(this.energyConsumed);
        transactionData.setEnergyUnit(io.motown.mobieurope.source.soap.schema.EnergyUnit.fromValue(this.energyUnit.value()));
        transactionData.setLocalOperatorIdentifier(this.localOperatorIdentifier);
        transactionData.setLocalServiceIdentifier(this.localServiceIdentifier);
        transactionData.setHomeOperatorIdentifier(this.homeOperatorIdentifier);
        transactionData.setUserIdentifier(this.userIdentifier);
        transactionData.setAuthorizationIdentifier(this.authorizationIdentifier);

        try {
            GregorianCalendar calendarStart = new GregorianCalendar();
            GregorianCalendar calendarStop = new GregorianCalendar();
            calendarStart.setTime(this.startTimestamp);
            calendarStop.setTime(this.endTimestamp);
            XMLGregorianCalendar xmlGregorianCalendarStart = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendarStart);
            XMLGregorianCalendar xmlGregorianCalendarStop = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendarStop);
            transactionData.setStartTimestamp(xmlGregorianCalendarStart);
            transactionData.setEndTimestamp(xmlGregorianCalendarStop);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactionData;
    }

    public void setServiceType(ServiceTypeIdentifier serviceType) {
        this.serviceType = serviceType;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void setEnergyConsumed(double energyConsumed) {
        this.energyConsumed = energyConsumed;
    }

    public void setEnergyUnit(EnergyUnit energyUnit) {
        this.energyUnit = energyUnit;
    }

    public void setLocalOperatorIdentifier(String localOperatorIdentifier) {
        this.localOperatorIdentifier = localOperatorIdentifier;
    }

    public void setLocalServiceIdentifier(String localServiceIdentifier) {
        this.localServiceIdentifier = localServiceIdentifier;
    }

    public void setHomeOperatorIdentifier(String homeOperatorIdentifier) {
        this.homeOperatorIdentifier = homeOperatorIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public void setAuthorizationIdentifier(String authorizationIdentifier) {
        this.authorizationIdentifier = authorizationIdentifier;
    }

    public boolean isValid() {
        return true;
    }
}

