package school.hei.linearP.solver;

import school.hei.linearE.NormalizedLE;
import school.hei.linearP.NormalizedLP;
import school.hei.linearP.Solution;
import school.hei.linearP.constraint.NormalizedConstraint;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static java.lang.Runtime.getRuntime;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.joining;

/**
 * <a href="http://web.mit.edu/lpsolve/doc/">LP Solver</a>
 */
public class LPSolver implements Solver {
  @Override
  public Solution solve(NormalizedLP lp) {
    var fileName = randomUUID() + ".lp";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
      writer.write(toLPSolverFormat(lp));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    var cmd = "lp_solve " + fileName;
    return new Solution(execCmd(cmd));
  }

  private String toLPSolverFormat(NormalizedLP lp) {
    var optimizeObjective = lp.optimizationType() + ": " + toLPSolverFormat(lp.objective()) + ";\n";
    String withOnlyAdditions = optimizeObjective + lp.constraints().stream().
        map(NormalizedConstraint::le)
        .map(this::toLPSolverFormat)
        .map(le -> le + " <= 0")
        .collect(joining(";\n")) + ";";
    return withOnlyAdditions.replace("+ -", "- ");
  }

  private String toLPSolverFormat(NormalizedLE le) {
    var weightedV = le.weightedV();
    return weightedV.keySet().stream()
        .map(v -> String.format("%s %s", weightedV.get(v).toString(), v.getName()))
        .collect(joining(" + "))
        + " + " + le.e();
  }

  private static String execCmd(String cmd) {
    String result;
    try (InputStream inputStream = getRuntime().exec(cmd).getInputStream();
         Scanner s = new Scanner(inputStream).useDelimiter("\\A")) {
      result = s.hasNext() ? s.next() : null;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return result;
  }
}
