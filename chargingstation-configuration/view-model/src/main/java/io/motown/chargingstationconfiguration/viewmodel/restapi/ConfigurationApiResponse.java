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
package io.motown.chargingstationconfiguration.viewmodel.restapi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The response that the REST api returns for the {@code GET} requests for the manufacturers and charging stations.
 * The returned JSON consists of a metadata map and a list of records.
 *
 * @param <T> The type of record which is wrapped in the response. This can be either {@link io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer} or {@link io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType}.
 */
public final class ConfigurationApiResponse<T> {
    private final Map<String, Object> metadata;
    private final List<T> records;

    /**
     * Construct a response to send to the client.
     *
     * @param metadata  The metadata of the response.
     * @param records   A list of records in the response.
     */
    public ConfigurationApiResponse(Map<String, Object> metadata, List<T> records) {
        this.metadata = ImmutableMap.copyOf(checkNotNull(metadata));
        this.records = ImmutableList.copyOf(checkNotNull(records));
    }

    /**
     * Gets the metadata of the response.
     *
     * @return a {@code Map} of metadata.
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * Gets the records from the response.
     *
     * @return a {@code List} of records.
     */
    public List<T> getRecords() {
        return records;
    }
}
