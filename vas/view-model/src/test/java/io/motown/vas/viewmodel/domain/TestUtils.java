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
package io.motown.vas.viewmodel.domain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.chargingstation.*;
import io.motown.vas.viewmodel.persistence.entities.ChargingStation;

import java.util.*;

public final class TestUtils {

    public static final int TRANSACTION_NUMBER = 123;
    public static final int MAX_AMP_32 = 32;
    public static final int PHASE_3 = 3;
    public static final int VOLTAGE_230 = 230;
    public static final int MAX_HOURS = 12;

    private TestUtils() {
        // Private no-arg constructor to prevent instantiation of utility class.
    }

    public static ChargingStation getRegisteredAndConfiguredChargingStation() {
        ChargingStation cs = new ChargingStation(getChargingStationId().getId(), getChargingStationAddress());
        cs.setRegistered(true);
        cs.setNumberOfEvses(getEvses().size());
        cs.setConfigured(true);

        return cs;
    }

    public static ChargingStationId getChargingStationId() {
        return new ChargingStationId("CS-001");
    }

    public static String getProtocol() {
        return "VAS10";
    }

    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }

    public static IdentifyingToken getIdentifyingToken() {
        return new TextualToken("ID-TAG");
    }

    public static String getIdTag() {
        return "ID-TAG";
    }

    public static NumberedTransactionId getNumberedTransactionId() {
        return new NumberedTransactionId(getChargingStationId(), getProtocol(), TRANSACTION_NUMBER);
    }

    public static String getChargingStationAddress() {
        return "127.0.0.1";
    }

    public static String getVendor() {
        return "Motown";
    }

    public static String getModel() {
        return "ChargingStation";
    }

    public static Map<String, String> getConfigurationItems() {
        return ImmutableMap.<String, String>builder()
                .put("io.motown.sockets.amount", "2")
                .put("io.motown.random.config.item", "true")
                .put("io.motown.another.random.config.item", "12")
                .put("io.motown.yet.another.one", "blue")
                .build();
    }

    public static Set<Evse> getEvses() {
        List<Connector> connectors = ImmutableList.<Connector>builder()
                .add(new Connector(MAX_AMP_32, PHASE_3, VOLTAGE_230, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_2))
                .build();

        return ImmutableSet.<Evse>builder()
                .add(new Evse(new EvseId(1), connectors))
                .add(new Evse(new EvseId(2), connectors))
                .build();
    }

    public static EvseId getEvseId() {
        return new EvseId(1);
    }

    public static int getReservationNumber() {
        return 1;
    }

    public static ReservationId getReservationId() {
        return new NumberedReservationId(getChargingStationId(), getProtocol(), getReservationNumber());
    }

    public static String getMessageId() {
        return "messageId";
    }

    public static String getData() {
        return "data";
    }

    public static String getConfigurationKey() {
        return "configKey";
    }

    public static String getConfigurationValue() {
        return "configValue";
    }

    public static String getFirmwareUpdateLocation() {
        return "ftp://test";
    }

    public static Map<String, String> getUpdateFirmwareAttributes(String numberOfRetries, String retryInterval) {
        return ImmutableMap.<String, String>builder()
                .put("NUM_RETRIES", numberOfRetries)
                .put("RETRY_INTERVAL", retryInterval)
                .build();
    }

    public static List<IdentifyingToken> getAuthorizationList() {
        List<IdentifyingToken> list = new ArrayList<>();
        list.add(new TextualToken("1"));
        list.add(new TextualToken("2"));
        return list;
    }

    public static Integer getAuthorizationListVersion() {
        return 1;
    }

    public static String getAuthorizationListHash() {
        return "hash";
    }

    public static AuthorizationListUpdateType getAuthorizationListUpdateType() {
        return AuthorizationListUpdateType.DIFFERENTIAL;
    }

    public static ReservationStatus getReservationStatus() {
        return ReservationStatus.UNAVAILABLE;
    }

    public static String getChargingStationSerialNumber() {
        return "serialNumber";
    }

    public static String getFirmwareVersion() {
        return "firmwareVersion";
    }

    public static String getIccid() {
        return "iccid";
    }

    public static String getImsi() {
        return "imsi";
    }

    public static String getMeterType() {
        return "meterType";
    }

    public static String getMeterSerialNumber() {
        return "meterSerialNumber";
    }

    public static Map<String, String> getEmptyAttributesMap() {
        return ImmutableMap.<String, String>of();
    }

    public static Map<String, String> getStartTransactionAttributesMap(int reservationId) {
        return ImmutableMap.<String, String>builder()
                .put("reservationId", new NumberedReservationId(getChargingStationId(), getProtocol(), reservationId).getId())
                .build();
    }

    public static List<MeterValue> getEmptyMeterValuesList() {
        return new ArrayList<MeterValue>();
    }

    public static List<MeterValue> getMeterValuesList() {
        List<MeterValue> values = new ArrayList<>();

        for (int hour = 1; hour < MAX_HOURS; hour++) {
            // make sure dates can be compared to other instances of this list
            Calendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            Date date = c.getTime();

            values.add(new MeterValue(date, "10"));
        }
        return values;
    }
}
