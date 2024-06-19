package com.github.aramanau.roomoccupancymanager;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class StreamsOccupancyServiceTest {

  static final OccupancyService OCCUPANCY_SERVICE = new StreamsOccupancyService(new ObjectMapper(), "test-guests.json", 100.00);

  @ParameterizedTest
  @MethodSource("com.github.aramanau.roomoccupancymanager.OccupancyServiceTestArguments#shouldCalculateOccupancy")
  void shouldCalculateOccupancy(final int premiumRooms, final int economyRooms, final OccupancyResponse expectedOccupancy) {

    final var actualOccupancy = OCCUPANCY_SERVICE.calculateOccupancy(premiumRooms, economyRooms);

    assertThat(actualOccupancy).isEqualTo(expectedOccupancy);
  }
}
