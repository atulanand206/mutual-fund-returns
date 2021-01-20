package com.creations.funds.service;

import com.creations.funds.AppConfig;
import com.creations.funds.jackson.Serializer;
import com.creations.funds.jackson.Serializers;
import com.creations.funds.models.SchemeLite;
import com.creations.funds.models.SchemeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.lang.Nullable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableAutoConfiguration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = CommandLineRunner.class))
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true",
        classes = {AppConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class ControllerTest {

    @Autowired
    private WebApplicationContext fAppContext;

    private MockMvc fMockMvc;

    @BeforeAll
    void setUp() {
        fMockMvc = MockMvcBuilders.webAppContextSetup(fAppContext)
                .build();
    }

    private static final String FETCH_SCHEMES = "/funds";
    private static final String FETCH_SCHEME = "/funds/%s";
    private static final Serializer<SchemeLite[]> SCHEMES_LITE_SERIALIZER =
            Serializers.newJsonSerializer(SchemeLite[].class);
    private static final Serializer<SchemeResponse> SCHEME_RESPONSE_SERIALIZER =
            Serializers.newJsonSerializer(SchemeResponse.class);

    private RequestBuilder fetchSchemesRequest() {
        return MockMvcRequestBuilders
                .get(FETCH_SCHEMES)
                .contentType(APPLICATION_JSON);
    }

    private RequestBuilder fetchSchemeRequest(@Nullable Integer schemeCode, int periodOfInvestment, int horizon) {
        return MockMvcRequestBuilders
                .get(String.format(FETCH_SCHEME, schemeCode))
                .queryParam("period", String.valueOf(periodOfInvestment))
                .queryParam("horizon", String.valueOf(horizon))
                .contentType(APPLICATION_JSON);
    }

    /**
     * Test passes if the response is correctly deserialized.
     */
    @Test
    void testFetchSchemes() throws Exception {
        final var contentAsString = fMockMvc.perform(fetchSchemesRequest())
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        SCHEMES_LITE_SERIALIZER.deserialize(contentAsString);
    }

    /**
     * Test passes because the request is malformed.
     */
    @Test
    void testFetchSchemeWithoutCode() throws Exception {
        fMockMvc.perform(fetchSchemeRequest(null, 0, 0)).andExpect(status().isBadRequest());
    }

    /**
     * Test passes because the data for scheme code is unavailable and returns 0 items.
     */
    @Test
    void testFetchSchemeForCodeWithoutData() throws Exception {
        final var contentAsString = fMockMvc.perform(fetchSchemeRequest(1, 1, 1))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        final var scheme = SCHEME_RESPONSE_SERIALIZER.deserialize(contentAsString);
        Assertions.assertEquals(0, scheme.getResponseItems().size());
    }

    /**
     * Test passes when the response contains filtered returns for a year.
     */
    @Test
    void testFetchSchemeForCode() throws Exception {
        final var contentAsString = fMockMvc.perform(fetchSchemeRequest(102883, 1, 1))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        final var scheme = SCHEME_RESPONSE_SERIALIZER.deserialize(contentAsString);
        Assertions.assertEquals(12, scheme.getResponseItems().size());
    }
}
