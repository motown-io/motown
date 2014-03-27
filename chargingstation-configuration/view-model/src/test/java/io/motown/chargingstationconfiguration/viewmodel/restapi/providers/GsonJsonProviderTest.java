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
package io.motown.chargingstationconfiguration.viewmodel.restapi.providers;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class GsonJsonProviderTest {
    private static final String MOCK_OBJECT_JSON = "{\"name\":\"TEST\",\"age\":1}";
    private static final String INCORRECT_MOCK_OBJECT_JSON = "{\"name\":{\"age\":1}}";
    private static final ByteArrayInputStream IS = new ByteArrayInputStream(MOCK_OBJECT_JSON.getBytes());
    private static final MultivaluedMap MAP = mock(MultivaluedMap.class);
    private static final Object MOCK = new MockObject();
    private static final MockObject MOCK_OBJECT = new MockObject("TEST", 1);
    private static final ByteArrayOutputStream OS = new ByteArrayOutputStream();

    private GsonJsonProvider provider = new GsonJsonProvider();

    @Test
    public void testIsReadable() {
        assertTrue(provider.isReadable(MockObject.class, MockObject.class.getGenericSuperclass(), new Annotation[]{}, MediaType.APPLICATION_JSON_TYPE));
    }

    @Test
    public void testIsWriteable() {
        assertTrue(provider.isWriteable(MockObject.class, MockObject.class.getGenericSuperclass(), new Annotation[]{}, MediaType.APPLICATION_JSON_TYPE));
    }

    @Test
    public void testGetSize() {
        assertEquals(-1, provider.getSize(new MockObject(), MockObject.class, MockObject.class.getGenericSuperclass(), new Annotation[]{}, MediaType.APPLICATION_JSON_TYPE));
    }

    @Test
    public void testReadFrom() throws IOException {
        Object obj = provider.readFrom((Class<Object>) MOCK.getClass(), MOCK.getClass(), new Annotation[]{}, MediaType.APPLICATION_JSON_TYPE, MAP, IS);
        assertEquals(new Gson().fromJson(MOCK_OBJECT_JSON, MockObject.class), obj);
    }

    @Test(expected = JsonParseException.class)
    public void testReadFromThrowsJsonParseException() throws IOException {
        Object obj = provider.readFrom((Class<Object>) MOCK.getClass(), MOCK.getClass(), new Annotation[]{}, MediaType.APPLICATION_JSON_TYPE, MAP, new ByteArrayInputStream(INCORRECT_MOCK_OBJECT_JSON.getBytes()));
    }

    @Test
    public void testWriteTo() throws IOException {
        provider.writeTo(MOCK_OBJECT, MOCK_OBJECT.getClass(), MOCK_OBJECT.getClass(), new Annotation[]{}, MediaType.APPLICATION_JSON_TYPE, MAP, OS);
        assertEquals(MOCK_OBJECT_JSON, new String(OS.toByteArray()));
    }

    final static class MockObject {
        private String name;
        private int age;

        MockObject() {
        }

        MockObject(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final MockObject other = (MockObject) obj;
            return Objects.equals(this.name, other.name) && Objects.equals(this.age, other.age);
        }

        @Override
        public String toString() {
            return "MockObject{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
