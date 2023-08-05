package school.hei.linearE.instantiableE;

import java.util.Set;

public final class Z<Costly> extends NonInstantiableV<Costly> implements Bounder<Costly> {
  public Z(String name, Set<Bounder<? extends Costly>> bounders) {
    super(name, bounders);
  }

  @SafeVarargs
  public Z(String name, Bounder<? extends Costly>... bounders) {
    super(name, bounders);
  }

  private Z(String name, SubstitutionContext<Costly> substitutionContext) {
    super(name, substitutionContext);
  }

  public Z(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable<Costly> toNew(SubstitutionContext<Costly> substitutionContext) {
    return new Z<>(name, substitutionContext);
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
  public Instantiator<Costly> instantiator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bounder<Costly> wi(Instantiator<Costly> instantiator) {
    throw new UnsupportedOperationException();
  }
}
