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

/**
 * Using the OCPP connector types as OCPP is the standard in communication protocol.
 */
public enum ConnectorType {

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
    OTHER_3_PH

}
