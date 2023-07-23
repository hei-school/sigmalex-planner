package school.hei.linearE.instantiableE;

public sealed interface InstantiableE permits Constant, InstantiableV, AddE, MultE {
  InstantiableE simplify();
}
