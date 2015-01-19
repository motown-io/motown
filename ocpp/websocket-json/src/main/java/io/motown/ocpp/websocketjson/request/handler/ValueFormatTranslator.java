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
package io.motown.ocpp.websocketjson.request.handler;

import io.motown.domain.api.chargingstation.ValueFormat;

/**
 * Translator which translates a {@code String} to a {@code ValueFormat}.
 */
class ValueFormatTranslator implements Translator<ValueFormat> {

    private final String valueFormat;

    /**
     * Creates a {@code ValueFormatTranslator}.
     *
     * @param valueFormat the value format to translate.
     */
    public ValueFormatTranslator(String valueFormat) {
        this.valueFormat = valueFormat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueFormat translate() {
        if (this.valueFormat == null || this.valueFormat.isEmpty()) {
            return ValueFormat.RAW;
        }

        switch (this.valueFormat) {
            case "Raw":
                return ValueFormat.RAW;
            case "SignedData":
                return ValueFormat.SIGNED_DATA;
            default:
                throw new AssertionError(String.format("Unknown value for ValueFormat: '%s'", this.valueFormat));
        }
    }
}
