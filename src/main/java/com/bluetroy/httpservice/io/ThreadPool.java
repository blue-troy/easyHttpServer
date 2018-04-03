package com.bluetroy.httpservice.io;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static ExecutorService service = Executors.newFixedThreadPool(10);

    public static void execute(Runnable task) {
        service.execute(task);
    }
}
