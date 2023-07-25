package school.hei.linearE.instantiableE;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public sealed abstract class Variable permits InstantiableV, NonInstantiableV {

  protected final String name;
  protected final List<Bounder> bounders;

  public Variable(String name, List<Bounder> bounders) {
    this.name = name;
    this.bounders = bounders.stream()
        .sorted(comparing((Bounder bounder) -> bounder.variable().name))
        .collect(toList());
  }

  public Variable(String name, Bounder... bounders) {
    this(name, Arrays.stream(bounders).toList());
  }

  public String getName() {
    return name;
  }

  public List<Bounder> getBounders() {
    return bounders;
  }

  public abstract Variable toNew(String name, List<Bounder> boundedTo);

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
