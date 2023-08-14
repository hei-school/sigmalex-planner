package school.hei.sigmalex.linearE.instantiableE;

import java.util.Set;

public abstract sealed class NonInstantiableV extends Variable permits Q, Z, B {
  public NonInstantiableV(String name, Set<Bounder<?>> bounders) {
    super(name, bounders);
  }

  public NonInstantiableV(String name, Bounder<?>... bounders) {
    super(name, bounders);
  }

  protected NonInstantiableV(String name, SubstitutionContext substitutionContext) {
    super(name, substitutionContext);
  }
}
