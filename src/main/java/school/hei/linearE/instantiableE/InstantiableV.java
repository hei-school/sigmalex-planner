package school.hei.linearE.instantiableE;

import java.util.Set;

public abstract sealed class InstantiableV<Costly>
    extends Variable implements InstantiableE<Costly> permits BounderQ {
  public InstantiableV(String name, Set<Bounder<?>> bounders) {
    super(name, bounders);
  }

  protected InstantiableV(String name, SubstitutionContext substitutionContext) {
    super(name, substitutionContext);
  }
}
