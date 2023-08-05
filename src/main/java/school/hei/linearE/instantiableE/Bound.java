package school.hei.linearE.instantiableE;

import java.util.function.Function;
import java.util.stream.IntStream;

public record Bound<Costly>(Bounder<Costly> bounder, BounderValue<Costly>... values) {
  public Bound(BounderZ k, int kMin, int kMax) {
    this(k, IntStream.range(kMin, kMax + 1).mapToObj(Constant::new).toArray(Constant[]::new));
  }

  public Bound<Costly> wi(Function<Costly, InstantiableE<Costly>> instantiator) {
    return new Bound<>(bounder.wi((costly, ctx) -> instantiator.apply(costly)), values);
  }

  public Bound<Costly> wi(Instantiator<Costly> instantiator) {
    return new Bound<>(bounder.wi(instantiator), values);
  }


  public Bound<Costly> wiq(Function<Costly, Double> instantiator) {
    return new Bound<>(bounder.wi((costly, ctx) -> new Constant<>(instantiator.apply(costly))), values);
  }
}
