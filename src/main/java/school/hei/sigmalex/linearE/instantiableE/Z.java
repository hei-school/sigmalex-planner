package school.hei.sigmalex.linearE.instantiableE;

import java.util.Set;

public final class Z extends NonInstantiableV implements Bounder<Void> {
  public Z(String name, Set<Bounder<?>> bounders) {
    super(name, bounders);
  }

  public Z(String name, Bounder<?>... bounders) {
    super(name, bounders);
  }

  private Z(String name, SubstitutionContext substitutionContext) {
    super(name, substitutionContext);
  }

  public Z(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable toNew(SubstitutionContext substitutionContext) {
    return new Z(name, substitutionContext);
  }

  @Override
  public String toString() {
    return "Z{" +
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
