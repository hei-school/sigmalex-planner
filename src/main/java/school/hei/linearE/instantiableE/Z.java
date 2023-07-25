package school.hei.linearE.instantiableE;

import java.util.Arrays;
import java.util.List;

public final class Z extends NonInstantiableV implements Bounder {
  public Z(String name, List<Bounder> bounders) {
    super(name, bounders);
  }

  public Z(String name, Bounder... bounders) {
    this(name, Arrays.stream(bounders).toList());
  }

  public Z(String name) {
    this(name, List.of());
  }

  @Override
  public Variable toNew(String name, List<Bounder> boundedTo) {
    return new Z(name, boundedTo);
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
