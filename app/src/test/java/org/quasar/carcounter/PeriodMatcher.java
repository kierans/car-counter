package org.quasar.carcounter;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class PeriodMatcher extends TypeSafeMatcher<Period> {
  private final String date;
  private final String time;
  private final Long numCars;

  public PeriodMatcher(final String date, final String time, final Long numCars) {
    this.date = date;
    this.time = time;
    this.numCars = numCars;
  }

  @Override
  protected boolean matchesSafely(final Period period) {
    return period.date().equals(this.date) && period.time().equals(this.time) && period.numCars().equals(this.numCars);
  }

  @Override
  public void describeTo(final Description description) {
    description
      .appendText("Period{date=")
      .appendValue(date)
      .appendText(", time=")
      .appendValue(time)
      .appendText(", numCars=")
      .appendValue(numCars)
      .appendText("}");
  }

  public static PeriodMatcher aPeriod(final String date, final String time, final Long numCars) {
    return new PeriodMatcher(date, time, numCars);
  }
}
