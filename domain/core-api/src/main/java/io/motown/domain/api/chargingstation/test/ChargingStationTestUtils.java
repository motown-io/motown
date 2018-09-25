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
package io.motown.domain.api.chargingstation.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.*;
import org.joda.time.DateTimeUtils;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Utility methods and constants which can be used for tests involving the Domain's Core API.
 */
public final class ChargingStationTestUtils {

    /**
     * The default protocol identifier.
     */
    public static final String PROTOCOL = "OCPPS15";

    /**
     * The default maximum amperage.
     */
    public static final int MAX_AMP = 32;

    /**
     * The default phase.
     */
    public static final int PHASE = 3;

    /**
     * The default voltage.
     */
    public static final int VOLTAGE = 240;

    /**
     * The default connector.
     */
    public static final Connector CONNECTOR = new Connector(MAX_AMP, PHASE, VOLTAGE, ChargingProtocol.MODE3, Current.AC, ConnectorType.TESLA);

    /**
     * The default number of connectors on an EVSE.
     */
    public static final int NUMBER_OF_CONNECTORS = 3;

    /**
     * Two minutes in milliseconds.
     */
    public static final int TWO_MINUTES = 120000;
    /**
     * The timestamp in milliseconds two minutes ago.
     * <p/>
     * Note: this is a constant. This means this timestamp isn't two minutes ago from the time you use but two minutes
     * ago from the time this class was loaded.
     */
    public static final Date TWO_MINUTES_AGO = new Date(DateTimeUtils.currentTimeMillis() - TWO_MINUTES);
    /**
     * The default correlation identifier used to correlate commands and events.
     */
    public static final String CORRELATION_ID = "d0052460-93d2-11e3-baa8-0800200c9a66";
    /**
     * The default status correlation identifier used to correlate status commands and events.
     */
    public static final CorrelationToken CORRELATION_TOKEN = new CorrelationToken();
    /**
     * An String containing a FTP URI
     */
    public static final String FTP_LOCATION = "ftp://somewhere.com";
    /**
     * Five minutes in milliseconds.
     */
    public static final int FIVE_MINUTES = 300000;

    /**
     * The timestamp in milliseconds five minutes ago.
     * <p/>
     * Note: this is a constant. This means this timestamp isn't five minutes ago from the time you use but five minutes
     * ago from the time this class was loaded.
     */
    public static final Date FIVE_MINUTES_AGO = new Date(DateTimeUtils.currentTimeMillis() - FIVE_MINUTES);

    /**
     * The timestamp in milliseconds in five minutes.
     * <p/>
     * Note: this is a constant. This means this timestamp isn't in five minutes from the time you use but five minutes
     * ago from the time this class was loaded.
     */
    public static final Date IN_FIVE_MINUTES = new Date(DateTimeUtils.currentTimeMillis() + FIVE_MINUTES);

    /**
     * The default transaction number.
     */
    public static final int TRANSACTION_NUMBER = 123;

    /**
     * The default charging station vendor.
     */
    public static final String CHARGING_STATION_VENDOR = "ACME_CORP";

