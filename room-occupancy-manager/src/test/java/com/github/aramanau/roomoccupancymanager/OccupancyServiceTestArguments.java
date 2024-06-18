package com.github.aramanau.roomoccupancymanager;

import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

class OccupancyServiceTestArguments {

  static Stream<Arguments> shouldCalculateOccupancy() {
    return Stream.of(
        Arguments.of(3, 3, new OccupancyResponse(3, 3, 738, 167.99)),
        Arguments.of(7, 5, new OccupancyResponse(6, 4, 1054, 189.99)),
        Arguments.of(2, 7, new OccupancyResponse(2, 4, 583, 189.99)),
        Arguments.of(7, 1, new OccupancyResponse(7, 1, 1153.99, 45))
    );
  }

}
