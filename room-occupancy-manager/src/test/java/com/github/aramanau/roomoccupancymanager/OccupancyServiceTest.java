package com.github.aramanau.roomoccupancymanager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OccupancyServiceTest {

  static final OccupancyService OCCUPANCY_SERVICE = new OccupancyService();

  static Stream<Arguments> shouldCalculateOccupancy() {
    return Stream.of(
        Arguments.of(3, 3, new OccupancyResponse(3, 3, 738, 167.99)),
        Arguments.of(7, 5, new OccupancyResponse(6, 4, 1054, 189.99)),
        Arguments.of(2, 7, new OccupancyResponse(2, 4, 583, 189.99)),
        Arguments.of(7, 1, new OccupancyResponse(7, 1, 1153.99, 45))
    );
  }

  @ParameterizedTest
  @MethodSource
  void shouldCalculateOccupancy(final int premiumRooms, final int economyRooms, final OccupancyResponse expectedOccupancy) {

    final var actualOccupancy = OCCUPANCY_SERVICE.calculateOccupancy(premiumRooms, economyRooms);

    assertThat(actualOccupancy).isEqualTo(expectedOccupancy);
  }
}
