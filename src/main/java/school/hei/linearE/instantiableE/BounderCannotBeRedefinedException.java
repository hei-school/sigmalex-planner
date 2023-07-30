package school.hei.linearE.instantiableE;

public class BounderCannotBeRedefinedException extends RuntimeException {
  public BounderCannotBeRedefinedException(Bounder bounder) {
    super(bounder.variable().name);
  }
}
