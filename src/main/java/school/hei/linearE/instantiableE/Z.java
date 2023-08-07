package school.hei.linearE.instantiableE;

import java.util.Set;

public final class Z extends NonInstantiableV<Object> implements Bounder<Object> {
  public Z(String name, Set<Bounder<?>> bounders) {
    super(name, bounders);
  }

  public Z(String name, Bounder<?>... bounders) {
    super(name, bounders);
  }

  private Z(String name, SubstitutionContext<Object> substitutionContext) {
    super(name, substitutionContext);
  }

  public Z(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable<Object> toNew(SubstitutionContext<Object> substitutionContext) {
    return new Z(name, substitutionContext);
  }

  @Override
  public String toString() {
    return "Z{" +
        "name='" + boundedName() + '\'' +
        '}';
  }

  @Override
  public Variable<Object> variable() {
    return this;
  }

  @Override
  public Instantiator<Object> instantiator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bounder<Object> wi(Instantiator<Object> instantiator) {
    throw new UnsupportedOperationException();
  }
}
