package com.github.aramanau.roomoccupancymanager;

import org.springframework.stereotype.Service;

@Service
public class OccupancyService {

  public OccupancyResponse calculateOccupancy(final int premiumRooms, final int economyRooms) {

    return new OccupancyResponse(0, 0, 0, 0);
  }

}
