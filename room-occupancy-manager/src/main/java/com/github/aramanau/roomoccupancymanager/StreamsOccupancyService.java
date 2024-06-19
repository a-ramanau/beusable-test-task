package com.github.aramanau.roomoccupancymanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StreamsOccupancyService extends AbstractOccupancyService {

  public StreamsOccupancyService(
      final ObjectMapper objectMapper,
      @Value("${guests-file}") final String guestsFile,
      @Value("${premium}") final double premium
  ) {
    super(objectMapper, guestsFile, premium);
  }

  @Override
  public OccupancyResponse calculateOccupancy(final int premiumRooms, final int economyRooms) {
    final var guests = loadGuests();
    final var partitionedGuests = Arrays.stream(guests)
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.partitioningBy(this::isPremiumGuest));

    final var premiumGuests = partitionedGuests.get(true);
    final var economyGuests = partitionedGuests.get(false);

    final var premiumUsage = (int) premiumGuests.stream().limit(premiumRooms).count();
    final var premiumRevenue = premiumGuests.stream().limit(premiumRooms).mapToDouble(value -> value).sum();

    if (economyGuests.size() > economyRooms) {
      final var freePremiumRooms = premiumRooms - premiumUsage;
      final var economyToPremium = Math.min(economyGuests.size() - economyRooms, freePremiumRooms);
      final var economyToPremiumRevenue = economyGuests.stream().limit(economyToPremium).mapToDouble(value -> value).sum();
      final var economyRevenue = economyGuests.stream().skip(economyToPremium).limit(economyRooms).mapToDouble(value -> value).sum();
      return new OccupancyResponse(premiumUsage + economyToPremium, economyRooms, premiumRevenue + economyToPremiumRevenue, economyRevenue);
    } else {
      final var economyUsage = (int) economyGuests.stream().limit(economyRooms).count();
      final var economyRevenue = economyGuests.stream().limit(economyRooms).mapToDouble(value -> value).sum();
      return new OccupancyResponse(premiumUsage, economyUsage, premiumRevenue, economyRevenue);
    }

  }

}
