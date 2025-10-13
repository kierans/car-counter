package org.quasar.carcounter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jetbrains.annotations.NotNull;

public record Period (@NotNull LocalDateTime timestamp, @NotNull Long numCars) implements Formattable {
  public Period(@NotNull String timestamp, @NotNull Long numCars) {
    this(LocalDateTime.parse(timestamp), numCars);
  }

  public Period(@NotNull String date, @NotNull String time, @NotNull Long numCars) {
    this("%sT%s".formatted(date, time), numCars);
  }

  public String toISO() {
    return timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
  }

  @Override
  public String toFormat() {
    return "%s %s".formatted(toISO(), numCars);
  }

  public String date() {
    return timestamp.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public String time() {
    return timestamp.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
  }
}
