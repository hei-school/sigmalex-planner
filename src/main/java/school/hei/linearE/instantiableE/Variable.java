package school.hei.linearE.instantiableE;

import java.util.List;
import java.util.Objects;

public sealed abstract class Variable permits InstantiableV, NonInstantiableV {

  protected final String name;
  protected final List<Variable> boundedTo;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Variable variable = (Variable) o;
    return Objects.equals(name, variable.name) && Objects.equals(boundedTo, variable.boundedTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, boundedTo);
  }
}
