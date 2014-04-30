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

    public static <T> OperatorApiResponse<T> buildResponse(final HttpServletRequest request, final int offset, final int limit, final long total, final List<T> elements) {
        checkArgument(offset >= 0);
        checkArgument(limit >= 1);
        checkArgument(total >= 0);
        checkArgument(offset < total);

        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();

        String href = requestUri + (!Strings.isNullOrEmpty(queryString) ? "?" + queryString : "");
        NavigationItem previous = new NavigationItem(hasPreviousPage(offset) ? requestUri + String.format(QUERY_STRING_FORMAT, getPreviousPageOffset(offset, limit), limit) : "");
        NavigationItem next = new NavigationItem(hasNextPage(offset, limit, total) ? requestUri + String.format(QUERY_STRING_FORMAT, getNextPageOffset(offset, limit, total), limit) : "");
        NavigationItem first = new NavigationItem(requestUri + String.format(QUERY_STRING_FORMAT, getFirstPageOffset(), limit));
        NavigationItem last = new NavigationItem(requestUri + String.format(QUERY_STRING_FORMAT, getLastPageOffset(total, limit), limit));

        return new OperatorApiResponse<>(href, previous, next, first, last, elements);
    }

    private static boolean hasPreviousPage(final int offset) {
        return offset != 0;
    }

    private static boolean hasNextPage(final int offset, final int limit, final long total) {
        return offset < total - limit;
    }

    private static int getPreviousPageOffset(final int offset, final int limit) {
        return hasFullPreviousPage(offset, limit) ? getPreviousFullPageOffset(offset, limit) : getFirstPageOffset();
    }

    private static long getNextPageOffset(final int offset, final int limit, final long total) {
        return hasFullNextPage(offset, limit, total) ? getNextFullPageOffset(offset, limit) : getLastPageOffset(total, limit);
    }

    private static boolean hasFullPreviousPage(final int offset, final int limit) {
        return offset - limit >= 0;
    }

    private static boolean hasFullNextPage(final int offset, final int limit, final long total) {
        return offset + limit <= total - limit;
    }

    private static int getPreviousFullPageOffset(final int offset, final int limit) {
        return offset - limit;
    }

    private static int getNextFullPageOffset(final int offset, final int limit) {
        return offset + limit;
    }

    private static int getFirstPageOffset() {
        return 0;
    }

    private static long getLastPageOffset(final long total, final int limit) {
        return total - limit >= 0 ? total - limit : 0;
    }



}
