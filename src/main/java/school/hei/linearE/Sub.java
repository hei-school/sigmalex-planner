package school.hei.linearE;

public record Sub(LinearE le1, LinearE le2) implements LinearE {
  @Override
  public NormalizedLE normalize() {
    return new Add(le1, new Mult(-1, le2)).normalize();
  }
}
