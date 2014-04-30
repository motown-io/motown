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
package io.motown.operatorapi.json.restapi.util;

import com.google.common.base.Strings;
import io.motown.operatorapi.json.restapi.response.NavigationItem;
import io.motown.operatorapi.json.restapi.response.OperatorApiResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public final class OperatorApiResponseBuilder {
    private static final String QUERY_STRING_FORMAT = "?offset=%d&limit=%d";

    private OperatorApiResponseBuilder() {
    }

    /**
     * Build a {@link io.motown.operatorapi.json.restapi.response.OperatorApiResponse} from the request, offset, limit, total and list of elements.
     *
     * @param request   The {@link javax.servlet.http.HttpServletRequest} which was executed.
     * @param offset    The offset of the results.
     * @param limit     The maximum number of results.
     * @param total     The total number of elements.
     * @param elements  The list of elements.
     * @param <T>       The type of elements in the list. This can be {@link io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation} or {@link io.motown.operatorapi.viewmodel.persistence.entities.Transaction}.
     *
     * @return          An Operator Api response with the correct metadata.
     *
     * @throws java.lang.IllegalArgumentException when the {@code offset} is negative, the {@code limit} is lesser than or equal to 0, the {@code total} is negative and when {@code offset} is lesser than the {@code total} (only if {@code total} is greater than 0.
     */
    public static <T> OperatorApiResponse<T> buildResponse(final HttpServletRequest request, final int offset, final int limit, final long total, final List<T> elements) {
        checkArgument(offset >= 0);
        checkArgument(limit >= 1);
        checkArgument(total >= 0);
        checkArgument(total == 0 || offset < total);

        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();

        String href = requestUri + (!Strings.isNullOrEmpty(queryString) ? "?" + queryString : "");
        NavigationItem previous = new NavigationItem(hasPreviousPage(offset) ? requestUri + String.format(QUERY_STRING_FORMAT, getPreviousPageOffset(offset, limit), limit) : "");
        NavigationItem next = new NavigationItem(hasNextPage(offset, limit, total) ? requestUri + String.format(QUERY_STRING_FORMAT, getNextPageOffset(offset, limit, total), limit) : "");
        NavigationItem first = new NavigationItem(requestUri + String.format(QUERY_STRING_FORMAT, getFirstPageOffset(), limit));
        NavigationItem last = new NavigationItem(requestUri + String.format(QUERY_STRING_FORMAT, getLastPageOffset(total, limit), limit));

        return new OperatorApiResponse<>(href, previous, next, first, last, elements);
    }

    /**
     * Checks whether there is a previous page.
     *
     * @param offset    the current offset.
     * @return {@code true} when a previous page is available, {@code false} otherwise.
     */
    private static boolean hasPreviousPage(final int offset) {
        return offset != 0;
    }

    /**
     * Checks whether there is a next page.
     *
     * @param offset    the current offset.
     * @param limit     the limit.
     * @param total     the total.
     * @return {@code true} when a next page is available, {@code false} otherwise.
     */
    private static boolean hasNextPage(final int offset, final int limit, final long total) {
        return offset < total - limit;
    }

    /**
     * Gets the previous page offset.
     *
     * @param offset    the current offset.
     * @param limit     the limit.
     * @return the previous page offset.
     */
    private static int getPreviousPageOffset(final int offset, final int limit) {
        return hasFullPreviousPage(offset, limit) ? getPreviousFullPageOffset(offset, limit) : getFirstPageOffset();
    }

    /**
     * Gets the next page offset.
     *
     * @param offset    the current offset.
     * @param limit     the limit.
     * @param total     the total.
     * @return the next page offset.
     */
    private static long getNextPageOffset(final int offset, final int limit, final long total) {
        return hasFullNextPage(offset, limit, total) ? getNextFullPageOffset(offset, limit) : getLastPageOffset(total, limit);
    }

    /**
     * Checks whether there is a full previous page.
     *
     * @param offset    the current offset.
     * @param limit     the limit.
     * @return {@code true} if there is a full previous page, {@code false} otherwise.
     */
    private static boolean hasFullPreviousPage(final int offset, final int limit) {
        return offset - limit >= 0;
    }

    /**
     * Checks whether there is a full next page.
     *
     * @param offset    the current offset.
     * @param limit     the limit.
     * @param total     the total.
     * @return {@code true} if there is a full next page, {@code false} otherwise.
     */
    private static boolean hasFullNextPage(final int offset, final int limit, final long total) {
        return offset + limit <= total - limit;
    }

    /**
     * Get the offset of the previous full page.
     *
     * @param offset    the current offset.
     * @param limit     the limit.
     * @return The offset of the previous full page.
     */
    private static int getPreviousFullPageOffset(final int offset, final int limit) {
        return offset - limit;
    }

    /**
     * Gets the offset of the next full page.
     * @param offset    the current offset.
     * @param limit     the limit.
     * @return The offset of the next full page.
     */
    private static int getNextFullPageOffset(final int offset, final int limit) {
        return offset + limit;
    }

    /**
     * Gets the offset of the first page.
     *
     * @return  The first page offset.
     */
    private static int getFirstPageOffset() {
        return 0;
    }

    /**
     * Gets the last page offset.
     *
     * @param total the total.
     * @param limit the limit.
     * @return The last page offset.
     */
    private static long getLastPageOffset(final long total, final int limit) {
        return total - limit >= 0 ? total - limit : 0;
    }



}
