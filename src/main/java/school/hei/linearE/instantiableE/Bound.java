package school.hei.linearE.instantiableE;

import java.util.function.Function;
import java.util.stream.IntStream;

public record Bound<T>(Bounder<T> bounder, BounderValue... values) {
  public Bound(BounderZ k, int kMin, int kMax) {
    this(k, IntStream.range(kMin, kMax + 1).mapToObj(Constant::new).toArray(Constant[]::new));
  }

  public Bound<T> wi(Function<T, Double> instantiator) {
    return new Bound<>(bounder.wi(costly -> new Constant(instantiator.apply(costly))), values);
  }
}
