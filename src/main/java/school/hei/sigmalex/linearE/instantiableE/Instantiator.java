package school.hei.sigmalex.linearE.instantiableE;

import java.util.function.BiFunction;

public interface Instantiator<Costly> extends BiFunction<
    Costly,
    SubstitutionContext,
    InstantiableE<Costly>> {
}
