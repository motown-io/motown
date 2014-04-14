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
package io.motown.vas.v10.soap;

import com.google.common.collect.ImmutableSet;
import io.motown.vas.v10.soap.schema.ChargePoint;
import io.motown.vas.viewmodel.model.*;

import javax.persistence.EntityManager;
import java.util.List;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VasSoapTestUtils {

    public static final String DELIVERY_ADDRESS = "http://localhost/";
    public static final String OTHER_DELIVERY_ADDRESS = "http://127.0.0.1/";

    public static final String SUBSCRIBER_IDENTITY = "MOTOWN_SUBSCRIBER";

    private static final String OPERATOR = "MOTOWN";

    public static void deleteFromDatabase(EntityManager entityManager, Class jpaEntityClass) {
        entityManager.getTransaction().begin();
        List resultList = entityManager.createQuery("SELECT entity FROM " + jpaEntityClass.getName() + " as entity").getResultList();
        for (Object obj : resultList) {
            entityManager.remove(obj);
        }
        entityManager.getTransaction().commit();
    }

    public static ChargingStation getConfiguredAndFilledChargingStation() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());

        //TODO implement
//        cs.setAccessibility();
        cs.setAddress(ADDRESS.getAddressLine1());
        cs.setChargeMode(ChargeMode.IEC_61851_MODE_3);

        ImmutableSet<ChargingCapability> chargingCapabilities = ImmutableSet.<ChargingCapability>builder()
                .add(ChargingCapability.V_120V_1_PHASE_10A)
                .add(ChargingCapability.V_240V_3_PHASE_32A)
                .add(ChargingCapability.V_480V_3_PHASE_32A)
                .build();
        cs.setChargingCapabilities(chargingCapabilities);

        cs.setChargingStationId(CHARGING_STATION_ID.getId());
        cs.setCity(ADDRESS.getCity());
        cs.setConfigured(true);

        ImmutableSet<ConnectorType> connectorTypes = ImmutableSet.<ConnectorType>builder()
                .add(ConnectorType.TESLA_CONNECTOR)
                .add(ConnectorType.TEPCO_CHA_DE_MO)
                .add(ConnectorType.IEC_60309_INDUSTRIAL_3P_E_N_AC)
                .build();
        cs.setConnectorTypes(connectorTypes);

        cs.setCountry(ADDRESS.getCountry());

        ImmutableSet<Evse> evses = ImmutableSet.<Evse>builder()
                .add(new Evse(1, ComponentStatus.AVAILABLE))
                .add(new Evse(2, ComponentStatus.OCCUPIED))
                .build();
        cs.setEvses(evses);

        cs.setHasFixedCable(false);
        cs.setLatitude(COORDINATES.getLatitude());
        cs.setLongitude(COORDINATES.getLongitude());

        //TODO openingtimes
        //cs.setOpeningTimes();

        cs.setOperator(OPERATOR);
        cs.setPostalCode(ADDRESS.getPostalCode());
        cs.setRegion(ADDRESS.getRegion());
        cs.setRegistered(true);
        cs.setReservable(false);
        cs.setState(ComponentStatus.AVAILABLE);

        return cs;
    }

    public static void compareChargingStationToVasRepresentation(ChargingStation cs, ChargePoint cp) {
        VasConversionService service = new VasConversionService();

        assertEquals(cs.getAddress(), cp.getAddress());
        assertEquals(service.getVasChargingMode(cs.getChargeMode()), cp.getChargingMode());
        assertEquals(cs.getCity(), cp.getCity());
        assertEquals(cs.getNumberOfEvses(), cp.getConnectors());
        assertEquals(cs.getNumberOfFreeEvses(), cp.getConnectorsFree());
        assertEquals(cs.getCountry(), cp.getCountry());
        assertEquals(cs.isHasFixedCable(), cp.isHasFixedCable());
        assertEquals(cs.isReservable(), cp.isIsReservable());
        assertEquals(cs.getOperator(), cp.getOperator());
        assertEquals(cs.getPostalCode(), cp.getPostalCode());
        assertEquals(cs.getRegion(), cp.getRegion());
        assertEquals(service.getStatus(cs), cp.getStatus());
        assertEquals(cs.getChargingStationId(), cp.getUid());

        assertEquals(cs.getLatitude(), cp.getCoordinates().getLatitude());
        assertEquals(cs.getLongitude(), cp.getCoordinates().getLongitude());

        // TODO implement
//        assertEquals(cs..., cp.getPublic());

        for (ConnectorType connectorType : cs.getConnectorTypes()) {
            if (!connectorType.equals(ConnectorType.TEPCO_CHA_DE_MO)) {
                assertTrue(cp.getConnectorTypes().contains(io.motown.vas.v10.soap.schema.ConnectorType.fromValue(connectorType.value())));
            } else {
                assertTrue(cp.getConnectorTypes().contains(io.motown.vas.v10.soap.schema.ConnectorType.TEPCO_CHA_ME_DO));
            }
        }

        for (ChargingCapability capability : cs.getChargingCapabilities()) {
            assertTrue(cp.getChargingCapabilities().contains(io.motown.vas.v10.soap.schema.ChargingCapability.fromValue(capability.value())));
        }

        // TODO openingTimes
    }

}
