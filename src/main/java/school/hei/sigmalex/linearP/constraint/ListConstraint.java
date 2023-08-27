package school.hei.sigmalex.linearP.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@AllArgsConstructor
public abstract sealed class ListConstraint extends Constraint permits And, Or {

  @Getter
  protected final List<Constraint> constraints;

  @Override
  public Set<Variable> variables() {
    return constraints.stream()
        .flatMap(constraint -> constraint.variables().stream())
        .collect(toSet());
  }
}
