/**
 * 
 */
package com.pantanal.data.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author gudong
 *
 */
public class ThreadUtil {
  private static ExecutorService executorService = Executors.newCachedThreadPool();

  /**
   * 
   * @param runnable
   */
  public static void executeWithThreadPool(Runnable runnable) {
    executorService.execute(runnable);
  }
}
