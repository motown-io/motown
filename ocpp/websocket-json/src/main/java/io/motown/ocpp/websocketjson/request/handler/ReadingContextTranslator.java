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

import io.motown.domain.api.chargingstation.ReadingContext;

import javax.annotation.Nullable;

/**
 * Translator which translates a {@code String} to a {@code ReadingContext}.
 */
class ReadingContextTranslator implements Translator<ReadingContext> {

    private final String readingContext;

    /**
     * Creates a {@code ReadingContextTranslator}.
     *
     * @param readingContext the reading context to translate.
     */
    public ReadingContextTranslator(@Nullable String readingContext) {
        this.readingContext = readingContext;
    }

    /**
     * {@inheritDoc}
     */
    public ReadingContext translate() {
        if (this.readingContext == null || this.readingContext.isEmpty()) {
            return ReadingContext.PERIODIC_SAMPLE;
        }

        switch (this.readingContext) {
            case "Interruption.Begin":
                return ReadingContext.BEGIN_INTERRUPTION;
            case "Interruption.End":
                return ReadingContext.END_INTERRUPTION;
            case "Sample.Clock":
                return ReadingContext.CLOCK_SAMPLE;
            case "Sample.Periodic":
                return ReadingContext.PERIODIC_SAMPLE;
            case "Transaction.Begin":
                return ReadingContext.BEGIN_TRANSACTION;
            case "Transaction.End":
                return ReadingContext.END_TRANSACTION;
            default:
                throw new AssertionError(String.format("Unknown value for ReadingContext: '%s'", this.readingContext));
        }
    }
}
