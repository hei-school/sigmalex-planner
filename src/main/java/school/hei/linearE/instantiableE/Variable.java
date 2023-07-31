package school.hei.linearE.instantiableE;

import school.hei.linearE.instantiableE.exception.BounderCannotBeRedefinedException;
import school.hei.linearE.instantiableE.exception.NoDuplicateBounderException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

public abstract sealed class Variable permits InstantiableV, NonInstantiableV {

  protected final String name;
  protected final Map<Bounder, BounderValue> bounderSubstitutions;

  public Variable(String name, Map<Bounder, BounderValue> bounderSubstitutions) {
    this.name = name;
    this.bounderSubstitutions = bounderSubstitutions;
  }

  public Variable(String name, Set<Bounder> bounders) {
    this.name = name;
    this.bounderSubstitutions = new HashMap<>();
    bounders.forEach(bounder -> this.bounderSubstitutions.put(bounder, null));
  }

  public Variable(String name, Bounder... bounders) {
    this(name, noDuplicate(bounders));
  }

  private static Set<Bounder> noDuplicate(Bounder[] bounders) {
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
    List<Bounder> sortedBounders = bounderSubstitutions.keySet().stream()
        .sorted(comparing(bounder -> bounder.variable().boundedName()))
        .toList();
    return name + sortedBounders.stream()
        .map(bounder -> bounderSubstitutions.get(bounder) == null
            ? ""
            : "[" + bounder.variable().boundedName() + ":" + bounderSubstitutions.get(bounder) + "]")
        .collect(joining());
  }

  public Variable substitute(Bounder bounder, BounderValue bounderValue) {
    if (!bounderSubstitutions.containsKey(bounder)) {
      return this;
    }
    if (isBounderAlreadySubstituted(bounder)) {
      throw new BounderCannotBeRedefinedException(bounder);
    }

    Map<Bounder, BounderValue> newBounderSubstitutions = new HashMap<>(bounderSubstitutions);
    newBounderSubstitutions.put(bounder, bounderValue);
    return toNew(newBounderSubstitutions);
  }

  public boolean isBounderAlreadySubstituted(Bounder k) {
    return bounderSubstitutions.containsKey(k) && bounderSubstitutions.get(k) != null;
  }

  public abstract Variable toNew(Map<Bounder, BounderValue> bounderSubstitutions);

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Variable variable = (Variable) o;
    return Objects.equals(boundedName(), variable.boundedName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(boundedName());
  }
}
