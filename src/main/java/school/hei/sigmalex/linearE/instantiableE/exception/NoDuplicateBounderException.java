package school.hei.sigmalex.linearE.instantiableE.exception;

import school.hei.sigmalex.linearE.instantiableE.Bounder;

public class NoDuplicateBounderException extends RuntimeException {
  public NoDuplicateBounderException(Bounder bounder) {
    super(bounder.toString());
  }
}
