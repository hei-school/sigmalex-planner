package school.hei.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class CartesianProduct implements BiFunction<Set<?>, Set<?>, Set<?>> {

  public static Set<?> cartesianProduct(Set<?>... sets) {
    var cpList = cartesianProduct(Arrays.stream(sets)
        .map(set -> set.stream().toList())
        .toArray(List[]::new));
    return cpList.stream()
        .map(e -> e instanceof List ? new HashSet<List<Object>>((List) e) : e)
        .collect(Collectors.toSet());
  }

  private static List<?> cartesianProduct(List<?> l1, List<?> l2) {
    if (l1.isEmpty()) {
      return l2;
    }
    if (l2.isEmpty()) {
      return l1;
    }

    List<Object> res = new ArrayList<>();
    for (var e1 : l1) {
      for (var e2 : l2) {
        res.add(List.of(e1, e2));
      }
    }
    return res;
  }

  private static List<?> cartesianProduct(List<?>... sets) {
    return Arrays.stream(sets)
        .reduce(List.of(), (s1, s2) -> cartesianProduct(s1, s2).stream()
            .map(CartesianProduct::flattenCp)
            .collect(toList()));
  }

  private static Object flattenCp(Object cp) {
    if (!(cp instanceof List)) {
      return cp;
    }
    var cpAsList = (List) cp;
    if (!(cpAsList.get(0) instanceof List)) {
      return cp;
    }

    var res = new ArrayList<>();
    res.addAll((List) cpAsList.get(0));
    res.add(cpAsList.get(1));
    return res;
  }

  @Override
  public Set<?> apply(Set<?> s1, Set<?> s2) {
    return new HashSet<>(cartesianProduct(
        s1.stream().toList(),
        s2.stream().toList()));
  }
}
