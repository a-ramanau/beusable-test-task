package com.github.aramanau.roomoccupancymanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SingleLoopOccupancyService extends AbstractOccupancyService {

  public SingleLoopOccupancyService(
      final ObjectMapper objectMapper,
      @Value("${guests-file}") final String guestsFile,
      @Value("${premium}") final double premium
  ) {
    super(objectMapper, guestsFile, premium);
  }

  @Override
  public OccupancyResponse calculateOccupancy(final int premiumRooms, final int economyRooms) {
    final var guests = loadGuests();
    Arrays.sort(guests, Collections.reverseOrder());

    var usagePremium = 0;
    var usageEconomy = 0;
    var revenuePremium = 0.0;
    var revenueEconomy = 0.0;

    for (var i = 0; i < guests.length; i++) {
      final var guest = guests[i];
      final var isPremiumGuest = isPremiumGuest(guest);
      final var isPremiumRooms = usagePremium < premiumRooms;
      final var isEconomyRooms = usageEconomy < economyRooms;
      final var isMoreEconomyGuestsThanRooms = guests.length - i > economyRooms - usageEconomy;

      if (isPremiumRooms && (isPremiumGuest || isMoreEconomyGuestsThanRooms)) {
        usagePremium++;
        revenuePremium += guest;
      } else if (!isPremiumGuest && isEconomyRooms) {
        usageEconomy++;
        revenueEconomy += guest;
      }
    }

    return new OccupancyResponse(usagePremium, usageEconomy, revenuePremium, revenueEconomy);
  }


}