    /**
     * The default charging station model.
     */
    public static final String CHARGING_STATION_MODEL = "WIDGET";
    /**
     * The default map of boot notification attributes.
     */
    public static final Map<String, String> BOOT_NOTIFICATION_ATTRIBUTES = getAttributes(CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL);
    /**
     * The default charging station serial number.
     */
    public static final String CHARGING_STATION_SERIAL_NUMBER = "SN001";
    /**
     * The default charging station serial number.
     */
    public static final String CHARGE_BOX_SERIAL_NUMBER = "CB_SN001";
    /**
     * The default firmware version.
     */
    public static final String FIRMWARE_VERSION = "0.0.1";
    /**
     * The default ICCID.
     */
    public static final String ICCID = "iccid";
    /**
     * The default IMSI.
     */
    public static final String IMSI = "imsi";
    /**
     * The default meter type.
     */
    public static final String METER_TYPE = "meterType";
    /**
     * The default meter serial number.
     */
    public static final String METER_SERIAL_NUMBER = "SN002";
    /**
     * The default data transfer vendor.
     */
    public static final String DATA_TRANSFER_VENDOR = "ACME_CORP";
    /**
     * The default data transfer message identifier.
     */
    public static final String DATA_TRANSFER_MESSAGE_ID = "WIDGET_MESSAGE";
    /**
     * The default data transfer data.
     */
    public static final String DATA_TRANSFER_DATA = "['acme','widget']";
    /**
     * The default data transfer message.
     */
    public static final DataTransferMessage DATA_TRANSFER_MESSAGE = new DataTransferMessage(DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID, DATA_TRANSFER_DATA);
    /**
     * The default meter start value.
     */
    public static final int METER_START = 100;
    /**
     * The default meter stop value.
     */
    public static final int METER_STOP = 105;
    /**
     * The default charging station identifier.
     */
    public static final ChargingStationId CHARGING_STATION_ID = new ChargingStationId("CS-001");
    /**
     * The default reservation identifier.
     */
    public static final NumberedReservationId RESERVATION_ID = new NumberedReservationId(CHARGING_STATION_ID, PROTOCOL, 1);

    /**
     * The default transaction identifier.
     */
    public static final TransactionId TRANSACTION_ID = new NumberedTransactionId(CHARGING_STATION_ID, PROTOCOL, TRANSACTION_NUMBER);
    /**
     * The default, unknown charging station identifier.
     */
    public static final ChargingStationId UNKNOWN_CHARGING_STATION_ID = new ChargingStationId("DT-112");
    /**
     * Ip address of localhost.
     */
    public static final String LOCALHOST = "127.0.0.1";
    /**
     * The default unit.
     */
    public static final String UNIT = "Wh";

    /**
     * The default measureand.
     */
    public static final String MEASURAND = "Energy.Active.Import.Register";

    /**
     * The default EVSE identifier.
     */
    public static final EvseId EVSE_ID = new EvseId(1);

    /**
     * The default, unknown EVSE identifier.
     * <p/>
     * This identifier assumes that there are only two EVSEs (like the constant with the default set of EVSEs).
     */
    public static final EvseId UNKNOWN_EVSE_ID = new EvseId(3);

    /**
     * The default identifying token.
     */
    public static final TextualToken EMPTY_IDENTIFYING_TOKEN = new TextualToken("");

    /**
     * The default identifying token.
     */
    public static final TextualToken IDENTIFYING_TOKEN = new TextualToken("12345AF");

    /**
     * The default identifying token.
     */
    public static final TextualToken PARENT_IDENTIFYING_TOKEN = new TextualToken("23456CD");

    /**
     * Another valid identifying token.
     */
    public static final TextualToken ANOTHER_IDENTIFYING_TOKEN = new TextualToken("67890BE");

    /**
     * Invalid textual token.
     */
    public static final String INVALID_TEXTUAL_TOKEN = "INVALID";

