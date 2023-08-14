package school.hei.planner.constraint;

import school.hei.sigmalex.linearE.LinearE;
import school.hei.sigmalex.linearP.constraint.Constraint;

public record MILPContext(LinearE objective, Constraint constraint) {
}