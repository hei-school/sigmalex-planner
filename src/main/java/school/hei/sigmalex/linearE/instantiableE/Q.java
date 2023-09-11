package school.hei.sigmalex.linearE.instantiableE;

import java.util.Set;

public final class Q extends NonInstantiableV implements Bounder<Void> {
  public Q(String name, Set<Bounder<?>> bounders) {
    super(name, bounders);
  }

  public Q(String name, Bounder<?>... bounders) {
    super(name, bounders);
  }

  private Q(String name, SubstitutionContext substitutionContext) {
    super(name, substitutionContext);
  }

  public Q(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable construct(String name, SubstitutionContext substitutionContext) {
    return new Q(name, substitutionContext);
  }

  @Override
  public String toString() {
    return "Q{" +
        "name='" + boundedName() + '\'' +
        '}';
  }

  @Override
  public Variable variable() {
    return this;
  }

  @Override
  public Instantiator<Void> instantiator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bounder<Void> wi(Instantiator<Void> instantiator) {
    throw new UnsupportedOperationException();
  }
}
