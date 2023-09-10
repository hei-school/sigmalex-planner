package school.hei.sigmalex.linearP.constraint;

import lombok.Value;
import school.hei.sigmalex.linearE.instantiableE.Bounder;
import school.hei.sigmalex.linearE.instantiableE.SubstitutionContext;
import school.hei.sigmalex.linearE.instantiableE.Variable;
import school.hei.sigmalex.linearP.constraint.polytope.DisjunctivePolytopes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Value
public class ForallConstraint extends Constraint {

  Constraint constraint;
  Set<SubstitutionContext> substitutionContexts;

  private static DisjunctivePolytopes normalize(Constraint constraint, Set<SubstitutionContext> substitutionContexts) {
    var and = new ArrayList<Constraint>();
    for (var substitutionContext : substitutionContexts) {
      and.add(normalizedSubstitution(constraint, substitutionContext));
    }
    return new And(and).normalize();
  }

  private static Constraint normalizedSubstitution(
      Constraint constraint, SubstitutionContext substitutionContext) {
    var normalized = constraint.normalize(substitutionContext);
    for (Bounder bounder : substitutionContext.substitutions().keySet()) {
      normalized = normalized.substitute(bounder, substitutionContext.get(bounder), substitutionContext);
    }
    return normalized.toDnf();
  }

  @Override
  public DisjunctivePolytopes normalize(SubstitutionContext substitutionContext) {
    var enrichedSubstitutionContexts = new HashSet<>(substitutionContexts);
    if (!substitutionContext.isEmpty()) {
      enrichedSubstitutionContexts.add(substitutionContext);
    }
    return normalize(constraint, enrichedSubstitutionContexts);
  }

  @Override
  public Set<Variable> variables() {
    return constraint.variables();
  }
}
