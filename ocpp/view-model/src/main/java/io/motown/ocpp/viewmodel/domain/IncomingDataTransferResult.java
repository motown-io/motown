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
package io.motown.ocpp.viewmodel.domain;

import io.motown.domain.api.chargingstation.IncomingDataTransferResultStatus;

/**
 * Contains values which reflect the result of an incoming datatransfer command.
 */
public class IncomingDataTransferResult {
    /**
     * The optional response data
     */
    private String data;

    /**
     * The status of the datatransfer-processing
     */
    private IncomingDataTransferResultStatus status;

    /**
     * @param data       the optional response data
     * @param status     the status of the datatransfer-processing
     */
    public IncomingDataTransferResult(String data, IncomingDataTransferResultStatus status) {
        this.data = data;
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public IncomingDataTransferResultStatus getStatus() {
        return status;
    }
}
