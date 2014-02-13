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
package io.motown.vas.viewmodel.model;

import com.google.common.collect.ImmutableMap;
import io.motown.domain.api.chargingstation.ConnectorType;

import java.util.Map;

public enum VasConnectorType {

    UNSPECIFIED("Unspecified"),

    /**
     *             Small paddle inductive.
     */
    SMALL_PADDLE_INDUCTIVE("SmallPaddleInductive"),

    /**
     *             Large paddle inductive.
     */
    LARGE_PADDLE_INDUCTIVE("LargePaddleInductive"),

    /**
     *             AVCON connector.
     */
    AVCON_CONNECTOR("AvconConnector"),

    /**
     *             Tesla connector.
     */
    TESLA_CONNECTOR("TeslaConnector"),

    /**
     *             SAE J1772 (Yazaki).
     */
    SAE_J_1772_YAZAKI("SaeJ1772Yazaki"),

    /**
     *             Nema 5-20
     */
    NEMA_520("Nema520"),

    /**
     *             TEPCO CHAdeMO.
     */
    TEPCO_CHA_DE_MO("TepcoChaDeMO"),

    /**
     *             IEC 62196-2 Type 1 (Yazaki).
     */
    IEC_621962_TYPE_1_YAZAKI("Iec621962Type1Yazaki"),

    /**
     *             IEC 62196-2 Type 2 (Mennekes).
     */
    IEC_621962_TYPE_2_MENNEKES("Iec621962Type2Mennekes"),

    /**
     *             IEC 62196-2 Type 3 (SCAME).
     */
    IEC_621962_TYPE_3_SCAME("Iec621962Type3Scame"),

    /**
     * [Converted from 60309Industrial2PDc to IEC_60309_INDUSTRIAL_2P_DC] 60309 - industrial 2P (DC).
     */
    IEC_60309_INDUSTRIAL_2P_DC("60309Industrial2PDc"),

    /**
     * [Converted from 60309IndustrialPneAc to IEC_60309INDUSTRIAL_P_N_E_AC] 60309 - industrial P + N int  + E (AC).
     */
    IEC_60309INDUSTRIAL_P_N_E_AC("60309IndustrialPneAc"),

    /**
     * [Converted from 60309Industrial3PEAc to IEC_60309_INDUSTRIAL_3P_E_AC] 60309 - industrial 3P + E (AC).
     */
    IEC_60309_INDUSTRIAL_3P_E_AC("60309Industrial3PEAc"),

    /**
     * [Converted from 60309Industrial3PENAc to IEC_60309_INDUSTRIAL_3P_E_N_AC] 60309 - industrial 3P + E + N (AC).
     */
    IEC_60309_INDUSTRIAL_3P_E_N_AC("60309Industrial3PENAc"),

    /**
     *             Domestic plug type A (NEMA 1-15 unpolarised).
     */
    DOMESTIC_PLUG_TYPE_A_NEMA_115_UNPOLARISED("DomesticPlugTypeANema115Unpolarised"),

    /**
     *             Domestic plug type A (NEMA 1-15 polarised).
     */
    DOMESTIC_PLUG_TYPE_A_NEMA_115_POLARISED("DomesticPlugTypeANema115Polarised"),

    /**
     *             Domestic plug type A (JIS C 8303, Class II).
     */
    DOMESTIC_PLUG_TYPE_A_JIS_C_8303_CLASS_II("DomesticPlugTypeAJisC8303ClassII"),

    /**
     *             Domestic plug type B (NEMA 5-15).
     */
    DOMESTIC_PLUG_TYPE_B_NEMA_515("DomesticPlugTypeBNema515"),

    /**
     *             Domestic plug type B (NEMA 5-20).
     */
    DOMESTIC_PLUG_TYPE_B_NEMA_520("DomesticPlugTypeBNema520"),

    /**
     *             Domestic plug type B (JIS C 8393, Class I).
     */
    DOMESTIC_PLUG_TYPE_B_JIS_8393_CLASS_I("DomesticPlugTypeBJis8393ClassI"),

    /**
     *             Domestic plug type C (CEE 7/16 Europlug).
     */
    DOMESTIC_PLUG_TYPE_C_CEE_716_EUROPLUG("DomesticPlugTypeCCee716Europlug"),

    /**
     *             Domestic plug type C (CEE 7/17).
     */
    DOMESTIC_PLUG_TYPE_C_CEE_717("DomesticPlugTypeCCee717"),

    /**
     *             Domestic plug type C (GOST 7396 C 1).
     */
    DOMESTIC_PLUG_TYPE_C_GOST_7396_C_1("DomesticPlugTypeCGost7396C1"),

    /**
     *             Domestic plug type D (BS 546, 2 pin).
     */
    DOMESTIC_PLUG_TYPE_D_BS_5462_PIN("DomesticPlugTypeDBs5462Pin"),

    /**
     *             Domestic plug type D (BS 546, 3 pin).
     */
    DOMESTIC_PLUG_TYPE_D_BS_5463_PIN("DomesticPlugTypeDBs5463Pin"),

