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
package io.motown.mobieurope.destination.persistence.entities;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"pmsIdentifier"})
)
public class SourceEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pmsIdentifier;

    private String sourceEndpointUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPmsIdentifier() {
        return pmsIdentifier;
    }

    public void setPmsIdentifier(String pmsIdentifier) {
        this.pmsIdentifier = pmsIdentifier;
    }

    public String getSourceEndpointUrl() {
        return sourceEndpointUrl;
    }

    public void setSourceEndpointUrl(String sourceEndpointUrl) {
        this.sourceEndpointUrl = sourceEndpointUrl;
    }
}
