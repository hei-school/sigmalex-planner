package school.hei.linearE.instantiableE;

import java.util.Set;

public abstract sealed class NonInstantiableV extends Variable permits Q, Z {
  public NonInstantiableV(String name, Set<Bounder> bounders) {
    super(name, bounders);
  }
}
