package org.quasar.carcounter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CarCounterTest {
  private final List<Period> periods = List.of(
    new Period("2021-12-01", "05:00:00", 5L),
    new Period("2021-12-01", "05:30:00", 12L),
    new Period("2021-12-01", "06:00:00", 14L),
    new Period("2021-12-01", "06:30:00", 15L),
    new Period("2021-12-01", "07:00:00", 25L),
    new Period("2021-12-01", "07:30:00", 46L),
    new Period("2021-12-01", "08:00:00", 42L),
    new Period("2021-12-01", "15:00:00", 9L),
    new Period("2021-12-01", "15:30:00", 11L),
    new Period("2021-12-01", "23:30:00", 0L),
    new Period("2021-12-05", "09:30:00", 18L),
    new Period("2021-12-05", "10:30:00", 15L),
    new Period("2021-12-05", "11:30:00", 7L),
    new Period("2021-12-05", "12:30:00", 6L),
    new Period("2021-12-05", "13:30:00", 9L),
    new Period("2021-12-05", "14:30:00", 11L),
    new Period("2021-12-05", "15:30:00", 15L),
    new Period("2021-12-08", "18:00:00", 33L),
    new Period("2021-12-08", "19:00:00", 28L),
    new Period("2021-12-08", "20:00:00", 25L),
    new Period("2021-12-08", "21:00:00", 21L),
    new Period("2021-12-08", "22:00:00", 16L),
    new Period("2021-12-08", "23:00:00", 11L),
    new Period("2021-12-09", "00:00:00", 4L)
  );

  private CarCounter carCounter;

  @Before
  public void setUp() {
    carCounter = new CarCounter(periods);
  }

  @Test
  public void shouldCalculateTotalCars() {
    assertThat(carCounter.totalCars(), is(398L));
  }

  @Test
  public void shouldCalculateNumberOfCarsPerDay() {
    assertThat(carCounter.carsPerDay(), equalTo(List.of(
      new CarCounter.CarsPerDay("2021-12-01", 179L),
      new CarCounter.CarsPerDay("2021-12-05", 81L),
      new CarCounter.CarsPerDay("2021-12-08", 134L),
      new CarCounter.CarsPerDay("2021-12-09", 4L)
    )));
  }

  @Test
  public void shouldCalculateTopUsagePeriods() {
    assertThat(carCounter.topUsagePeriods(), equalTo(List.of(
      new Period("2021-12-01", "07:30:00", 46L),
      new Period("2021-12-01", "08:00:00", 42L),
      new Period("2021-12-08", "18:00:00", 33L)
    )));
  }

  @Test
  public void shouldFindLeastUsageWindow() {
    assertThat(carCounter.leastUsageWindow(), equalTo(List.of(
      new Period("2021-12-01", "05:00:00", 5L),
      new Period("2021-12-01", "05:30:00", 12L),
      new Period("2021-12-01", "06:00:00", 14L)
    )));
  }

  @Test
  public void shouldReturnNothingWhenNoLeastUsageWindow() {
    assertThat(new CarCounter(List.of()).leastUsageWindow(), equalTo(List.of()));
  }
}