    /**
     *             Domestic plug type E (CEE 7/5).
     */
    DOMESTIC_PLUG_TYPE_E_CEE_75("DomesticPlugTypeECee75"),

    /**
     *             Domestic plug type F (CEE 7/4 Schuko).
     */
    DOMESTIC_PLUG_TYPE_F_CEE_74_SCHUKO("DomesticPlugTypeFCee74Schuko"),

    /**
     *             Domestic plug type E+F (CEE 7/7).
     */
    DOMESTIC_PLUG_TYPE_EF_CEE_77("DomesticPlugTypeEFCee77"),
    DOMESTIC_PLUG_TYPE_G_BS_1363_IS_401411_MS_58("DomesticPlugTypeGBs1363Is401411Ms58"),

    /**
     *             Domestic plug type H (SI 32).
     */
    DOMESTIC_PLUG_TYPE_H_SI_32("DomesticPlugTypeHSi32"),

    /**
     *             Domestic plug type I (AS/NZS 3112).
     */
    DOMESTIC_PLUG_TYPE_I_AS_NZS_3112("DomesticPlugTypeIAsNzs3112"),

    /**
     *             Domestic plug type I (CPCS-CCC).
     */
    DOMESTIC_PLUG_TYPE_I_CPCS_CCC("DomesticPlugTypeICpcsCcc"),

    /**
     *             Domestic plug type I (IRAM 2073).
     */
    DOMESTIC_PLUG_TYPE_I_IRAM_2073("DomesticPlugTypeIIram2073"),

    /**
     *             Domestic plug type J (SEV 1011) .
     */
    DOMESTIC_PLUG_TYPE_J_SEV_1011("DomesticPlugTypeJSev1011"),

    /**
     *             Domestic plug type K (Section 107-2-D1).
     */
    DOMESTIC_PLUG_TYPE_K_SECTION_1072_D_1("DomesticPlugTypeKSection1072D1"),

    /**
     *             Domestic plug type K (Thailand TIS 166 - 2549).
     */
    DOMESTIC_PLUG_TYPE_K_THAILAND_TIS_1662549("DomesticPlugTypeKThailandTis1662549"),

    /**
     *             Domestic plug type L (CEI 23-16/VII).
     */
    DOMESTIC_PLUG_TYPE_L_CEI_2316_VII("DomesticPlugTypeLCei2316VII"),

    /**
     *             Domestic plug type IEC 60906-1 (2 pin).
     */
    DOMESTIC_PLUG_TYPE_IEC_6090612_PIN("DomesticPlugTypeIec6090612Pin"),

    /**
     *             Domestic plug type IEC 60906-1 (3 pin).
     */
    DOMESTIC_PLUG_TYPE_IEC_6090613_PIN("DomesticPlugTypeIec6090613Pin");

    private final String value;

    private static final Map<ConnectorType, VasConnectorType> MAPPING = ImmutableMap.<ConnectorType, VasConnectorType>builder()
            .put(ConnectorType.W_INDUCTIVE, SMALL_PADDLE_INDUCTIVE)
            .put(ConnectorType.TESLA, TESLA_CONNECTOR)
            .put(ConnectorType.C_G105, TEPCO_CHA_DE_MO)
            .put(ConnectorType.C_TYPE_1, IEC_621962_TYPE_1_YAZAKI)
            .put(ConnectorType.C_TYPE_2, IEC_621962_TYPE_2_MENNEKES)
            .put(ConnectorType.S_TYPE_3, IEC_621962_TYPE_3_SCAME)
            .put(ConnectorType.S_309_1P_16A, IEC_60309INDUSTRIAL_P_N_E_AC)
            .put(ConnectorType.S_309_1P_32A, IEC_60309INDUSTRIAL_P_N_E_AC)
            // S_309_3P_16A could also be IEC_60309_INDUSTRIAL_3P_E_N_AC?
            .put(ConnectorType.S_309_3P_16A, IEC_60309_INDUSTRIAL_3P_E_AC)
            // S_309_3P_32A could also be IEC_60309_INDUSTRIAL_3P_E_N_AC?
            .put(ConnectorType.S_309_3P_32A, IEC_60309_INDUSTRIAL_3P_E_AC)
            // CEE_7_7 could also be DomesticPlugTypeECee75, DomesticPlugTypeFCee74Schuko?
            .put(ConnectorType.CEE_7_7, DOMESTIC_PLUG_TYPE_EF_CEE_77)
            .put(ConnectorType.UNSPECIFIED, UNSPECIFIED)
            .build();

    VasConnectorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VasConnectorType fromValue(String v) {
        for (VasConnectorType c: VasConnectorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    /**
     * Maps a ConnectorType (based on OCPP) to a VAS representation. The connector types that cannot be mapped will
     * result in UNSPECIFIED.
     *
     * @param connectorType    connector type
     * @return the corresponding vas connector type, or UNSPECIFIED if no mapping can be made.
     */
    public static VasConnectorType fromConnectorType(ConnectorType connectorType) {
        VasConnectorType vasConnectorType = MAPPING.get(connectorType);

        if (vasConnectorType == null) {
            vasConnectorType = UNSPECIFIED;
        }

        return vasConnectorType;
    }

}
