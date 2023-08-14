package school.hei.sigmalex.linearE;

import school.hei.sigmalex.linearE.exception.DuplicateVariableNameException;
import school.hei.sigmalex.linearE.instantiableE.AddIE;
import school.hei.sigmalex.linearE.instantiableE.Bounder;
import school.hei.sigmalex.linearE.instantiableE.BounderValue;
import school.hei.sigmalex.linearE.instantiableE.Constant;
import school.hei.sigmalex.linearE.instantiableE.InstantiableE;
import school.hei.sigmalex.linearE.instantiableE.MultIE;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearE.instantiableE.exception.ArithmeticConversionException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static school.hei.sigmalex.linearE.instantiableE.IEFactory.addie;
import static school.hei.sigmalex.linearE.instantiableE.IEFactory.multie;
import static school.hei.sigmalex.linearP.constraint.Le.DEFAULT_EPSILON;

/**
 * normalizedLe := toLinear(weightedV) + e <= 0
 */
public final class NormalizedLE implements LinearE {

  private final Map<Variable, InstantiableE> weightedV;
  private final InstantiableE e;

  private final SubstitutionContext substitutionContext;

  public NormalizedLE(Map<Variable, InstantiableE> weightedV, InstantiableE e) {
    checkNoDuplicateNames(weightedV);
    this.weightedV = new HashMap<>();
    this.weightedV.putAll(weightedV);
    this.e = e;

    substitutionContext = new SubstitutionContext(new HashMap<>());
  }

  public NormalizedLE(double c) {
    this(Map.of(), new Constant(c));
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
      throw new DuplicateVariableNameException(duplicateNames);
    }
  }

  @Override
  public NormalizedLE normalize(SubstitutionContext substitutionContext) {
    return this;
  }

  public NormalizedLE simplify() {
    var simplifiedWeightedV = new HashMap<Variable, InstantiableE>();
    weightedV.forEach((v, c) -> simplifiedWeightedV.put(v, new Constant(c.simplify())));

    return new NormalizedLE(simplifiedWeightedV, new Constant(e.simplify()));
  }

  public Map<Variable, InstantiableE> weightedV() {
    return weightedV;
  }

  public InstantiableE e() {
    return e;
  }

  @Override
  public Set<Variable> variables() {
    return weightedV.keySet();
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

  public NormalizedLE not() {
    Map<Variable, InstantiableE> wTimesNeg1 = new HashMap<>();
    weightedV.forEach((var, val) -> wTimesNeg1.put(var, new MultIE(new Constant(-1.), val)));
    return new NormalizedLE(
        wTimesNeg1,
        new AddIE(
            multie(-1., e),
            // DEFAULT_EPSILON is publicly writable in case tuning is needed
            new Constant(DEFAULT_EPSILON)));
  }

  public NormalizedLE substitute(Bounder k, BounderValue kValue, SubstitutionContext initialSubstitutionContext) {
    initialSubstitutionContext.substitutions().forEach(
        (bounder, bounderValue) -> substitutionContext.put(bounder, bounderValue));
    substitutionContext.put(k, kValue);

    var substitutedWeightedV = new HashMap<>(weightedV);
    weightedV.forEach((v, c) -> {
      var substitutedV = v.substitute(k, kValue);
      try {
        substitutedWeightedV.put(substitutedV, c.instantiate(k, kValue, substitutionContext));
      } catch (ArithmeticConversionException ex) {
        throw new RuntimeException(ex);
      }
      if (!substitutedV.equals(v)) {
        substitutedWeightedV.remove(v);
      }
    });

    var newE = e;
    if (weightedV.containsKey(k)) {
      try {
        var instantiatedMult = multie(
            kValue.toQ(kValue.costly(), substitutionContext, k.instantiator()),
            weightedV.get(k));
        newE = addie(newE, instantiatedMult);
      } catch (ArithmeticConversionException e) {
        throw new RuntimeException(e);
      }
      substitutedWeightedV.keySet()
          .removeIf(variable -> variable.name().equals(k.variable().boundedName()));
    }

    try {
      return new NormalizedLE(substitutedWeightedV, newE.instantiate(k, kValue, substitutionContext));
    } catch (ArithmeticConversionException ex) {
      throw new RuntimeException(ex);
    }
  }

  public NormalizedLE substituteAll(SubstitutionContext substitutionContext) {
    var res = this;
    var substitutions = substitutionContext.substitutions();
    for (var bounder : substitutionContext.substitutions().keySet()) {
      res = res.substitute(bounder, substitutions.get(bounder), substitutionContext);
    }
    return res;
  }
}