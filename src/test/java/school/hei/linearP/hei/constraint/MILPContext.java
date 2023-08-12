package school.hei.linearP.hei.constraint;

import school.hei.linearE.LinearE;
import school.hei.linearP.constraint.Constraint;

public record MILPContext(LinearE objective, Constraint constraint) {
}