package school.hei.linearE.instantiableE;

import java.util.List;

public final class Q extends NonInstantiableV {
  public Q(String name, List<Variable> boundedTo) {
    super(name, boundedTo);
  }

  public Q(String name) {
    this(name, List.of());
  }

  @Override
  public Variable toNew(String name, List<Variable> boundedTo) {
    return new Q(name, boundedTo);
  }
}
