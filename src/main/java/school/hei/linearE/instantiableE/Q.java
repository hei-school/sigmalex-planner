package school.hei.linearE.instantiableE;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class Q<Costly> extends NonInstantiableV<Costly> implements Bounder<Costly> {
  public Q(String name, Set<Bounder<Costly>> bounders) {
    super(name, bounders);
  }

  public Q(String name, Bounder<Costly>... bounders) {
    super(name, bounders);
  }

  private Q(String name, Map<Bounder<Costly>, BounderValue<Costly>> bounderSubstitutions) {
    super(name, bounderSubstitutions);
  }

  public Q(String name) {
    this(name, Set.of());
  }

  @Override
  public Variable<Costly> toNew(Map<Bounder<Costly>, BounderValue<Costly>> bounderSubstitutions) {
    return new Q(name, bounderSubstitutions);
  }

  @Override
  public String toString() {
    return "Q{" +
        "name='" + boundedName() + '\'' +
        '}';
  }

  @Override
  public Variable<Costly> variable() {
    return this;
  }

  @Override
  public Function<Costly, InstantiableE<Costly>> instantiator() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bounder<Costly> wi(Function<Costly, InstantiableE<Costly>> instantiator) {
    throw new UnsupportedOperationException();
  }
}
