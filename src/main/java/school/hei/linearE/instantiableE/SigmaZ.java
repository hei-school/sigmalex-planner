package school.hei.linearE.instantiableE;

import java.util.List;

public final class SigmaZ extends InstantiableV {

  public SigmaZ(String name) {
    super(name, List.of());
  }

  @Override
  public Variable toNew(String name, List<Variable> boundedTo) {
    throw new RuntimeException("Not implemented");
  }

  @Override
  public InstantiableE simplify() {
    return this;
  }
}