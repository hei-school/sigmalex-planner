package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.NoDuplicateBounderException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

public abstract sealed class Variable<Costly> permits InstantiableV, NonInstantiableV {

  protected final String name;
  protected final SubstitutionContext<Costly> substitutionContext;

  public Variable(String name, SubstitutionContext<Costly> substitutionContext) {
    this.name = name;
    this.substitutionContext = substitutionContext;
  }

  public Variable(String name, Set<Bounder<? extends Costly>> bounders) {
    this.name = name;
    this.substitutionContext = new SubstitutionContext<>(new HashMap<>());
    bounders.forEach(bounder -> this.substitutionContext.put(bounder, null));
  }

  public Variable(String name, Bounder<? extends Costly>... bounders) {
    this(name, noDuplicate(bounders));
  }

  private static <Costly> Set<Bounder<? extends Costly>> noDuplicate(Bounder<? extends Costly>... bounders) {
    for (int i = 0; i < bounders.length; i++) {
      for (int j = i + 1; j < bounders.length; j++) {
        if (bounders[i].variable().name.equals(bounders[j].variable().name)) {
          throw new NoDuplicateBounderException(bounders[i]);
        }
      }
    }
    return Arrays.stream(bounders).collect(toSet());
  }

  public String name() {
    return name;
  }

  public String boundedName() {
    List<Bounder<? extends Costly>> sortedBounders = substitutionContext.keySet().stream()
        .sorted(comparing(bounder -> bounder.variable().boundedName()))
        .toList();
    return name + sortedBounders.stream()
        .map(bounder -> substitutionContext.get(bounder) == null
            ? ""
            : "[" + bounder.variable().boundedName() + ":" + substitutionContext.get(bounder) + "]")
        .collect(joining());
  }

  public Variable<Costly> substitute(Bounder<Costly> bounder, BounderValue<Costly> bounderValue) {
    if (!substitutionContext.containsKey(bounder)) {
      return this;
    }

    SubstitutionContext<Costly> newBounderSubstitutions =
        new SubstitutionContext<>(new HashMap<>(substitutionContext.substitutions()));
    newBounderSubstitutions.put(bounder, bounderValue);
    return toNew(newBounderSubstitutions);
  }

  public abstract Variable<Costly> toNew(SubstitutionContext<Costly> substitutionContext);

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Variable<Costly> variable = (Variable<Costly>) o;
    return Objects.equals(boundedName(), variable.boundedName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(boundedName());
  }
}
