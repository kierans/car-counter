package org.quasar.carcounter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.common.io.Files;

public class App {
  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      throw new IllegalArgumentException("Usage: java -jar carcounter.jar <file>");
    }

    final Lexer lexer = new Lexer();
    final List<Period> periods = lexer.parse(Files.readLines(new File(args[0]), StandardCharsets.UTF_8));

    final CarCounter carCounter = new CarCounter(periods);

    System.out.printf("%d%n", carCounter.totalCars());
    printList(carCounter.carsPerDay());
    printList(carCounter.topUsagePeriods());
    printList(carCounter.leastUsageWindow());
  }

  private static void printList(final List<? extends Formattable> list) {
    list.stream().map(Formattable::toFormat).forEach(System.out::println);
  }
}
