package school.hei.linearE.instantiableE;

import java.util.Map;
import java.util.Set;

public abstract sealed class NonInstantiableV<Costly> extends Variable<Costly> permits Q, Z {
  public NonInstantiableV(String name, Set<Bounder<Costly>> bounders) {
    super(name, bounders);
  }

  public NonInstantiableV(String name, Bounder<Costly>... bounders) {
    super(name, bounders);
  }

  protected NonInstantiableV(String name, Map<Bounder<Costly>, BounderValue<Costly>> bounderSubstitutions) {
    super(name, bounderSubstitutions);
  }
}