    /**
     * The default invalid identifying token.
     */
    public static final TextualToken INVALID_IDENTIFYING_TOKEN = new TextualToken(INVALID_TEXTUAL_TOKEN);
    /**
     * The default identifying token, status INVALID.
     */
    public static final IdentifyingToken IDENTIFYING_TOKEN_INVALID = new TextualToken(INVALID_TEXTUAL_TOKEN, IdentifyingToken.AuthenticationStatus.INVALID);
    /**
     * The default identifying token, status ACCEPTED.
     */
    public static final IdentifyingToken IDENTIFYING_TOKEN_ACCEPTED = new TextualToken("ACCEPTED", IdentifyingToken.AuthenticationStatus.ACCEPTED, "MSP", "7007");
    /**
     * The default identifying token, status BLOCKED.
     */
    public static final IdentifyingToken IDENTIFYING_TOKEN_BLOCKED = new TextualToken("BLOCKED", IdentifyingToken.AuthenticationStatus.BLOCKED);
    /**
     * The default identifying token, status CONCURRENT_TX.
     */
    public static final IdentifyingToken IDENTIFYING_TOKEN_CONCURRENT_TX = new TextualToken("CONCURRENT_TX", IdentifyingToken.AuthenticationStatus.CONCURRENT_TX);
    /**
     * The default identifying token, status DELETED.
     */
    public static final IdentifyingToken IDENTIFYING_TOKEN_DELETED = new TextualToken("DELETED", IdentifyingToken.AuthenticationStatus.DELETED);
    /**
     * The default identifying token, status EXPIRED.
     */
    public static final IdentifyingToken IDENTIFYING_TOKEN_EXPIRED = new TextualToken("EXPIRED", IdentifyingToken.AuthenticationStatus.EXPIRED);
    /**
     * The default coordinates.
     */
    public static final Coordinates COORDINATES = new Coordinates(52.3702157, 4.895167899999933);

    /**
     * The default start transaction information.
     */
    public static final StartTransactionInfo START_TRANSACTION_INFO = new StartTransactionInfo(EVSE_ID, 1, new Date(), IDENTIFYING_TOKEN_ACCEPTED, Collections.<String, String>emptyMap());

    /**
     * The default address.
     */
    public static final Address ADDRESS = new Address("Address line 1", "Address line 2", "POSTALCODE", "City", "Region", "Country");

    /**
     * The default accessibility.
     */
    public static final Accessibility ACCESSIBILITY = Accessibility.PUBLIC;

    /**
     * The default update location.
     */
    public static final String UPDATE_LOCATION = "https://somewhere.nl";

    /**
     * Add-on id used to construct ADD_ON_IDENTITY.
     */
    public static final String ADD_ON_ID = "ID";

    /**
     * The default add-on identity.
     */
    public static final AddOnIdentity ADD_ON_IDENTITY = new TypeBasedAddOnIdentity("ADD-ON", ADD_ON_ID);

    /**
     * The default identity context with NullUserIdentity.
     * <p/>
     * Used to test add-ons which do not pass a user identity in the identity context.
     */
    public static final IdentityContext NULL_USER_IDENTITY_CONTEXT = new IdentityContext(ADD_ON_IDENTITY, new NullUserIdentity());

    /**
     * The default status notification.
     */
    public static final StatusNotification STATUS_NOTIFICATION = new StatusNotification(ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap());

    /**
     * The default diagnostics upload settings.
     */
    public static final DiagnosticsUploadSettings DIAGNOSTICS_UPLOAD_SETTINGS = new DiagnosticsUploadSettings("ftp://localhost", 3, 10, FIVE_MINUTES_AGO, IN_FIVE_MINUTES);

    /**
     * The default configuration item.
     */
    public static final ConfigurationItem CONFIGURATION_ITEM = new ConfigurationItem("key", "value");

    /**
     * The default configuration keys to retrieve from the charging station
     */
    public static final Set<String> CONFIGURATION_KEYS = new HashSet<>();

    /**
     * Specific configuration keys to retrieve from the charging station
     */
    public static final Set<String> CONFIGURATION_SPECIFIC_KEYS = ImmutableSet.<String>builder()
            .add("KVCBX_PROFILE")
            .build();

    /**
     * The default user identity.
     */
    public static final UserIdentity USER_IDENTITY = new SimpleUserIdentity("USER-ID");

    /**
     * The default identity context.
     */
    public static final IdentityContext IDENTITY_CONTEXT = new IdentityContext(ADD_ON_IDENTITY, USER_IDENTITY);

    /**
     * The default root user identity.
     */
    public static final UserIdentity ROOT_USER_IDENTITY = new SimpleUserIdentity("root");

