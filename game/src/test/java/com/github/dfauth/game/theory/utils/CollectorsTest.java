package com.github.dfauth.game.theory.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import static com.github.dfauth.game.theory.TestUtils.waitOn;
import static com.github.dfauth.game.theory.utils.CompletableFutures.future;
import static io.github.dfauth.trycatch.ExceptionalRunnable.tryCatch;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CollectorsTest {

    private Executor executor = ForkJoinPool.commonPool();

    @Test
    public void testIt() {
        int futureCount = 1000;
        List<CompletableFuture<Integer>> l = new ArrayList<>();
        for(int i=0;i<futureCount;i++) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
            l.add(f);
            int finalI = i;
            executor.execute(() -> {
                long sleepTime = (long) (Math.random() * 10);
                f.complete(tryCatch(() -> {
                    sleep(sleepTime);
                    return finalI;
                }));
            });
        }
        CompletableFuture<List<Integer>> f1 = l.stream().collect(future());
        List<Integer> ll = waitOn(f1, 10000);
        assertEquals(futureCount,ll.size());
        assertEquals((futureCount/2)*(futureCount-1), ll.stream().mapToInt(Integer::intValue).sum());
    }


    @Test
    public void testOne() {
        int futureCount = 1;
        List<CompletableFuture<Integer>> l = new ArrayList<>();
        for(int i=0;i<futureCount;i++) {
            CompletableFuture<Integer> f = new CompletableFuture<>();
            l.add(f);
            int finalI = i;
            executor.execute(() -> {
                long sleepTime = (long) (Math.random() * 10);
                f.complete(tryCatch(() -> {
                    sleep(sleepTime);
                    return finalI;
                }));
            });
        }
        CompletableFuture<List<Integer>> f1 = l.stream().collect(future());
        List<Integer> ll = waitOn(f1, 10000);
        assertEquals(futureCount,ll.size());
        assertEquals(0, ll.stream().mapToInt(Integer::intValue).sum());
    }

}
