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
package io.motown.vas.viewmodel;

/**
 * Charge Point Type as per CDR spec
 */
public enum CdrChargePointType {

    UNSPECIFIED(0),   // Unspecified
    AC_LT_37KW(1),    // AC < 3,7kW
    AC_37KW(2),       // AC 3,7kW
    AC_11KW(3),       // AC 11kW
    AC_22KW(4),       // AC 22kW
    DC_50KW(5);       // DC 50kW

    private final Integer value;

    CdrChargePointType(Integer v) {
        value = v;
    }

    public Integer value() {
        return value;
    }

    public static CdrChargePointType fromValue(Integer v) {
        for (CdrChargePointType c : values()) {
            if (c.value == v) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

}
