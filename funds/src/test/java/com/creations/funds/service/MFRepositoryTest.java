package com.creations.funds.service;

import com.creations.funds.jackson.Serializer;
import com.creations.funds.jackson.Serializers;
import com.creations.funds.models.Scheme;
import com.creations.funds.models.SchemeData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.creations.funds.constants.Constants.SERVICE_DATE_PATTERN;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
final class MFRepositoryTest {

    private static final Serializer<Scheme> FUND_SERIALIZER = Serializers.newJsonSerializer(Scheme.class);
    private Scheme scheme;
    private SimpleDateFormat simpleDateFormat;

    @BeforeAll
    void setup() {
        scheme = FUND_SERIALIZER.deserialize(readFromJson("scheme-test.json"));
        simpleDateFormat = new SimpleDateFormat(SERVICE_DATE_PATTERN);
    }

    @MethodSource("argumentsForReturn")
    @ParameterizedTest
    void testReturns(String startDate, String endDate, double startNav, double endNav, double expectedReturn, int period) {
        try {
            final var start = simpleDateFormat.parse(startDate);
            final var end = simpleDateFormat.parse(endDate);
            final var schemeDataStart = new SchemeData(start, startNav);
            final var schemeDataEnd = new SchemeData(end, endNav);
            final var returns = new MFRepository().returns(schemeDataStart, schemeDataEnd, period);
            Assertions.assertEquals(expectedReturn, returns);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static Stream<Arguments> argumentsForReturn() {
        return Stream.of(
                Arguments.of("17-04-2019", "19-04-2020", 80.4486, 58.713, -0.2701799658415436, 1),
                Arguments.of("18-02-2019", "18-02-2020", 73.1845, 80.557, 0.10073854436390217, 1)
        );
    }

    @MethodSource("argumentsForGetFundResponse")
    @ParameterizedTest
    void testGetFundResponse(int period, int horizon, int expectedSize) {
        final var fundResponse = new MFRepository().getFundResponse(scheme, period, horizon);
        Assertions.assertEquals(expectedSize, fundResponse.getResponseItems().size());

    }

    private static Stream<Arguments> argumentsForGetFundResponse() {
        return Stream.of(
                Arguments.of(1, 1, 12),
                Arguments.of(2, 2, 24),
                Arguments.of(1, 2, 24)
        );
    }

    private static String readFromJson(final String fileName) {
        try (final var templateResource = new InputStreamReader(
                Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(fileName)));
             final var bufferedTemplateReader = new BufferedReader(templateResource)) {
            return bufferedTemplateReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
