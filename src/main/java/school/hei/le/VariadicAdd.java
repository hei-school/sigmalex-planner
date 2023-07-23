package school.hei.lp.le;

import java.util.Arrays;

public record VariadicAdd(LinearExpression... leList) implements LinearExpression {
  @Override
  public Normalized normalize() {
    return Arrays.stream(leList)
        .reduce((Add::new))
        .orElse(new Mono(0.))
        .normalize();
  }
}
