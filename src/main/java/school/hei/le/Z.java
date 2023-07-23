package school.hei.le;

import java.util.List;

public final class Z extends Variable {
  public Z(String name, List<Variable> boundedTo) {
    super(name, boundedTo);
  }

  public Z(String name) {
    this(name, List.of());
  }

  @Override
  public Variable toNew(String name, List<Variable> boundedTo) {
    return new Z(name, boundedTo);
  }
}
