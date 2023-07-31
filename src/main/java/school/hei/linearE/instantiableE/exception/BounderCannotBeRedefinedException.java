package school.hei.linearE.instantiableE.exception;

import school.hei.linearE.instantiableE.Bounder;

public class BounderCannotBeRedefinedException extends RuntimeException {
  public BounderCannotBeRedefinedException(Bounder bounder) {
    super(bounder.variable().name());
  }
}
