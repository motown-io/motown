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

public class Connector {

    public final static int ALL = 0;

    private int connectorId;
    private String connectorType;  // should be enum ?
    private int maxAmp;

    public Connector(int connectorId, String connectorType, int maxAmp) {
        this.connectorId = connectorId;
        this.connectorType = connectorType;
        this.maxAmp = maxAmp;
    }

    public int getConnectorId() {
        return this.connectorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connector connector = (Connector) o;

        if (connectorId != connector.connectorId) return false;
        if (maxAmp != connector.maxAmp) return false;
        if (connectorType != null ? !connectorType.equals(connector.connectorType) : connector.connectorType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = connectorId;
        result = 31 * result + (connectorType != null ? connectorType.hashCode() : 0);
        result = 31 * result + maxAmp;
        return result;
    }

    @Override
    public String toString() {
        return String.format("Connector(id = %d, type = %s, maxAmp = %d)", connectorId, connectorType, maxAmp);
    }

}
