package school.hei.linearE.instantiableE;

import java.util.Set;

public final class Q extends NonInstantiableV<Object> implements Bounder<Object> {
  public Q(String name, Set<Bounder<?>> bounders) {
    super(name, bounders);
  }

  public Q(String name, Bounder<?>... bounders) {
    super(name, bounders);
  }

  private Q(String name, SubstitutionContext<Object> substitutionContext) {
    super(name, substitutionContext);
  }

  public Q(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable<Object> toNew(SubstitutionContext<Object> substitutionContext) {
    return new Q(name, substitutionContext);
  }

  @Override
  public String toString() {
    return "Q{" +
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
