package school.hei.linearE.instantiableE;

import java.util.List;

public abstract sealed class NonInstantiableV extends Variable permits Q, Z {
  public NonInstantiableV(String name, List<Variable> boundedTo) {
    super(name, boundedTo);
  }
}
