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
package io.motown.domain.api.chargingstation;

public enum ConnectorType {

    //TODO decide what to use here. - Mark van den Bergh, Februari 5th 2014

    // OCPP 2.0
    UNSPECIFIED,
    C_TYPE_1,
    C_TYPE_2,
    S_TYPE_2,
    S_TYPE_3,
    C_CCS_1,
    C_CCS_2,
    C_G105,
    TESLA,
    CEE_7_7,
    S_BS1361,
    S_309_3P_16A,
    S_309_1P_16A,
    S_309_3P_32A,
    S_309_1P_32A,
    W_INDUCTIVE,
    W_RESONANT,
    OTHER_1_PH_MAX_16A,
    OTHER_3_PH,
    
    // VAS, comment shows OCPP 2.0 counterpart
    Unspecified,                    // UNSPECIFIED
    Small_Paddle_Inductive,         // W_INDUCTIVE
    Large_Paddle_Inductive,         // W_INDUCTIVE
    Avcon_Connector,
    Tesla_Connector,                // TESLA
    Sae_J1772_Yazaki,
    Nema_520,
    Tepco_Cha_Me_Do,                // C_G105
    Iec_621962_Type_1_Yazaki,       // C_TYPE_1 (captive enabled)
    Iec_621962_Type_2_Mennekes,     // C_TYPE_2 (captive enabled), S_TYPE_2
    Iec_621962_Type_3_Scame,        // S_TYPE_3
    _60309Industrial2PDc,
    _60309IndustrialPneAc,          // S_309_1P_16A or S_309_1P_32A ?
    _60309Industrial3PEAc,          // S_309_3P_16A or S_309_3P_32A ?
    _60309Industrial3PENAc,         // S_309_3P_16A or S_309_3P_32A ?
    DomesticPlugTypeANema115Unpolarised,
    DomesticPlugTypeANema115Polarised,
    DomesticPlugTypeAJisC8303ClassII,
    DomesticPlugTypeBNema515,
    DomesticPlugTypeBNema520,
    DomesticPlugTypeBJis8393ClassI,
    DomesticPlugTypeCCee716Europlug,
    DomesticPlugTypeCCee717,
    DomesticPlugTypeCGost7396C1,
    DomesticPlugTypeDBs5462Pin,
    DomesticPlugTypeDBs5463Pin,
    DomesticPlugTypeECee75,         // CEE_7_7
    DomesticPlugTypeFCee74Schuko,   // CEE_7_7
    DomesticPlugTypeEFCee77,        // CEE_7_7
    DomesticPlugTypeGBs1363Is401411Ms58,
    DomesticPlugTypeHSi32,
    DomesticPlugTypeIAsNzs3112,
    DomesticPlugTypeICpcsCcc,
    DomesticPlugTypeIIram2073,
    DomesticPlugTypeJSev1011,
    DomesticPlugTypeKSection1072D1,
    DomesticPlugTypeKThailandTis1662549,
    DomesticPlugTypeLCei2316VII,
    DomesticPlugTypeIec6090612Pin,
    DomesticPlugTypeIec6090613Pin

}
