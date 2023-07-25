package school.hei.linearE.instantiableE;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Z extends NonInstantiableV implements Bounder {
  public Z(String name, Set<Bounder> bounders) {
    super(name, bounders);
  }

  public Z(String name, Bounder... bounders) {
    this(name, Arrays.stream(bounders).collect(toSet()));
  }

  public Z(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable toNew(String name, Set<Bounder> bounders) {
    return new Z(name, bounders);
  }

  @Override
  public String toString() {
    return "Z{" +
        "name='" + name + '\'' +
        ", boundedTo=" + bounders +
        '}';
  }

  @Override
  public Variable variable() {
    return this;
  }
}
