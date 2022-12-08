package com.labs.lab6;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ExecutorImpl {
    private ExecutorService primeNumberGeneratorWorkerPool;
    private List<Future<Long>> primeNumberGeneratorFuture;
    private ScheduledExecutorService resultLookupWorker;
    private List<PrimeGenerator> primeNumberGeneratorTask;
    private List<Long> primeNumbers;
    private Long executionTime;

    public void invokeGenerate(long initialValue, int threadCount, JButton generatePrime, JTextArea resultViewer, boolean isCommandLineExecution) {
        // On clicking generate button this action listener is triggerred, which will starts the process of
        // executing prime number generation
        primeNumbers = new ArrayList<>();
        primeNumberGeneratorTask = new ArrayList<>();
        primeNumberGeneratorFuture = new ArrayList<>();

        // In case of threading we will split the user input limit value based on number of thread selected
        // by user to distribute a specific range across different thread that  will find prime number within
        // the range and report it back to main method on completion
        long startPosition = 0;

        long differentiatorValue = initialValue / threadCount;
        long endPosition = startPosition + differentiatorValue;

        // Using executor framework to create a fixed thread pool based on users selected thread count value in
        // combo box control, which will used in creating same number of callable primer generator instance and
        // will be used with different executor thread to run prime generation program in parallel or concurrent manner
        primeNumberGeneratorWorkerPool = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            PrimeGenerator primeGenerator = new PrimeGenerator(startPosition, endPosition, primeNumbers);
            primeNumberGeneratorTask.add(primeGenerator);
            if (i != threadCount - 1) {
                startPosition = endPosition + 1;
                endPosition = startPosition + differentiatorValue;
                if (endPosition > initialValue) {
                    System.out.println("End Position is Greater than Initial Value");
                    endPosition = initialValue;
                }
            }

            // Submitting the callable prime number instance to executor frame for execution and storing the
            // future reference that would be returned by executor on callable submission into a list for
            // later getting execution time back as a result on future completion.
            primeNumberGeneratorFuture.add(primeNumberGeneratorWorkerPool.submit(primeGenerator));
            System.out.println("threadCount : " + i + " || startPosition : " + startPosition + " || endPosition : " + endPosition);
        }

        // Creating new thread to wait and track prime number generation progress and also will use callback mechanism
        // to track and report intermediate prime generation progress at specific interval to user on result viewer
        // text area output control
        // Also when the program completes execution, the execution time taken to complete prime number generation
        // execution and the list of prime number produced by program for user specified limit value will be
        // displayed to user on the same result viewer text area control
        resultLookupWorker = Executors.newSingleThreadScheduledExecutor();
        resultLookupWorker.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            	System.out.println("here i am 1");
                // Stream once consumed cannot be used again so using supplier to reuse the same stream again
                // in the while loop to check for completion of future and with specified delay report back on prime
                // number processing progress to user
                Supplier<Stream<Future<Long>>> supplierStream = () -> primeNumberGeneratorFuture.stream();
                while (supplierStream.get().anyMatch(i->!i.isDone())) {
                	if (!isCommandLineExecution) {
                		resultViewer.append("Total Prime Number Computed So Far.... " + primeNumbers.size() + "\n");
                	}
                	System.out.println("Total Prime Number Computed So Far.... " + primeNumbers.size());
                }

                System.out.println("here i am 3");
                // Added a check here to see if user clicked cancel button then the result would not be provided
                // here but within the cancel control
                if (primeNumberGeneratorTask.stream().allMatch(primeGenerator -> primeGenerator.isKeepPrimeGeneratorRunning())) {

                    // Shutting down the executor as all processing needed for prime number generation is over.
                    primeNumberGeneratorWorkerPool.shutdown();

                    // Computing/Adding all execution time taken return result provided by the future and will be
                    // shown to the user on result viewer text area control
                    executionTime = primeNumberGeneratorFuture.stream().map(f -> {
                        try {
                            // In case we reach so far before completion of future then this is a blocking function
                            // call and waits for future to complete before getting the execution time taken result
                            // back from each future in the list
                            return f.get();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        } catch (ExecutionException ex) {
                            ex.printStackTrace();
                        }
                        return 0L;
                    }).reduce(0L, Long::sum);

                    // Prime number generation result and execution time taken to be shown to user on result viewer
                    // text area control
                    if (!isCommandLineExecution) {
                        resultViewer.append("Complete Execution Time  : " + executionTime + "ms \n");
                        resultViewer.append("Complete Prime Numbers Computed: " + primeNumbers.toString());
                    }
                    System.out.println("Complete Execution Time  : " + executionTime + "ms");
                    System.out.println("Complete Prime Numbers Computed: " + primeNumbers);
                }

                // Shutting down the executor as callback waiting for result is over.
                resultLookupWorker.shutdown();

                // Enabling and changing button label to Generate as the prime number generation execution is complete
                if (!isCommandLineExecution) {
                    generatePrime.setText("Generate");
                    generatePrime.setEnabled(true);
                    generatePrime.setOpaque(true);
                    generatePrime.setBackground(Color.ORANGE);
                    generatePrime.setForeground(Color.blue);
                }


            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public void invokeCancel(JTextArea resultViewer,  boolean isCommandLineExecution) {
        // On clicking cancel button this action listener is triggerred, which will start the process of cancelling
        // prime number generator execution

        // Creating new thread to wait and track prime number generation execution cancellation and report
        // partial execution time taken and partially completed list of prime number produced by program before
        // cancellation and display it on result viewer text area control for users to view
        ExecutorService execTerminate = Executors.newSingleThreadExecutor();
        execTerminate.submit(new Runnable() {
            @Override
            public void run() {
                // Setting all callable task instance variable keepPrimeGeneratorRunning varaible to false,
                // which will provide indication for the task to stop processing due to cancel button click action
                // from user
                primeNumberGeneratorTask.stream().forEach(primeGenerator -> primeGenerator.setKeepPrimeGeneratorRunning(false));

                // Stream once consumed cannot be used again so using supplier to reuse the same stream again
                // in the while loop to check for prime Generator processing stopped due to cancellation before
                // proceeding further
                Supplier<Stream<PrimeGenerator>> supplierTaskStream = () -> primeNumberGeneratorTask.stream();
                while (supplierTaskStream.get().allMatch(primeGenerator -> !primeGenerator.isPrimeGeneratorStopped())) {
                }

                // Computing/Adding all execution time taken return result provided by the future and will be
                // shown to the user on result viewer text area control
                executionTime = primeNumberGeneratorFuture.stream().map(f -> {
                    try {
                        // In case we reach so far before completion of future then this is a blocking function
                        // call and waits for future to complete before getting the execution time taken result
                        // back from each future in the list
                        return f.get();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (ExecutionException ex) {
                        ex.printStackTrace();
                    }
                    return 0L;
                }).reduce(0L, Long::sum);

                // Partial prime number generated result and partial execution time taken to be shown to user
                // on result viewer text area control due to cancel action
                if (!isCommandLineExecution) {
                    resultViewer.append("Due Cancel Partial Execution Time: " + executionTime + "ms \n");
                    resultViewer.append("Partial Prime Numbers Computed : " + primeNumbers.toString());
                }
                System.out.println("Due Cancel Partial Execution Time: "+ executionTime + "ms");
                System.out.println("Partial Prime Numbers Computed : "+ primeNumbers);

                // Shutting down the executor as cancellation reporting of result is over.
                execTerminate.shutdown();
            }
        });
    }

}
