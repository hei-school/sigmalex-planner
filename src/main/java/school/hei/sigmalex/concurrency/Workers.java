package school.hei.sigmalex.concurrency;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newWorkStealingPool;

@Slf4j
public class Workers {

  private static final ExecutorService THREAD_POOL = newThreadPool();
  private final static int LOG_EVERY_MULTIPLE_OF = 250;
  private static int totalSubmittedTasks = 0;

  private static ExecutorService newThreadPool() {
    return newWorkStealingPool(100);
  }

  @SneakyThrows
  public static <T> Future<T> submit(Callable<T> callable) {
    totalSubmittedTasks++;
    if (totalSubmittedTasks % LOG_EVERY_MULTIPLE_OF == 0) {
      logTotalSubmittedTasks();
    }

    return THREAD_POOL.submit(callable);
  }

  private static void logTotalSubmittedTasks() {
    log.info(String.format("Total submitted tasks: %d+", totalSubmittedTasks));
  }
}
