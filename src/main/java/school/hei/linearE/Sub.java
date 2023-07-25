package school.hei.linearE;

public record Sub(LinearE le1, LinearE le2) implements LinearE {

  public Sub(double le1, LinearE le2) {
    this(new Mono(le1), le2);
  }

  @Override
  public NormalizedLE normalize() {
    return new Add(le1, new Mult(-1, le2)).normalize();
  }
}
