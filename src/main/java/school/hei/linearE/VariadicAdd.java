package school.hei.linearE;

import java.util.Arrays;

public record VariadicAdd(LinearE... leList) implements LinearE {
  @Override
  public Normalized normalize() {
    return Arrays.stream(leList)
        .reduce(Add::new)
        .orElse(new Mono(0.))
        .normalize();
  }
}
