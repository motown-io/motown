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
package io.motown.vas.viewmodel.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Evse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Indication of the logical position of the EVSE in a charging station.
     */
    @Column(nullable = false)
    private Integer position;

    /**
     * The current state of the EVSE.
     */
    @Column(nullable = false)
    private State state;

    private Evse() {
        // Private no-arg constructor for Hibernate.
    }

    public Evse(Integer position, State state) {
        this.position = position;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public Integer getPosition() {
        return position;
    }

    public State getState() {
        return state;
    }

    @Override
    public int hashCode() {
        // field 'id' is left out of hash on purpose because comparisons do not care about the generated id
        return Objects.hash(position, state);
    }

    @Override
    public boolean equals(Object obj) {
        // field 'id' is left out of equals on purpose because comparisons do not care about the generated id
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Evse other = (Evse) obj;
        return Objects.equals(this.position, other.position) && Objects.equals(this.state, other.state);
    }
}
