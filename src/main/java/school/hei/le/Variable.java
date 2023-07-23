package school.hei.le;

import java.util.List;

public sealed abstract class Variable permits Z, Q {

  private final String name;
  private final List<Variable> boundedTo;

  public Variable(String name, List<Variable> boundedTo) {
    this.name = name;
    this.boundedTo = boundedTo;
  }

  public String getName() {
    return name;
  }

  public List<Variable> getBoundedTo() {
    return boundedTo;
  }

  public abstract Variable toNew(String name, List<Variable> boundedTo);
}
