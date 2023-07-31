package school.hei.linearE.instantiableE;

import java.util.Map;
import java.util.Set;

public abstract sealed class NonInstantiableV extends Variable permits Q, Z {
  public NonInstantiableV(String name, Set<Bounder> bounders) {
    super(name, bounders);
  }

  public NonInstantiableV(String name, Bounder... bounders) {
    super(name, bounders);
  }

  protected NonInstantiableV(String name, Map<Bounder, BounderValue> bounderSubstitutions) {
    super(name, bounderSubstitutions);
  }
}
