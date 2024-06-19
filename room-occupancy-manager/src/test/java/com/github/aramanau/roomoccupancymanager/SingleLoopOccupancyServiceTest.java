package com.github.aramanau.roomoccupancymanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SingleLoopOccupancyServiceTest {

  static final OccupancyService OCCUPANCY_SERVICE = new SingleLoopOccupancyService(new ObjectMapper(), "test-guests.json", 100.00);

  @ParameterizedTest
  @MethodSource("com.github.aramanau.roomoccupancymanager.OccupancyServiceTestArguments#shouldCalculateOccupancy")
  void shouldCalculateOccupancy(final int premiumRooms, final int economyRooms, final OccupancyResponse expectedOccupancy) {

    final var actualOccupancy = OCCUPANCY_SERVICE.calculateOccupancy(premiumRooms, economyRooms);

    assertThat(actualOccupancy).isEqualTo(expectedOccupancy);
  }

  @Test
  void shouldRethrowExceptions() {
    final var errorMessage = "some error message";
    try (var files = mockStatic(Files.class)) {
      files.when(() -> Files.readString(any(), any())).thenThrow(new IOException(errorMessage){});
      assertThatThrownBy(() -> OCCUPANCY_SERVICE.calculateOccupancy(3, 3))
          .isInstanceOf(IOException.class)
          .hasMessageContaining(errorMessage);
    }
  }

}
