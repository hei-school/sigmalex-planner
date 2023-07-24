package school.hei.linearE.instantiableE;

import java.util.List;

public final class Z extends NonInstantiableV implements Bounder {
  public Z(String name, List<Bounder> boundedTo) {
    super(name, boundedTo);
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
        ", boundedTo=" + boundedTo +
        '}';
  }

  @Override
  public Variable variable() {
    return this;
  }
}
