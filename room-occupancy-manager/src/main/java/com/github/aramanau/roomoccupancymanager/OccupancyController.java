package com.github.aramanau.roomoccupancymanager;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/occupancy", produces = APPLICATION_JSON_VALUE)
public class OccupancyController {

  @Qualifier("singleLoopOccupancyService")
  final OccupancyService occupancyService;

  @GetMapping
  public OccupancyResponse getOccupancy(@RequestParam final int premiumRooms, @RequestParam final int economyRooms) {
    return occupancyService.calculateOccupancy(premiumRooms, economyRooms);
  }

}
