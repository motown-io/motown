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
package io.motown.restutils.json.providers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class EntityNotFoundExceptionMapperTest {
    private static final int NOT_FOUND = 404;

    @Mock
    private EntityNotFoundException exception;

    @Test
    public void testEntityNotFoundExceptionMapper() {
        EntityNotFoundExceptionMapper mapper = new EntityNotFoundExceptionMapper();
        Response response = mapper.toResponse(exception);

        assertNotNull(response);
        assertEquals(NOT_FOUND, response.getStatus());
    }
}
