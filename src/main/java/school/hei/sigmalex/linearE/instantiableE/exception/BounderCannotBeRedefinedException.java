package school.hei.sigmalex.linearE.instantiableE.exception;

import school.hei.sigmalex.linearE.instantiableE.Bounder;

public class BounderCannotBeRedefinedException extends RuntimeException {
  public BounderCannotBeRedefinedException(Bounder bounder) {
    super(bounder.variable().name());
  }
}
