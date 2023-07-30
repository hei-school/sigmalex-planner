package school.hei.linearE;

import school.hei.linearE.instantiableE.AddIE;
import school.hei.linearE.instantiableE.ArithmeticConversionException;
import school.hei.linearE.instantiableE.Bounder;
import school.hei.linearE.instantiableE.BounderValue;
import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.MultIE;
import school.hei.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.linearP.constraint.Le.DEFAULT_EPSILON;

/**
 * normalizedLe := toLinear(weightedV) + e <= 0
 */
public final class NormalizedLE implements LinearE {

  private final Map<Variable, InstantiableE> weightedV;
  private final InstantiableE e;


  public NormalizedLE(Map<Variable, InstantiableE> weightedV, InstantiableE e) {
    this.weightedV = new HashMap<>();
    weightedV.forEach((v, eOfV) -> this.weightedV.put(v, eOfV.simplify()));
    checkNoDuplicateNames(weightedV);
    this.e = e.simplify();
  }

  private static void checkNoDuplicateNames(Map<Variable, InstantiableE> weightedV) {
    Set<String> distinctNames = new HashSet<>();
    var variablesNames = weightedV.keySet().stream()
        .map(Variable::boundedName)
        .toList();
    var duplicateNames = variablesNames.stream()
        .filter(name -> !distinctNames.add(name))
        .collect(toSet());
    if (!duplicateNames.isEmpty()) {
      throw new DuplicateVariableName(duplicateNames);
    }
  }

  public NormalizedLE(double c) {
    this(Map.of(), new Constant(c));
  }

  @Override
  public NormalizedLE normalize() {
    return this;
  }

  @Override
  public Set<Variable> variables() {
    return weightedV.keySet();
  }

  public Map<Variable, InstantiableE> weightedV() {
    return weightedV;
  }

  public InstantiableE e() {
    return e;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NormalizedLE that = (NormalizedLE) o;
    return Objects.equals(weightedV, that.weightedV) && Objects.equals(e, that.e);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weightedV, e);
  }

  @Override
  public String toString() {
    return "Normalized{" +
        "weightedV=" + weightedV +
        ", e=" + e +
        '}';
  }

  public static class DuplicateVariableName extends RuntimeException {
    public DuplicateVariableName(Set<String> duplicateNames) {
      super(duplicateNames.toString());
    }
  }

  public NormalizedLE not() {
    Map<Variable, InstantiableE> wTimesNeg1 = new HashMap<>();
    weightedV.forEach((var, val) -> wTimesNeg1.put(var, new MultIE(new Constant(-1.), val)));
    return new NormalizedLE(
        wTimesNeg1,
        new AddIE(
            new MultIE(new Constant(-1.), e),
            // DEFAULT_EPSILON is publicly writable in case tuning is needed
            new Constant(DEFAULT_EPSILON)));
  }

  public NormalizedLE substitute(Bounder k, BounderValue kValue) {
    var substitutedWeightedV = new HashMap<>(weightedV);
    weightedV.forEach((v, c) -> {
      var substitutedV = v.substitute(k, kValue);
      substitutedWeightedV.put(substitutedV, c);
      if (!substitutedV.equals(v)) {
        substitutedWeightedV.remove(v);
      }
    });

    var newE = e;
    if (weightedV.containsKey(k)) {
      try {
        newE = new AddIE(newE, new MultIE(kValue.toArithmeticValue(), weightedV.get(k)));
      } catch (ArithmeticConversionException e) {
        throw new RuntimeException(e);
      }
      substitutedWeightedV.keySet()
          .removeIf(variable -> variable.name().equals(k.variable().boundedName()));
    }

    return new NormalizedLE(substitutedWeightedV, newE);
  }
}