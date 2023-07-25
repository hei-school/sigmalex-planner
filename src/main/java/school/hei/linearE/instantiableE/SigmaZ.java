package school.hei.linearE.instantiableE;

import java.util.Set;

public final class SigmaZ extends InstantiableV implements Bounder {

  public SigmaZ(String name) {
    super(name, Set.of());
  }

  @Override
  public Variable toNew(String name, Set<Bounder> bounders) {
    throw new RuntimeException("Not implemented");
  }

  @Override
  public InstantiableE simplify() {
    return this;
  }

  @Override
  public Variable variable() {
    return this;
  }
}