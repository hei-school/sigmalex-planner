package school.hei.linearE.instantiableE;

import java.util.Map;
import java.util.Set;

public abstract sealed class InstantiableV<Costly>
    extends Variable<Costly> implements InstantiableE<Costly> permits BounderZ {
  public InstantiableV(String name, Set<Bounder<? extends Costly>> bounders) {
    super(name, bounders);
  }

  protected InstantiableV(String name, Map<Bounder<? extends Costly>, BounderValue<Costly>> bounderSubstitutions) {
    super(name, bounderSubstitutions);
  }
}
