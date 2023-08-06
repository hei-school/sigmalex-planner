package school.hei.linearE;

import school.hei.linearE.instantiableE.InstantiableE;
import school.hei.linearE.instantiableE.MultIE;
import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearE.instantiableE.Variable;

import java.util.HashMap;
import java.util.Set;

import static school.hei.linearE.instantiableE.IEFactory.multie;

public record Mult(InstantiableE e, LinearE le) implements LinearE {

  @Override
  public NormalizedLE normalize(SubstitutionContext substitutionContext) {
    var normalizedLeToMult = le.normalize(substitutionContext);
    var weightedV = new HashMap<Variable, InstantiableE>();
    normalizedLeToMult.weightedV().forEach((v, cToMult) -> weightedV.put(v, new MultIE(e, (cToMult))));
    return new NormalizedLE(weightedV, multie(e, normalizedLeToMult.e())).substituteAll(substitutionContext);
  }

  @Override
  public Set<Variable> variables() {
    return le.variables();
  }
}
