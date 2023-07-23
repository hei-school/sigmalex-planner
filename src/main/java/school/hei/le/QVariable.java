package school.hei.le;

import java.util.List;

public final class QVariable extends Variable {
  public QVariable(String name, List<Variable> boundedTo) {
    super(name, boundedTo);
  }

  public QVariable(String name) {
    this(name, List.of());
  }

  @Override
  public Variable toNew(String name, List<Variable> boundedTo) {
    return new QVariable(name, boundedTo);
  }
}
