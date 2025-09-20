package org.quasar.carcounter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LeastUsageWindowTest {
  LeastUsageWindow window;

  @Before
  public void setUp() {
    window = new LeastUsageWindow();
  }

  @Test
  public void shouldFindLeastWindowUsageAtStartOfList() {
    final List<Period> periods = List.of(
      new Period("2021-12-01", "05:00:00", 100L),
      new Period("2021-12-01", "05:30:00", 12L),
      new Period("2021-12-01", "06:00:00", 14L),
      new Period("2021-12-01", "07:00:00", 25L)
    );

    window.addAll(periods);

    assertThat(window.getLeastUsageWindow(), is(equalTo(periods.subList(0, 3))));
  }

  @Test
  public void shouldFindLeastWindowUsageInMiddleOfList() {
    final List<Period> periods = List.of(
      new Period("2021-12-01", "05:00:00", 100L),
      new Period("2021-12-01", "06:30:00", 12L),
      new Period("2021-12-01", "07:00:00", 14L),
      new Period("2021-12-01", "07:30:00", 15L),
      new Period("2021-12-01", "08:00:00", 25L)
    );

    window.addAll(periods);

    assertThat(window.getLeastUsageWindow(), is(equalTo(periods.subList(1, 4))));
  }

  @Test
  public void shouldFindLeastWindowUsageAtEndOfList() {
    final List<Period> periods = List.of(
      new Period("2021-12-01", "05:00:00", 100L),
      new Period("2021-12-01", "06:00:00", 14L),
      new Period("2021-12-01", "06:30:00", 15L),
      new Period("2021-12-01", "07:00:00", 25L)
    );

    window.addAll(periods);

    assertThat(window.getLeastUsageWindow(), is(equalTo(periods.subList(1, 4))));
  }

  @Test
  public void shouldIgnoreNonContiguousPeriods() {
    final List<Period> periods = List.of(
      new Period("2021-12-01", "05:00:00", 100L),
      new Period("2021-12-01", "06:00:00", 14L),
      new Period("2021-12-01", "06:30:00", 15L),
      new Period("2021-12-01", "07:00:00", 25L),
      new Period("2021-12-01", "08:00:00", 25L)
    );

    window.addAll(periods);

    assertThat(window.getLeastUsageWindow(), is(equalTo(periods.subList(1, 4))));
  }

  @Test
  public void shouldReturnNothingWhenNoLeastUsageWindow() {
    window.addAll(List.of(
      new Period("2021-12-01", "05:00:00", 5L),
      new Period("2021-12-01", "05:30:00",  12L),
      new Period("2021-12-01", "06:30:00",  15L),
      new Period("2021-12-01", "07:00:00",  25L)
    ));

    assertThat(window.getLeastUsageWindow(), is(empty()));
  }

  @Test
  public void shouldSlideWindowOverContiguousPeriods() {
    final List<Period> periods = List.of(
      new Period("2021-12-01", "05:00:00", 100L),
      new Period("2021-12-01", "05:30:00", 12L),
      new Period("2021-12-01", "06:00:00", 14L),
      new Period("2021-12-01", "06:30:00", 15L),
      new Period("2021-12-01", "07:00:00", 25L)
    );

    window.addAll(periods);

    assertThat(window.getLeastUsageWindow(), is(equalTo(periods.subList(1, 4))));
  }
}
