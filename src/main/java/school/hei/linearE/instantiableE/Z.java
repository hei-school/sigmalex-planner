package school.hei.linearE.instantiableE;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class Z<Costly> extends NonInstantiableV<Costly> implements Bounder<Costly> {
  public Z(String name, Set<Bounder<Costly>> bounders) {
    super(name, bounders);
  }

  public Z(String name, Bounder<Costly>... bounders) {
    super(name, bounders);
  }

  private Z(String name, Map<Bounder<Costly>, BounderValue<Costly>> bounderSubstitutions) {
    super(name, bounderSubstitutions);
  }

  public Z(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable<Costly> toNew(Map<Bounder<Costly>, BounderValue<Costly>> bounderSubstitutions) {
    return new Z<>(name, bounderSubstitutions);
  }

  @Override
  public String toString() {
    return "Z{" +
        "name='" + boundedName() + '\'' +
        '}';
  }

  @Override
  public Variable<Costly> variable() {
    return this;
  }

  @Override
  public Function<Costly, InstantiableE<Costly>> instantiator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bounder<Costly> wi(Function<Costly, InstantiableE<Costly>> instantiator) {
    throw new UnsupportedOperationException();
  }
}
