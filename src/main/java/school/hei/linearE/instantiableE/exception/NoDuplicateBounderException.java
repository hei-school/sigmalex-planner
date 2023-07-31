package school.hei.linearE.instantiableE.exception;

import school.hei.linearE.instantiableE.Bounder;

public class NoDuplicateBounderException extends RuntimeException {
  public NoDuplicateBounderException(Bounder bounder) {
    super(bounder.toString());
  }
}
