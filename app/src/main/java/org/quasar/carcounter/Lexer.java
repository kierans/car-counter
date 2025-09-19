package org.quasar.carcounter;

import java.util.List;
import java.util.regex.Pattern;

public class Lexer {
  private final Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2}:\\d{2})\\s*(\\d+)");

  public Period parse(final String data) {
    final var matcher = pattern.matcher(data);
    if (matcher.matches()) {
      return new Period(matcher.group(1), matcher.group(2), Long.valueOf(matcher.group(3)));
    }
    else {
      throw new IllegalArgumentException("Invalid data: " + data);
    }
  }

  public List<Period> parse(final List<String> data) {
    return data.stream().map(this::parse).toList();
  }
}
