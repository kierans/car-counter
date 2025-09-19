package org.quasar.carcounter;

import org.jetbrains.annotations.NotNull;

public record Period (@NotNull String date, @NotNull String time, @NotNull Long numCars) implements Formattable {
  public String toISO() {
    return "%sT%s".formatted(date, time);
  }

  @Override
  public String toFormat() {
    return "%s %s".formatted(toISO(), numCars);
  }
}
