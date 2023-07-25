package school.hei.linearE.instantiableE;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public sealed abstract class Variable permits InstantiableV, NonInstantiableV {

  protected final String name;
  protected final Set<Bounder> bounders;

  public Variable(String name, Set<Bounder> bounders) {
    this.name = name;
    this.bounders = bounders;
  }

  public Variable(String name, Bounder... bounders) {
    this(name, Arrays.stream(bounders).collect(Collectors.toSet()));
  }

  public String getName() {
    return name;
  }

  public Set<Bounder> getBounders() {
    return bounders;
  }

  public abstract Variable toNew(String name, Set<Bounder> bounders);

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Variable variable = (Variable) o;
    return Objects.equals(name, variable.name) && Objects.equals(bounders, variable.bounders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, bounders);
  }
}
