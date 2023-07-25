package school.hei.linearE.instantiableE;

import java.util.Set;

public abstract sealed class InstantiableV
    extends Variable implements InstantiableE permits SigmaZ {
  public InstantiableV(String name, Set<Bounder> bounders) {
    super(name, bounders);
  }
}