    /**
     * The default root identity context.
     */
    public static final IdentityContext ROOT_IDENTITY_CONTEXT = new IdentityContext(ADD_ON_IDENTITY, ROOT_USER_IDENTITY);

    /**
     * The default user identities which should have all permissions on created charging stations.
     */
    public static final Set<UserIdentity> USER_IDENTITIES_WITH_ALL_PERMISSIONS = ImmutableSet.<UserIdentity>builder()
            .add(ROOT_USER_IDENTITY)
            .build();

    /**
     * The default list of identifying tokens.
     */
    public static final ImmutableSet<IdentifyingToken> IDENTIFYING_TOKENS = ImmutableSet.<IdentifyingToken>builder()
            .add(IDENTIFYING_TOKEN)
            .add(ANOTHER_IDENTIFYING_TOKEN)
            .build();

    /**
     * The default authorization list version.
     */
    public static final int LIST_VERSION = 1;

    /**
     * The default list of meter value attributes.
     */
    public static final ImmutableMap<String, String> METER_VALUE_ATTRIBUTES = ImmutableMap.<String, String>builder()
            .put("context", "Sample.Clock")
            .put("format", "Raw")
            .put("measurand", "Energy.Active.Export.Register")
            .put("location", "Inlet")
            .put("unit", "kWh")
            .build();

    /**
     * The default list of meter values.
     */
    public static final ImmutableList<MeterValue> METER_VALUES = ImmutableList.<MeterValue>builder()
            .add(new MeterValue(FIVE_MINUTES_AGO, Integer.toString(METER_START)))
            .add(new MeterValue(TWO_MINUTES_AGO, Integer.toString(METER_STOP)))
            .build();

    /**
     * The default map of configuration items.
     */
    public static final Set<ConfigurationItem> CONFIGURATION_ITEMS = ImmutableSet.<ConfigurationItem>builder()
            .add(new ConfigurationItem("io.motown.sockets.amount", "2"))
            .add(new ConfigurationItem("io.motown.random.config.item", "true"))
            .add(new ConfigurationItem("io.motown.another.random.config.item", "12"))
            .add(new ConfigurationItem("io.motown.yet.another.one", "blue"))
            .build();

    /**
     * The default map of requested configuration keys.
     */
    public static final Set<String> REQUESTED_CONFIGURATION_KEYS = ImmutableSet.<String>builder()
            .add("io.motown.sockets.amount")
            .add("io.motown.random.config.item")
            .add("io.motown.another.random.config.item")
            .add("io.motown.yet.another.one")
            .build();

    /**
     * A default list of connectors.
     */
    public static final List<Connector> CONNECTORS = ImmutableList.<Connector>builder()
            .add(new Connector(MAX_AMP, PHASE, VOLTAGE, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_2))
            .add(new Connector(MAX_AMP, PHASE, VOLTAGE, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_1))
            .build();

    /**
     * A default set of EVSEs with connectors.
     */
    public static final Set<Evse> EVSES = ImmutableSet.<Evse>builder()
            .add(new Evse(new EvseId(1), CONNECTORS))
            .add(new Evse(new EvseId(2), CONNECTORS))
            .build();

