package school.hei.linearP.constraint;

import school.hei.linearE.NormalizedLE;
import school.hei.linearE.instantiableE.Variable;

import java.util.Objects;
import java.util.Set;

public final class NormalizedConstraint extends Constraint {

  private final NormalizedLE le; // which is <= 0

  public NormalizedConstraint(String name, NormalizedLE le) {
    super(name);
    this.le = le;
  }

  public NormalizedConstraint(NormalizedLE le) {
    super(null);
    this.le = le;
  }

  @Override
  public Set<Set<NormalizedConstraint>> normalize() {
    return Set.of(Set.of(this));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NormalizedConstraint that = (NormalizedConstraint) o;
    return le.equals(that.le);
  }

  @Override
  public int hashCode() {
    return Objects.hash(le);
  }

  @Override
  public String toString() {
    return "NormalizedConstraint{" +
        "le=" + le +
        ", name='" + name + '\'' +
        '}';
  }

  public NormalizedLE le() {
    return le;
  }

  @Override
  public Set<Variable> variables() {
    return le.variables();
  }

  public double weight(Variable v) {
    return le.weightedV().get(v).instantiate();
  }
}
