package school.hei.linearE.instantiableE;

import java.util.function.BiFunction;

public interface Instantiator<Costly> extends BiFunction<
    Costly,
    SubstitutionContext<Costly>,
    InstantiableE<Costly>> {
}
