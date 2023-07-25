package school.hei.linearE.instantiableE;

import java.util.Arrays;
import java.util.List;

public final class Q extends NonInstantiableV implements Bounder {
  public Q(String name, List<Bounder> bounders) {
    super(name, bounders);
  }

  public Q(String name, Bounder... bounders) {
    this(name, Arrays.stream(bounders).toList());
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
        ", boundedTo=" + bounders +
        '}';
  }

  @Override
  public Variable variable() {
    return this;
  }
}
