package org.quasar.carcounter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
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
    return sumNumCars(this.periods);
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
    // iterate over the periods, finding 90-minute windows.
    return periods.stream().reduce(
      new LeastUsageWindowData(),
      (acc, current) -> {
        if (acc.previous != null) {
          if (isContiguous(acc.previous, current)) {
            if (haveUsageWindow(acc.contiguousPeriods)) {
              acc.leastUsageWindow = leastUsage(acc.leastUsageWindow, acc.contiguousPeriods);

              // remove the top of the window to allow for a new window to be found
              acc.contiguousPeriods.remove();
            }
          }
          else {
            // check if a window has been found already.
            if (haveUsageWindow(acc.contiguousPeriods)) {
              acc.leastUsageWindow = leastUsage(acc.leastUsageWindow, acc.contiguousPeriods);
            }

            // start a new window
            acc.contiguousPeriods.clear();
          }
        }

        acc.contiguousPeriods.add(current);
        acc.previous = current;

        return acc;
      },
      (a, b) -> a).leastUsageWindow;
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
   * Data structure for reduction operation when finding the least usage window.
   */
  private static class LeastUsageWindowData {
    // a group of contiguous periods.
    final LinkedList<Period> contiguousPeriods = new LinkedList<>();

    // the current window of periods with the least number of cars.
    List<Period> leastUsageWindow = new ArrayList<>(0);

    // what the previous period in the list was.
    Period previous = null;
  }

  private static boolean haveUsageWindow(final LinkedList<Period> periods) {
    return periods.size() == 3;
  }

  /*
   * Two periods are contiguous if they are within 30 minutes of each other.
   */
  private static boolean isContiguous(final Period earlier, final Period later) {
    return LocalDateTime.parse(earlier.toISO()).plusMinutes(30).equals(LocalDateTime.parse(later.toISO()));
  }

  /*
   * If the `contiguousPeriods` list has fewer cars than the `leastUsageWindow` then a new list is returned to
   * prevent mutation issues to the same list due to references.
   */
  private static List<Period> leastUsage(final List<Period> leastUsageWindow, final List<Period> contiguousPeriods) {
    /*
     * cater for the case where the leastUsageWindow is empty as well, in which case we can just return the
     * contiguousPeriods list.
     */
    return !leastUsageWindow.isEmpty() && sumNumCars(leastUsageWindow) <= sumNumCars(contiguousPeriods)
      ? leastUsageWindow
      : new ArrayList<>(contiguousPeriods);
  }

  private static Long sumNumCars(final List<Period> periods) {
    return periods.stream().reduce(0L, (sum, period) -> sum + period.numCars(), Long::sum);
  }
}
