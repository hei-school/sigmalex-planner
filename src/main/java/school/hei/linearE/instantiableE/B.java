package school.hei.linearE.instantiableE;

import java.util.Set;

public final class B extends NonInstantiableV implements Bounder<Void> {
  public B(String name, Set<Bounder<?>> bounders) {
    super(name, bounders);
  }

  public B(String name, Bounder<?>... bounders) {
    super(name, bounders);
  }

  private B(String name, SubstitutionContext substitutionContext) {
    super(name, substitutionContext);
  }

  public B(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable toNew(SubstitutionContext substitutionContext) {
    return new B(name, substitutionContext);
  }

  @Override
  public String toString() {
    return "B{" +
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
