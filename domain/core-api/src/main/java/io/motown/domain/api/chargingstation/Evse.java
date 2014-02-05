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

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Evse {

    public final static EvseId ALL = new EvseId(0);

    private EvseId evseId;

    private List<Connector> connectors;

    public Evse(EvseId evseId, List<Connector> connectors) {
        this.evseId = checkNotNull(evseId);
        this.connectors = checkNotNull(connectors);
    }

    public EvseId getEvseId() {
        return evseId;
    }

    public List<Connector> getConnectors() {
        return connectors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Evse evse = (Evse) o;

        if (!connectors.equals(evse.connectors)) return false;
        if (!evseId.equals(evse.evseId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = evseId.hashCode();
        result = 31 * result + connectors.hashCode();
        return result;
    }
}
