package school.hei.linearE;

import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.MultIE;
import school.hei.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.Set;

public record Mult(double c, LinearE le) implements LinearE {

  public Mult(double c, Variable v) {
    this(c, new Mono(v));
  }

  @Override
  public NormalizedLE normalize() {
    var normalizedLeToMult = le.normalize();
    var weightedV = new HashMap<Variable, InstantiableE>();
    var constantC = new Constant(c);
    normalizedLeToMult.weightedV().forEach((v, cToMult) -> weightedV.put(v, new MultIE(constantC, (cToMult))));
    return new NormalizedLE(weightedV, new MultIE(constantC, (normalizedLeToMult.e())));
  }

  @Override
  public Set<Variable> variables() {
    return le.variables();
  }
}
