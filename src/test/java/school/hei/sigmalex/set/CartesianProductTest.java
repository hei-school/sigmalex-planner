package school.hei.sigmalex.set;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.sigmalex.set.CartesianProduct.cartesianProduct;

class CartesianProductTest {

  @Test
  public void cartesian_product_with_empty_sets() {
    assertEquals(Set.of(), cartesianProduct(Set.of()));
    assertEquals(Set.of(), cartesianProduct(Set.of(), Set.of()));
    assertEquals(Set.of(1, 2, 3), cartesianProduct(Set.of(1, 2, 3)));
    assertEquals(Set.of(1, 2, 3), cartesianProduct(Set.of(1, 2, 3), Set.of()));
    assertEquals(Set.of(1, 2, 3), cartesianProduct(Set.of(), Set.of(1, 2, 3)));
  }

  @Test
  public void cartesian_product_with_no_empty_set() {
    var x = cartesianProduct(Set.of(1, 2, 3), Set.of("a", "b"));
    assertEquals(
        Set.of(
            Set.of(1, "a"), Set.of(1, "b"),
            Set.of(2, "a"), Set.of(2, "b"),
            Set.of(3, "a"), Set.of(3, "b")),
        cartesianProduct(Set.of(1, 2, 3), Set.of("a", "b")));
    assertEquals(
        Set.of(
            Set.of(1, "a", "+"), Set.of(1, "a", "*"),
            Set.of(1, "b", "+"), Set.of(1, "b", "*"),
            Set.of(2, "a", "+"), Set.of(2, "a", "*"),
            Set.of(2, "b", "+"), Set.of(2, "b", "*"),
            Set.of(3, "a", "+"), Set.of(3, "a", "*"),
            Set.of(3, "b", "+"), Set.of(3, "b", "*")),
        cartesianProduct(Set.of(1, 2, 3), Set.of("a", "b"), Set.of("+", "*")));
  }
}