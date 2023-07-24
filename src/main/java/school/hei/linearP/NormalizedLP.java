package school.hei.linearP;

import school.hei.linearE.NormalizedLE;
import school.hei.linearP.constraint.NormalizedConstraint;

import java.util.Set;

public record NormalizedLP(
    String name,
    OptimizationType optimizationType,
    NormalizedLE objective,
    Set<NormalizedConstraint> constraints) {
}
