package org.quasar.carcounter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class LeastUsageWindow {
  private static final int WINDOW_SIZE = 3;

  // a group of contiguous periods.
  private final LinkedList<Period> slidingWindow = new LinkedList<>();

  // the current window of periods with the least number of cars.
  private List<Period> leastUsageWindow = new ArrayList<>(0);

  // the last period seen
  private Period previous = null;

  public List<Period> getLeastUsageWindow() {
    // if the last WINDOW_SIZE periods have formed a new window, then we need to check for it.
    checkForUsageWindow();

    return leastUsageWindow;
  }

  public LeastUsageWindow add(final @NotNull Period current) {
    if (previous != null) {
      checkForNewWindowWithPeriod(current);
    }

    slidingWindow.add(current);
    previous = current;

    return this;
  }

  public LeastUsageWindow addAll(final @NotNull List<Period> periods) {
    periods.forEach(this::add);

    return this;
  }

  private void checkForNewWindowWithPeriod(final Period current) {
    if (isContiguous(previous, current)) {
      if (checkForUsageWindow()) {
        // remove the top of the window to allow for a new window to be possibly found.
        // this effectively implements a sliding window over a list of contiguous periods
        slidingWindow.remove();
      }
    }
    else {
      // check if a window has been found already.
      checkForUsageWindow();

      // start a new window
      slidingWindow.clear();
    }
  }

  private boolean checkForUsageWindow() {
    if (haveUsageWindow(slidingWindow)) {
      checkForLeastUsage();

      return true;
    }

    return false;
  }

  /*
   * If the `slidingWindow` list has fewer cars than the `leastUsageWindow` then a new list is set to
   * prevent mutation issues due to shared references to the same list.
   */
  private void checkForLeastUsage() {
    /*
     * cater for the case where the leastUsageWindow is empty as well, in which case we can just return the
     * contiguousPeriods list.
     */
    this.leastUsageWindow = !leastUsageWindow.isEmpty() && sumNumCars(leastUsageWindow) <= sumNumCars(slidingWindow)
      ? leastUsageWindow
      : new ArrayList<>(slidingWindow);
  }

  private static boolean haveUsageWindow(final LinkedList<Period> periods) {
    return periods.size() == WINDOW_SIZE;
  }

  /*
   * Two periods are contiguous if they are within 30 minutes of each other.
   */
  private static boolean isContiguous(final Period earlier, final Period later) {
    return earlier.timestamp().plusMinutes(30).equals(later.timestamp());
  }

  private static Long sumNumCars(final List<Period> periods) {
    return periods.stream().reduce(0L, (sum, period) -> sum + period.numCars(), Long::sum);
  }
}
