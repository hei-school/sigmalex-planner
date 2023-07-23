package school.hei.linearE.instantiableE;

import java.util.List;

public sealed abstract class Variable permits InstantiableV, NonInstantiableV {

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