    /**
     * The list of events which leads to a created charging station.
     */
    public static final List<Object> CREATED_CHARGING_STATION = ImmutableList.builder()
            .add(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT))
            .build();

    /**
     * The list of events which leads to a created, but unconfigured charging station which has booted.
     */
    public static final List<Object> UNCONFIGURED_CHARGING_STATION = ImmutableList.builder()
            .addAll(CREATED_CHARGING_STATION)
            .add(new UnconfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT))
            .build();

    /**
     * The list of events which leads to a created and accepted, but unconfigured charging station which has booted.
     */
    public static final List<Object> UNCONFIGURED_ACCEPTED_CHARGING_STATION = ImmutableList.builder()
            .addAll(UNCONFIGURED_CHARGING_STATION)
            .add(new ChargingStationAcceptedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
            .build();

    /**
     * The list of events which leads to a created, accepted, and configured charging station which has booted.
     */
    public static final List<Object> CHARGING_STATION = ImmutableList.builder()
            .add(new ConfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT))
            .add(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT))
            .add(new ChargingStationAcceptedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
            .add(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, NULL_USER_IDENTITY_CONTEXT))
            .build();

    /**
     * The list of events which leads to a created, accepted, configured, and reservable charging station which has booted.
     */
    public static final List<Object> RESERVABLE_CHARGING_STATION = ImmutableList.builder()
            .add(new ConfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT))
            .add(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT))
            .add(new ChargingStationAcceptedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
            .add(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, NULL_USER_IDENTITY_CONTEXT))
            .add(new ChargingStationMadeReservableEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
            .build();

    private static final int OPENING_TIME_START_HOUR = 12;
    private static final int OPENING_TIME_START_MINUTES = 35;
    private static final int OPENING_TIME_STOP_HOUR = 15;
    private static final int OPENING_TIME_STOP_MINUTES = 13;

    /**
     * The default opening times.
     */
    public static final Set<OpeningTime> OPENING_TIMES = ImmutableSet.<OpeningTime>builder()
            .add(new OpeningTime(Day.MONDAY, new TimeOfDay(OPENING_TIME_START_HOUR, OPENING_TIME_START_MINUTES), new TimeOfDay(OPENING_TIME_STOP_HOUR, OPENING_TIME_STOP_MINUTES)))
            .build();
    /**
     * A default opening times.
     */
    public static final OpeningTime OPENING_TIME = new OpeningTime(Day.MONDAY, new TimeOfDay(OPENING_TIME_START_HOUR, OPENING_TIME_START_MINUTES), new TimeOfDay(OPENING_TIME_STOP_HOUR, OPENING_TIME_STOP_MINUTES));

    /**
     * Default page number for REST api
     */
    public static final int OFFSET = 0;

    /**
     * Default results per page for REST api
     */
    public static final int LIMIT = 10;

    /**
     * Private no-arg constructor to prevent instantiation of this utility class.
     */
    private ChargingStationTestUtils() {
    }

    /**
     * Gets an immutable map of attributes with the given vendor and model.
     *
     * @param vendor the given charging station vendor.
     * @param model  the given charging station model.
     * @return an immutable map of attributes.
     */
    public static Map<String, String> getAttributes(String vendor, String model) {
        return ImmutableMap.<String, String>builder()
                .put("vendorId", vendor)
                .put("model", model)
                .build();
    }

    /**
     * Gets a list with the given number of default connectors.
     *
     * @param numberOfConnectors the number of connectors.
     * @return a list with the given number of default connectors.
     * @throws IllegalArgumentException if the number of connectors is lower than 1.
     */
    public static List<Connector> getConnectors(int numberOfConnectors) {
        checkArgument(numberOfConnectors > 0);

        List<Connector> connectors = new ArrayList<>(numberOfConnectors);
        for (int i = 0; i < numberOfConnectors; i++) {
            connectors.add(CONNECTOR);
        }

        return connectors;
    }

    /**
     * Gets a default EVSE with the given identifier.
     *
     * @param identifier the identifier for the EVSE.
     * @return a default EVSE with the given identifier.
     */
    public static Evse getEvse(int identifier) {
        return new Evse(new EvseId(identifier), getConnectors(NUMBER_OF_CONNECTORS));
    }

    /**
     * Gets a set with the given number of default EVSEs.
     *
     * @param numberOfEvses the number of EVSEs.
     * @return a set with the given number of default EVSEs.
     */
    public static Set<Evse> getEvses(int numberOfEvses) {
        checkArgument(numberOfEvses > 0);

        Set<Evse> evses = new HashSet<>(numberOfEvses);
        for (int i = 0; i < numberOfEvses; i++) {
            evses.add(getEvse(i));
        }

        return evses;
    }
}
