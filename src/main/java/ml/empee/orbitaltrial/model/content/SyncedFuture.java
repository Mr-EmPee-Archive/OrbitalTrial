package ml.empee.orbitaltrial.model.content;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@RequiredArgsConstructor(staticName = "of")
public class SyncedFuture<T> implements Future<T> {

  private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(SyncedFuture.class);
  private final CompletableFuture<T> future;

  /**
   * Runs this runnable on the main server thread
   */
  public void thenRun(Runnable runnable) {
    if (future.isDone()) {
      runnable.run();
    } else {
      future.thenRun(() -> Bukkit.getScheduler().runTask(plugin, runnable));
    }
  }

  /**
   * Runs this consumer on the main server thread
   */
  @SneakyThrows
  public void thenConsume(Consumer<T> consumer) {
    if (future.isDone()) {
      consumer.accept(future.get());
    } else {
      future.thenAccept(e -> Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(e)));
    }
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    return future.cancel(mayInterruptIfRunning);
  }

  @Override
  public boolean isCancelled() {
    return future.isCancelled();
  }

  @Override
  public boolean isDone() {
    return future.isDone();
  }

  @Override
  public T get() throws ExecutionException, InterruptedException {
    return future.get();
  }

  @Override
  public T get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return future.get(timeout, unit);
  }

}
