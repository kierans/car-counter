package org.quasar.carcounter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;

import org.junit.Test;

public class PeriodTest {
  final String date = "2022-12-01";
  final String time = "05:00:00";

  final Period period = new Period(LocalDateTime.parse("%sT%s".formatted(date, time)), 10L);

  @Test
  public void shouldFormatTimestamp() {
    assertThat(period.toISO(), is("%sT%s".formatted(date, time)));
  }

  @Test
  public void shouldReturnDateFromTimestamp() {
    assertThat(period.date(), is(date));
  }

  @Test
  public void shouldReturnTimeFromTimestamp() {
    assertThat(period.time(), is(time));
  }
}
