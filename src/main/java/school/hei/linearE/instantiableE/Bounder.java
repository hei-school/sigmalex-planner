package school.hei.linearE.instantiableE;

import java.util.function.Function;

public interface Bounder<Costly> {
  Variable<Costly> variable();

  Function<Costly, InstantiableE<Costly>> instantiator();

  Bounder<Costly> wi(Function<Costly, InstantiableE<Costly>> instantiator); //wi: with instantiator
}
