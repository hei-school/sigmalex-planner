package school.hei.linearE.instantiableE;

import java.util.Set;

public final class Q<Costly> extends NonInstantiableV<Costly> implements Bounder<Costly> {
  public Q(String name, Set<Bounder<? extends Costly>> bounders) {
    super(name, bounders);
  }

  @SafeVarargs
  public Q(String name, Bounder<? extends Costly>... bounders) {
    super(name, bounders);
  }

  private Q(String name, SubstitutionContext<Costly> substitutionContext) {
    super(name, substitutionContext);
  }

  public Q(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable<Costly> toNew(SubstitutionContext<Costly> substitutionContext) {
    return new Q(name, substitutionContext);
  }

  @Override
  public String toString() {
    return "Q{" +
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
