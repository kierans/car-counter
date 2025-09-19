package org.quasar.carcounter;

public record Period (String date, String time, Long numCars) implements Formattable {
  public String toISO() {
    return "%sT%s".formatted(date, time);
  }

  @Override
  public String toFormat() {
    return "%s %s".formatted(toISO(), numCars);
  }
}
