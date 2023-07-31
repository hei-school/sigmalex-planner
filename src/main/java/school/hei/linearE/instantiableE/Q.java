package school.hei.linearE.instantiableE;

import java.util.Map;
import java.util.Set;

public final class Q extends NonInstantiableV implements Bounder {
  public Q(String name, Set<Bounder> bounders) {
    super(name, bounders);
  }

  public Q(String name, Bounder... bounders) {
    super(name, bounders);
  }

  private Q(String name, Map<Bounder, BounderValue> bounderSubstitutions) {
    super(name, bounderSubstitutions);
  }

  public Q(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable toNew(Map<Bounder, BounderValue> bounderSubstitutions) {
    return new Q(name, bounderSubstitutions);
  }

  @Override
  public String toString() {
    return "Q{" +
        "name='" + boundedName() + '\'' +
        '}';
  }

  @Override
  public Variable variable() {
    return this;
  }
}
