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
package io.motown.ocpp.v15.soap.centralsystem;

import io.motown.domain.api.chargingstation.ReadingContext;

import javax.annotation.Nullable;

/**
 * Adapter which translates a {@code io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext} to a {@code ReadingContext}.
 */
class ReadingContextTranslator implements Translator<ReadingContext> {

    private final io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext readingContext;

    /**
     * Creates a {@code ReadingContextTranslationAdapter}.
     *
     * @param readingContext the reading context to translate.
     */
    public ReadingContextTranslator(@Nullable io.motown.ocpp.v15.soap.centralsystem.schema.ReadingContext readingContext) {
        this.readingContext = readingContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReadingContext translate() {
        if (this.readingContext == null) {
            // In OCPP 1.5, PERIODIC_SAMPLE is the default value.
            return ReadingContext.PERIODIC_SAMPLE;
        }

        ReadingContext result;

        switch (this.readingContext) {
            case INTERRUPTION_BEGIN:
                result = ReadingContext.BEGIN_INTERRUPTION;
                break;
            case INTERRUPTION_END:
                result = ReadingContext.END_INTERRUPTION;
                break;
            case SAMPLE_CLOCK:
                result = ReadingContext.CLOCK_SAMPLE;
                break;
            case SAMPLE_PERIODIC:
                result = ReadingContext.PERIODIC_SAMPLE;
                break;
            case TRANSACTION_BEGIN:
                result = ReadingContext.BEGIN_TRANSACTION;
                break;
            case TRANSACTION_END:
                result = ReadingContext.END_TRANSACTION;
                break;
            default:
                throw new AssertionError(String.format("Unknown value for ReadingContext: '%s'", readingContext));
        }

        return result;
    }

}
