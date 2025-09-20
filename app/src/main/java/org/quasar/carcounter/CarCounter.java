package org.quasar.carcounter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

/*
 * The implementations of the methods are not optimized for performance.
 *
 * For large data sets, the methods should use more efficient data structures like trees to reduce the linear complexity
 * to logarithmic complexity.
 */
public class CarCounter {
  private final List<Period> periods;

  public CarCounter(final @NotNull List<Period> periods) {
    this.periods = periods;
  }

  public Long totalCars() {
    return this.periods.stream().reduce(0L, (sum, period) -> sum + period.numCars(), Long::sum);
  }

  public List<CarsPerDay> carsPerDay() {
    final Map<String, Long> carsByDate = periods.stream()
      .collect(Collectors.groupingBy(
        Period::date,
        Collectors.summingLong(Period::numCars)
      ));

    return carsByDate.entrySet().stream()
      .sorted(Map.Entry.comparingByKey())
      .map(entry -> new CarsPerDay(entry.getKey(), entry.getValue()))
      .toList();
  }

  public List<Period> topUsagePeriods() {
    return periods.stream()
      .sorted(Comparator.comparing(Period::numCars, Comparator.reverseOrder()))
      .collect(Collectors.toList())
      .subList(0, 3);
  }

  /**
   * A "least usage window" is a 90-minute period of time with the least number of cars.
   */
  public List<Period> leastUsageWindow() {
    return new LeastUsageWindow().addAll(periods).getLeastUsageWindow();
  }

  /**
   * Convenience class to represent cars per day.
   *
   * @param date
   * @param cars
   */
  public record CarsPerDay(@NotNull String date, @NotNull Long cars) implements Formattable {
    @Override
    public String toFormat() {
      return "%s %s".formatted(date, cars);
    }
  }
}
