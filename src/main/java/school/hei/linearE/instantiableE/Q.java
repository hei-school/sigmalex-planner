package school.hei.linearE.instantiableE;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Q extends NonInstantiableV implements Bounder {
  public Q(String name, Set<Bounder> bounders) {
    super(name, bounders);
  }

  public Q(String name, Bounder... bounders) {
    this(name, Arrays.stream(bounders).collect(toSet()));
  }

  public Q(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable toNew(String name, Set<Bounder> bounders) {
    return new Q(name, bounders);
  }

  @Override
  public String toString() {
    return "Q{" +
        "name='" + name + '\'' +
        ", boundedTo=" + bounders +
        '}';
  }

  @Override
  public Variable variable() {
    return this;
  }
}
