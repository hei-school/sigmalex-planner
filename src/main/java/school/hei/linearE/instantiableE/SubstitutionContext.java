package school.hei.linearE.instantiableE;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record SubstitutionContext<Costly>(Map<Bounder<? extends Costly>, BounderValue<Costly>> substitutions) {

  public static <Costly> SubstitutionContext<Costly> of() {
    return new SubstitutionContext<>(Map.of());
  }

  public SubstitutionContext add(SubstitutionContext that) {
    Map<Bounder<? extends Costly>, BounderValue<Costly>> thisAndThatMap = new HashMap<>();
    thisAndThatMap.putAll(this.substitutions);
    thisAndThatMap.putAll(that.substitutions);
    return new SubstitutionContext(thisAndThatMap);
  }

  public void put(Bounder<? extends Costly> bounder, BounderValue<Costly> bounderValue) {
    substitutions.put(bounder, bounderValue);
  }

  public Set<Bounder<? extends Costly>> keySet() {
    return substitutions.keySet();
  }

  public BounderValue<Costly> get(Bounder<? extends Costly> bounder) {
    return substitutions.get(bounder);
  }

  public boolean containsKey(Bounder<Costly> bounder) {
    return substitutions.containsKey(bounder);
  }
}
