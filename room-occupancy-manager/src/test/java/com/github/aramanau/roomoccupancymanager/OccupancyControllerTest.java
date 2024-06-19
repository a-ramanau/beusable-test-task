package com.github.aramanau.roomoccupancymanager;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OccupancyControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @SneakyThrows
  void shouldCalculateOccupancy() {
    final var expectedResponse = loadResourceAsString("occupancy-response.json");

    final var actualResponse = performGetRequest("/api/v1/occupancy?premiumRooms=7&economyRooms=5", OK);

    assertThatJson(actualResponse).isEqualTo(expectedResponse);
  }

  static Stream<Arguments> shouldReturnError() {
    return Stream.of(
        Arguments.of("/api/v1/occupancy?premiumRooms=-7&economyRooms=2", "constraint-violation-parameter-response.json", BAD_REQUEST),
        Arguments.of("/api/v1/occupancy?premiumRooms=7&economyRooms=aa", "type-mismatch-response.json", BAD_REQUEST),
        Arguments.of("/api/v1/occupancy", "missing-required-parameter-response.json", BAD_REQUEST),
        Arguments.of("/api/v2/occupancy", "internal-server-error-response.json", INTERNAL_SERVER_ERROR)
    );
  }

  @ParameterizedTest
  @MethodSource
  void shouldReturnError(final String requestUrl, final String responseJsonFilePath, final HttpStatus responseStatus) {
    final var expectedResponse = loadResourceAsString(responseJsonFilePath);

    final var actualResponse = performGetRequest(requestUrl, responseStatus);

    assertThatJson(actualResponse).isEqualTo(expectedResponse);
  }

  @SneakyThrows
  private String performGetRequest(final String requestUrl, final HttpStatus responseStatus) {
    return mockMvc.perform(get(requestUrl))
        .andExpect(status().is(responseStatus.value()))
        .andReturn().getResponse().getContentAsString();
  }

  @SneakyThrows
  private static String loadResourceAsString(final String filePath) {
    return Files.readString(
        ResourceUtils.getFile(CLASSPATH_URL_PREFIX + filePath).toPath(),
        StandardCharsets.UTF_8
    );
  }
}
