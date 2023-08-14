package school.hei.sigmalex.linearE.instantiableE;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record SubstitutionContext(Map<Bounder<?>, BounderValue<?>> substitutions) {

  public static SubstitutionContext of() {
    return new SubstitutionContext(Map.of());
  }

  public SubstitutionContext add(SubstitutionContext that) {
    Map<Bounder<?>, BounderValue<?>> thisAndThatMap = new HashMap<>();
    thisAndThatMap.putAll(this.substitutions);
    thisAndThatMap.putAll(that.substitutions);
    return new SubstitutionContext(thisAndThatMap);
  }

  public void put(Bounder<?> bounder, BounderValue<?> bounderValue) {
    substitutions.put(bounder, bounderValue);
  }

  public Set<Bounder<?>> keySet() {
    return substitutions.keySet();
  }

  public BounderValue<?> get(Bounder<?> bounder) {
    return substitutions.get(bounder);
  }

  public boolean containsKey(Bounder<?> bounder) {
    return substitutions.containsKey(bounder);
  }
}
