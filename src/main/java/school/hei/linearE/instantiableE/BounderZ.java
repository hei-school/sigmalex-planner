package school.hei.linearE.instantiableE;

import java.util.Map;
import java.util.Set;

public final class BounderZ extends InstantiableV implements Bounder {

  public BounderZ(String name) {
    super(name, Set.of());
  }

  private BounderZ(String name, Map<Bounder, BounderValue> bounderSubstitutions) {
    super(name, bounderSubstitutions);
  }

  @Override
  public InstantiableE simplify() {
    return this;
  }

  @Override
  public Variable variable() {
    return this;
  }

  @Override
  public Variable toNew(Map<Bounder, BounderValue> bounderSubstitutions) {
    return new BounderZ(name, bounderSubstitutions);
  }

  @Override
  public String toString() {
    return "BounderZ{" +
        "name='" + boundedName() + '\'' +
        '}';
  }
}