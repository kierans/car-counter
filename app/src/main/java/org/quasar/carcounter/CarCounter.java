package org.quasar.carcounter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

/*
 * The implementations of the methods are not optimized for performance.
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
    // if frequently accessed, this could be an attribute.
    final PriorityQueue<Period> heap = indexCarsByTopUsage();

    return take(3, heap);
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

  /*
   * Indexes the periods by the number of cars they contain.
   *
   * The result is a sorted tree that has O(log(n)) insertion and retrieval time.
   * For large data sets this is an example of where a tree is more efficient than a simple list reduction.
   */
  private @NotNull PriorityQueue<Period> indexCarsByTopUsage() {
    final PriorityQueue<Period> heap = new PriorityQueue<>(
      this.periods.size(),
      Comparator.comparing(Period::numCars, Comparator.reverseOrder())
    );

    heap.addAll(periods);

    return heap;
  }

  private static @NotNull List<Period> take(final int n, final PriorityQueue<Period> heap) {
    final List<Period> result = new ArrayList<>();

    if (n <= 0) {
      throw new IllegalArgumentException("n must be greater than 0");
    }

    if (heap.size() < n) {
      throw new IllegalArgumentException("n must be less than or equal to the number of periods");
    }

    /*
     * Because the PriorityQueue streaming doesn't guarantee the order of the elements, we need to poll them in order.
     */
    for (int i = 0; i < n;  i++) {
      result.add(heap.poll());
    }

    return result;
  }
}
