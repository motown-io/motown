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
package io.motown.utils.rest.json.response;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Response to be sent from the Operator API. The response consists of the current request URI, the previous, next, first and last navigation links and a list of elements.
 *
 * @param <T> The type of element. This can be either one of {@link io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation} or {@link io.motown.operatorapi.viewmodel.persistence.entities.Transaction}.
 */
public final class Response<T> {
    private final String href;
    private final NavigationItem previous;
    private final NavigationItem next;
    private final NavigationItem first;
    private final NavigationItem last;
    private final List<T> elements;

    /**
     * Construct a new API response.
     *
     * @param href      the current request URI.
     * @param previous  the previous link.
     * @param next      the next link.
     * @param first     the first link.
     * @param last      the last link.
     * @param elements  the list of elements.
     */
    public Response(String href, NavigationItem previous, NavigationItem next, NavigationItem first, NavigationItem last, List<T> elements) {
        checkArgument(!checkNotNull(href).isEmpty());
        this.href = href;

        this.previous = checkNotNull(previous);
        this.next = checkNotNull(next);

        checkArgument(!checkNotNull(first).getHref().isEmpty());
        this.first = first;

        checkArgument(!checkNotNull(last).getHref().isEmpty());
        this.last = last;

        this.elements = ImmutableList.copyOf(checkNotNull(elements));
    }

    public String getHref() {
        return href;
    }

    public NavigationItem getPrevious() {
        return previous;
    }

    public NavigationItem getNext() {
        return next;
    }

    public NavigationItem getFirst() {
        return first;
    }

    public NavigationItem getLast() {
        return last;
    }

    public List<T> getElements() {
        return elements;
    }

}
