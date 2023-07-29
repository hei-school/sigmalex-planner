package school.hei.linearE.instantiableE;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;

public record Bound(Bounder bounder, BounderValue... values) {
  public Bound(BounderZ k, int kMin, int kMax) {
    this(k, IntStream.range(kMin, kMax + 1).mapToObj(Constant::new).toArray(Constant[]::new));
  }

  public static List<Bound> sort(Bound[] bounds) {
    return Arrays.stream(bounds)
        .sorted(comparing(bound -> bound.bounder().variable().getName()))
        .toList();
  }

}
