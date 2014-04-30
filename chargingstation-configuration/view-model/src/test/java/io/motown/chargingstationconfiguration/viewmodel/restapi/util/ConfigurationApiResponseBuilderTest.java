package io.motown.chargingstationconfiguration.viewmodel.restapi.util;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigurationApiResponseBuilderTest {
    private HttpServletRequest request;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getQueryString()).thenReturn("offset=0&limit=10");
    }

    @Test
    public void testConfigurationApiResponseBuilder() {
        ConfigurationApiResponseBuilder.buildResponse(request, 0, 1, 0, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationApiResponseBuilderInvalidOffset() {
        ConfigurationApiResponseBuilder.buildResponse(request, -1, 1, 0, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationApiResponseBuilderInvalidLimit() {
        ConfigurationApiResponseBuilder.buildResponse(request, 0, 0, 1, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationApiResponseBuilderInvalidTotal() {
        ConfigurationApiResponseBuilder.buildResponse(request, 0, 1, -1, Lists.<String>newArrayList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigurationApiResponseBuilderOffsetEqualToTotalWhenTotalGreaterThanZero() {
        ConfigurationApiResponseBuilder.buildResponse(request, 1, 1, 1, Lists.<String>newArrayList());
    }
}
