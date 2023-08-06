package school.hei.linearE;

import school.hei.linearE.instantiableE.SubstitutionContext;
import school.hei.linearE.instantiableE.Variable;

import java.util.Set;

public sealed interface LinearE permits Add, Mono, Mult, NormalizedLE, Sigma {
  NormalizedLE normalize(SubstitutionContext<?> substitutionContext);

  default NormalizedLE normify() {
    return normalize(SubstitutionContext.of()).simplify();
  }


  Set<Variable> variables();
}
