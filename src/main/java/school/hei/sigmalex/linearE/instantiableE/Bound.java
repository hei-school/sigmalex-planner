package school.hei.sigmalex.linearE.instantiableE;

import school.hei.sigmalex.set.CartesianProduct;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

public record Bound<Costly>(Bounder<Costly> bounder, BounderValue<?>... values) {
  @SafeVarargs
  public Bound {
  }

  public Bound(BounderQ<Costly> k, int kMin, int kMax) {
    this(k, IntStream.range(kMin, kMax + 1).mapToObj(Constant::new).toArray(Constant[]::new));
  }

  public static Optional<Set<SubstitutionContext>> toBSubstitutionContexts(Bound<?>... bounds) {
    for (var bound : bounds) {
      if (bound.values.length == 0) {
        return Optional.empty();
      }
    }

    var bounderAndValuesArray = Arrays.stream(bounds)
        .map(bound -> Arrays.stream(bound.values())
            .map(bounderValue -> new BoundedValue(bound.bounder(), bounderValue))
            .collect(toSet()))
        .toArray(Set[]::new);
    var cp = CartesianProduct.cartesianProduct(bounderAndValuesArray);

    var substitutionContexts = new HashSet<SubstitutionContext>();
    for (var bounderAndValues : cp) {
      if (bounderAndValues instanceof BoundedValue) {
        substitutionContexts.add(new SubstitutionContext(
            Map.of(((BoundedValue) bounderAndValues).bounder(), ((BoundedValue) bounderAndValues).bounderValue())));
        continue;
      }
      var substitutionContextMap = new HashMap<Bounder<?>, BounderValue<?>>();
      Set<BoundedValue> bounderAndValuesAsSet = (Set) bounderAndValues;
      bounderAndValuesAsSet.forEach(boundedValue ->
          substitutionContextMap.put(boundedValue.bounder(), boundedValue.bounderValue()));
      substitutionContexts.add(new SubstitutionContext(substitutionContextMap));
    }

    return Optional.of(substitutionContexts);
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
