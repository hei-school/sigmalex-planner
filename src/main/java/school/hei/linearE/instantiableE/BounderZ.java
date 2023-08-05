package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;
import school.hei.linearE.instantiableE.exception.MissingInstantiationException;

import java.util.Map;
import java.util.Set;

public final class BounderZ<Costly> extends InstantiableV<Costly> implements Bounder<Costly> {

  private final Instantiator<Costly> instantiator;

  public BounderZ(String name) {
    super(name, Set.of());
    instantiator = (costly, substitutionCtx) -> null;
  }

  private BounderZ(
      String name,
      SubstitutionContext<Costly> substitutionContext,
      Instantiator<Costly> instantiator) {
    super(name, substitutionContext);
    this.instantiator = instantiator;
  }

  private BounderZ(String name, SubstitutionContext<Costly> substitutionContext) {
    this(name, substitutionContext, (costly, lambdaSubstitutionCtx) -> null);
  }

  @Override
  public double simplify() {
    throw new MissingInstantiationException("Earlier instantiation should have prevented current simplification");
  }

  @Override
  public InstantiableE<Costly> instantiate(Bounder<Costly> bounder, BounderValue<Costly> bounderValue)
      throws ArithmeticConversionException {
    if (this.equals(bounder)) {
      return bounderValue.toQ(
          bounderValue.costly(),

          //TODO: we lose previous substitutions. Isn't this catastrophic doctor?
          new SubstitutionContext(Map.of(bounder, bounderValue)),

          bounder.instantiator());
    }
    return this;
  }

  @Override
  public Variable<Costly> variable() {
    return this;
  }

  @Override
  public Instantiator<Costly> instantiator() {
    return instantiator;
  }


  @Override
  public BounderZ<Costly> wi(Instantiator<Costly> instantiator) {
    return new BounderZ<>(name, substitutionContext, instantiator);
  }

  @Override
  public Variable<Costly> toNew(SubstitutionContext<Costly> substitutionContext) {
    return new BounderZ<>(name, substitutionContext);
  }

  @Override
  public String toString() {
    return "BounderZ{" +
        "name='" + boundedName() + '\'' +
        '}';
  }
}