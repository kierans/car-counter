package org.quasar.carcounter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.quasar.carcounter.PeriodMatcher.aPeriod;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {
  Lexer lexer;

  @Before
  public void setUp() {
    lexer = new Lexer();
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowErrorParsingPeriodWhenDataIsInvalid() {
    lexer.parse("foo");
  }

  @Test
  public void shouldParsePeriod() {
    final String timestamp = "2021-12-01T05:00:00";
    final String cars = "5";

    final Period result = lexer.parse(toInputFormat(timestamp, cars));

    assertThat(result, is(aPeriod(timestamp, Long.valueOf(cars))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowErrorParsingListOfPeriodsWhenDataIsInvalid() {
    lexer.parse(List.of("foo"));
  }

  @Test
  public void shouldParseListOfPeriods() {
    final String date = "2022-12-01";
    final String time1 = "05:00:00";
    final String time2 = "05:30:00";
    final String cars = "10";

    final List<Period> result = lexer.parse(List.of(
      toInputFormat(date, time1, cars),
      toInputFormat(date, time2, cars)
    ));

    assertThat(result.size(), is(2));

    assertThat(result.get(0), is(aPeriod(date, time1, Long.valueOf(cars))));
    assertThat(result.get(1), is(aPeriod(date, time2, Long.valueOf(cars))));
  }

  private String toInputFormat(String timestamp, String cars) {
    return "%s %s".formatted(timestamp, cars);
  }

  private String toInputFormat(String date, String time, String cars) {
    return toInputFormat("%sT%s".formatted(date, time), cars);
  }
}
