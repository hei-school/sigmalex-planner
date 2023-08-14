package school.hei.sigmalex.linearE;

import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;

import java.util.Set;

public sealed interface LinearE permits Add, Mono, Mult, NormalizedLE, Sigma {
  NormalizedLE normalize(SubstitutionContext substitutionContext);

  default NormalizedLE normify() {
    return normalize(SubstitutionContext.of()).simplify();
  }


  Set<Variable> variables();
}
