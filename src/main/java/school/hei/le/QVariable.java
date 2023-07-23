package school.hei.lp.le;

import java.util.List;

public final class QVariable extends Variable {
  public QVariable(String name, List<Variable> boundedTo) {
    super(name, boundedTo);
  }

  @Override
  public Variable toNew(String name, List<Variable> boundedTo) {
    return new QVariable(name, boundedTo);
  }
}
