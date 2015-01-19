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
 * Context indicating when a {@code MeterValue} was measured.
 */
public enum ReadingContext {
    /**
     * Value taken at start of interruption.
     */
    BEGIN_INTERRUPTION,
    /**
     * Value taken when resuming after interruption.
     */
    END_INTERRUPTION,
    /**
     * Value taken at clock aligned interval.
     */
    CLOCK_SAMPLE,
    /**
     * Value taken as periodic sample relative to start time of transaction.
     */
    PERIODIC_SAMPLE,
    /**
     * Value taken at end of transaction.
     */
    BEGIN_TRANSACTION,
    /**
     * Value taken at start of transaction.
     */
    END_TRANSACTION
}
