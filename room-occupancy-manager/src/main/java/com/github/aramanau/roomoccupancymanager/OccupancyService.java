package com.github.aramanau.roomoccupancymanager;

import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
public interface OccupancyService {

  OccupancyResponse calculateOccupancy(@Positive final int premiumRooms, @Positive final int economyRooms);

}
