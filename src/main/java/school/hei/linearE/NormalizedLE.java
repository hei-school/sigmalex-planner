package school.hei.linearE;

import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class NormalizedLE implements LinearE {

  private final Map<Variable, InstantiableE> weightedV;
  private final InstantiableE e;

  public NormalizedLE(Map<Variable, InstantiableE> weightedV, InstantiableE e) {
    this.weightedV = new HashMap<>();
    weightedV.forEach((v, eOfV) -> this.weightedV.put(v, eOfV.simplify()));
    this.e = e.simplify();
  }

  public NormalizedLE(double c) {
    this(Map.of(), new Constant(c));
  }

  @Override
  public NormalizedLE normalize() {
    return this;
  }

  public Map<Variable, InstantiableE> weightedV() {
    return weightedV;
  }

  public InstantiableE e() {
    return e;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NormalizedLE that = (NormalizedLE) o;
    return Objects.equals(weightedV, that.weightedV) && Objects.equals(e, that.e);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weightedV, e);
  }

  @Override
  public String toString() {
    return "Normalized{" +
        "weightedV=" + weightedV +
        ", e=" + e +
        '}';
  }
}
