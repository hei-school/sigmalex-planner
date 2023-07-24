package school.hei.linearE;

import school.hei.linearE.instantiableE.Constant;
import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.MultE;
import school.hei.linearE.instantiableE.Variable;

import java.util.HashMap;

public record Mult(double c, LinearE le) implements LinearE {
  @Override
  public NormalizedLE normalize() {
    var normalizedLeToMult = le.normalize();
    var weightedV = new HashMap<Variable, InstantiableE>();
    var constantC = new Constant(c);
    normalizedLeToMult.weightedV().forEach((v, cToMult) -> weightedV.put(v, new MultE(constantC, (cToMult))));
    return new NormalizedLE(weightedV, new MultE(constantC, (normalizedLeToMult.e())));
  }
}
