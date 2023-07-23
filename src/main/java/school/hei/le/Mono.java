package school.hei.lp.le;

import java.util.Map;
import java.util.Optional;

public record Mono(double c, Optional<Variable> optV) implements LinearExpression {

  public Mono(double c) {
    this(c, Optional.empty());
  }

  public Mono(Variable v) {
    this(1, Optional.of(v));
  }

  @Override
  public Normalized normalize() {
    return optV
        .map(v -> new Normalized(Map.of(v, c), 0))
        .orElseGet(() -> new Normalized(Map.of(), c));
  }
}
