package school.hei.lp.le;

import java.util.List;

public final class ZVariable extends Variable {
  public ZVariable(String name, List<Variable> boundedTo) {
    super(name, boundedTo);
  }

  @Override
  public Variable toNew(String name, List<Variable> boundedTo) {
    return new ZVariable(name, boundedTo);
  }
}
