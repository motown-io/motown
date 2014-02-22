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

import static com.google.common.base.Preconditions.checkNotNull;

public enum Accessibility {
    PUBLIC("Public"),
    PAYING("Paying"),
    PRIVATE("Private");

    private final String value;

    private Accessibility(String value) {
        this.value = value;
    }

    public static Accessibility fromValue(String value) {
        for (Accessibility accessibility : Accessibility.values()) {
            if (accessibility.value.equalsIgnoreCase(checkNotNull(value))) {
                return accessibility;
            }
        }
        throw new IllegalArgumentException(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
