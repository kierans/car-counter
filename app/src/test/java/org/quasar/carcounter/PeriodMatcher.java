package org.quasar.carcounter;

import java.time.LocalDateTime;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class PeriodMatcher extends TypeSafeMatcher<Period> {
  private final LocalDateTime timestamp;
  private final Long numCars;

  public PeriodMatcher(final LocalDateTime timestamp, final Long numCars) {
    this.timestamp = timestamp;
    this.numCars = numCars;
  }

  @Override
  protected boolean matchesSafely(final Period period) {
    return period.timestamp().equals(this.timestamp) && period.numCars().equals(this.numCars);
  }

  @Override
  public void describeTo(final Description description) {
    description
      .appendText("Period{timestamp=")
      .appendValue(timestamp)
      .appendText(", numCars=")
      .appendValue(numCars)
      .appendText("}");
  }

  public static PeriodMatcher aPeriod(final String timestamp, final Long numCars) {
    return new PeriodMatcher(LocalDateTime.parse(timestamp), numCars);
  }

  public static PeriodMatcher aPeriod(final String date, final String time, final Long numCars) {
    return aPeriod("%sT%s".formatted(date, time), numCars);
  }
}
