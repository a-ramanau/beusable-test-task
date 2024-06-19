package com.github.aramanau.roomoccupancymanager;

import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;

@AllArgsConstructor
public abstract class AbstractOccupancyService implements OccupancyService {

  final ObjectMapper objectMapper;
  final String guestsFile;
  final double premium;

  protected boolean isPremiumGuest(final double guest) {
    return guest >= premium;
  }

  @SneakyThrows
  protected Double[] loadGuests() {

    final var str = Files.readString(
        ResourceUtils.getFile(CLASSPATH_URL_PREFIX + guestsFile).toPath(),
        StandardCharsets.UTF_8
    );

    return objectMapper.readValue(str, Double[].class);
  }

}
