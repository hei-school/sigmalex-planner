package school.hei.linearE.instantiableE;

public sealed interface InstantiableE permits AddIE, Constant, InstantiableV, MultIE {
  InstantiableE simplify();

  default double instantiate() {
    throw new RuntimeException("TODO");
  }
}
