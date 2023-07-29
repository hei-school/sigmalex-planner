package school.hei.linearE.instantiableE;

import java.util.stream.IntStream;

public record Bound(Bounder bounder, BounderValue... values) {
  public Bound(SigmaZ k, int kMin, int kMax) {
    this(k, IntStream.range(kMin, kMax + 1).mapToObj(Constant::new).toArray(Constant[]::new));
  }
}
