package school.hei.sigmalex.linearE;

import lombok.SneakyThrows;
import school.hei.sigmalex.concurrency.Workers;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.sigmalex.linearE.instantiableE.IEFactory.addie;

public record Add(List<LinearE> leList) implements LinearE {

  @SneakyThrows
  @Override
  public NormalizedLE normalize() {
    return Workers.submit(() -> leList.parallelStream()
            .map(LinearE::normalize)
            .reduce(new NormalizedLE(0), this::addToNormalized))
        .get();
  }

  @Override
  public NormalizedLE substitute(SubstitutionContext substitutionContext) {
    return new Add(leList.stream()
        .map(linearE -> linearE.substitute(substitutionContext))
        .toList())
        .normalize();
  }

  private NormalizedLE addToNormalized(NormalizedLE actual, NormalizedLE toAdd) {
    var newWeightedV = new HashMap<>(toAdd.weightedV());
    actual.weightedV().forEach((v, vValue) -> {
      if (newWeightedV.containsKey(v)) {
        newWeightedV.put(v, addie(newWeightedV.get(v), vValue));
      } else {
        newWeightedV.put(v, vValue);
      }
    });
    return new NormalizedLE(newWeightedV, addie(actual.e(), toAdd.e()));
  }

  @Override
  public Set<Variable> variables() {
    return leList.stream().flatMap(le -> le.variables().stream()).collect(toSet());
  }
}
