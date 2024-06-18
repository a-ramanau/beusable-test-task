package com.github.aramanau.roomoccupancymanager;


public interface OccupancyService {

  OccupancyResponse calculateOccupancy(final int premiumRooms, final int economyRooms);

}
