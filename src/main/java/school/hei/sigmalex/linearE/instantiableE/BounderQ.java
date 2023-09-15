package school.hei.sigmalex.linearE.instantiableE;

import school.hei.sigmalex.linearE.instantiableE.exception.ArithmeticConversionException;
import school.hei.sigmalex.linearE.instantiableE.exception.MissingInstantiationException;

import java.util.Set;

public final class BounderQ<Costly> extends InstantiableV<Costly> implements Bounder<Costly> {

  private final Instantiator<Costly> instantiator;

  public BounderQ(String name) {
    super(name, Set.of());
    instantiator = (costly, substitutionCtx) -> null;
  }

  private BounderQ(
      String name,
      SubstitutionContext substitutionContext,
      Instantiator<Costly> instantiator) {
    super(name, substitutionContext);
    this.instantiator = instantiator;
  }

  private BounderQ(String name, SubstitutionContext substitutionContext) {
    this(name, substitutionContext, (costly, lambdaSubstitutionCtx) -> null);
  }

  @Override
  public double simplify() {
    throw new MissingInstantiationException(
        "Earlier instantiation should have prevented current simplification: " + name);
  }

  @Override
  public InstantiableE<?> instantiate(
      Bounder<Costly> bounder, BounderValue<Costly> bounderValue, SubstitutionContext substitutionContext)
      throws ArithmeticConversionException {
    if (this.equals(bounder)) {
      return bounderValue.toQ(bounderValue.costly(), substitutionContext, bounder.instantiator());
    }
    return this;
  }

  @Override
  public Variable variable() {
    return this;
  }

  @Override
  public Instantiator<Costly> instantiator() {
    return instantiator;
  }


  @Override
  public BounderQ<Costly> wi(Instantiator<Costly> instantiator) {
    return new BounderQ<>(name, substitutionContext, instantiator);
  }

  @Override
  public Variable construct(String name, SubstitutionContext substitutionContext) {
    return new BounderQ<>(name, substitutionContext);
  }

  @Override
  public String toString() {
    return "BounderZ{" +
        "name='" + boundedName() + '\'' +
        '}';
  }
}