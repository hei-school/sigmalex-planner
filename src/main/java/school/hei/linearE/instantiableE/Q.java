package school.hei.linearE.instantiableE;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Q extends NonInstantiableV implements Bounder {
  public Q(String name, Set<Bounder> bounders) {
    super(name, bounders);
  }

  public Q(String name, Bounder... bounders) {
    this(name, Arrays.stream(bounders).collect(toSet()));
  }

  private Q(String name, Map<Bounder, BounderValue> bounderSubstitutions) {
    super(name, bounderSubstitutions);
  }

  @Override
  public Variable toNew(Map<Bounder, BounderValue> bounderSubstitutions) {
    return new Q(name, bounderSubstitutions);
  }

  public Q(String name) {
    this(name, Set.of());
  }

  @Override
  public String toString() {
    return "Q{" +
        "name='" + boundedName() + '\'' +
        '}';
  }

  @Override
  public Variable variable() {
    return this;
  }
}
