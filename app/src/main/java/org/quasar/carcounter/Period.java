package org.quasar.carcounter;

public record Period (String date, String time, Long numCars) {
  public String toISO() {
    return "%sT%s".formatted(date, time);
  }
}
