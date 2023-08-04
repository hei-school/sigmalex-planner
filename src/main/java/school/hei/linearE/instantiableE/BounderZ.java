package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.ArithmeticConversionException;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class BounderZ<Costly> extends InstantiableV<Costly> implements Bounder<Costly> {

  private final Function<Costly, InstantiableE<Costly>> instantiator;

  public BounderZ(String name) {
    super(name, Set.of());
    instantiator = bounderValue -> null;
  }

  private BounderZ(
      String name,
      Map<Bounder<? extends Costly>, BounderValue<Costly>> bounderSubstitutions,
      Function<Costly, InstantiableE<Costly>> instantiator) {
    super(name, bounderSubstitutions);
    this.instantiator = instantiator;
  }

  private BounderZ(String name, Map<Bounder<? extends Costly>, BounderValue<Costly>> bounderSubstitutions) {
    this(name, bounderSubstitutions, bounderValue -> null);
  }

  @Override
  public double simplify() {
    throw new UnsupportedOperationException("Earlier instantiation should have prevented current simplification ");
  }

  @Override
  public InstantiableE<Costly> instantiate(Bounder<Costly> bounder, BounderValue<Costly> bounderValue)
      throws ArithmeticConversionException {
    if (this.equals(bounder)) {
      return bounderValue.toQ(bounderValue.costly(), bounder.instantiator());
    }
    return this;
  }

  @Override
  public Variable<Costly> variable() {
    return this;
  }

  @Override
  public Function<Costly, InstantiableE<Costly>> instantiator() {
    return instantiator;
  }


  @Override
  public BounderZ<Costly> wi(Function<Costly, InstantiableE<Costly>> instantiator) {
    return new BounderZ<>(name, bounderSubstitutions, instantiator);
  }

  @Override
  public Variable<Costly> toNew(Map<Bounder<? extends Costly>, BounderValue<Costly>> bounderSubstitutions) {
    return new BounderZ<>(name, bounderSubstitutions);
  }

  @Override
  public String toString() {
    return "BounderZ{" +
        "name='" + boundedName() + '\'' +
        '}';
  }
}