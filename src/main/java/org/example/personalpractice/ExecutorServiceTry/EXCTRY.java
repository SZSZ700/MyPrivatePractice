package org.example.personalpractice.ExecutorServiceTry;

import java.util.concurrent.Executors;

public class EXCTRY {
    public static void main(String [] args){
        var cores = Runtime.getRuntime().availableProcessors();

        var executor = Executors.newFixedThreadPool(cores);

        for (var i = 0; i < cores; i++) {

            executor.submit(() -> {

                long sum = 0;

                for (var j = 0; j < 5_000_000_000L; j++) {
                    sum += j;
                }

                System.out.println(
                        Thread.currentThread().getName() + "finished: " + sum
                );
            });

        }

        executor.shutdown();
    }
}
