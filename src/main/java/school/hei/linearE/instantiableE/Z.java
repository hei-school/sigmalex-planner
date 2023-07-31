package school.hei.linearE.instantiableE;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Z extends NonInstantiableV implements Bounder {
  public Z(String name, Set<Bounder> bounders) {
    super(name, bounders);
  }

  public Z(String name, Bounder... bounders) {
    super(name, bounders);
  }

  private Z(String name, Map<Bounder, BounderValue> bounderSubstitutions) {
    super(name, bounderSubstitutions);
  }

  @Override
  public Variable toNew(Map<Bounder, BounderValue> bounderSubstitutions) {
    return new Z(name, bounderSubstitutions);
  }

  public Z(String name) {
    this(name, Set.of());
  }

  @Override
  public String toString() {
    return "Z{" +
        "name='" + boundedName() + '\'' +
        '}';
  }

  @Override
  public Variable variable() {
    return this;
  }
}
