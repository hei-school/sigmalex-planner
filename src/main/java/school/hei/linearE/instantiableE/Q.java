package school.hei.linearE.instantiableE;

import java.util.List;

public final class Q extends NonInstantiableV implements Bounder {
  public Q(String name, List<Bounder> boundedTo) {
    super(name, boundedTo);
  }

  public Q(String name) {
    this(name, List.of());
  }

  @Override
  public Variable toNew(String name, List<Bounder> boundedTo) {
    return new Q(name, boundedTo);
  }

  @Override
  public String toString() {
    return "Q{" +
        "name='" + name + '\'' +
        ", boundedTo=" + boundedTo +
        '}';
  }

  @Override
  public Variable variable() {
    return this;
  }
}
