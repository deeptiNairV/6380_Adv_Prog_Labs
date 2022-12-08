package com.labs.lab6;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrimeGenerator implements Callable<Long> {
    private List<Long> test;
    private long startPosition;
    private long endPosition;
    private AtomicBoolean keepPrimeGeneratorRunning = new AtomicBoolean(true);
    private static AtomicBoolean primeGeneratorStopped = new AtomicBoolean(false);

    public PrimeGenerator(long startPosition, long endPosition, List test) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.test = test;

    }
    @Override
    public Long call() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("startPosition: " + startPosition);
        System.out.println("endPosition: " + endPosition);
        for (long i = startPosition; i < endPosition; i++) {
            if (!this.keepPrimeGeneratorRunning.get()) {
                primeGeneratorStopped.set(true);
                System.out.println("Prime Generator Stopped : " + primeGeneratorStopped.get());
                long end = System.currentTimeMillis();
                return (end - start);
            }

                if (isPrime(i)) {
                    test.add(i);
                }

        }
        long end = System.currentTimeMillis();
        System.out.println("Genration Complete");
        return (end - start);
    }

    private boolean isPrime(long n) {
    	long m = n/2;
        if (n <= 1) {
            return false;
        }
        for (long i = 2; i <= m; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isKeepPrimeGeneratorRunning() {
        return keepPrimeGeneratorRunning.get();
    }

    public void setKeepPrimeGeneratorRunning(boolean changeIt) {
        System.out.println("Called setKeepPrimeGeneratorRunning");
        this.keepPrimeGeneratorRunning.set(changeIt);;
    }

    public boolean isPrimeGeneratorStopped() {
        return primeGeneratorStopped.get();
    }
}
