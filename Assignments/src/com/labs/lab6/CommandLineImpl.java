package com.labs.lab6;

import java.util.Scanner;

public class CommandLineImpl {
    private ExecutorImpl executorImpl = new ExecutorImpl();

    public void userInteractionStarterModule() {
        long primeNumberLimitInput = 0L;
        int threadCountInput = 1;

        // Getting User input for primer number result to be generated for provided a limit
        Scanner primeNumberLimitUserInput = new Scanner(System.in);
        boolean valid = false;
        while (!valid) {
            System.out.print("Generate Prime Number Until : ");
            try {
                primeNumberLimitInput = Integer.parseInt(primeNumberLimitUserInput.nextLine());
                valid = true;
            } catch (Exception ex) {
                System.out.println( "\nEnter Valid Numeric Digit For Prime Generator");
            }

        }

        // Getting User input for number of thread to be used for prime number generation execution
        Scanner threadCountUserInput = new Scanner(System.in);
        valid = false;
        while (!valid) {
            System.out.print("Number Of Thread To Be Used For Execution [1-5] : ");
            try {
                threadCountInput = Integer.parseInt(threadCountUserInput.nextLine());
                if (threadCountInput >= 1 && threadCountInput <= 5) {
                    valid = true;
                } else {
                    System.out.println( "\nInvalid Thread Count Value [Enter Value Between 1-5]");
                }
            } catch (Exception ex) {
                System.out.println( "\nInvalid Thread Count Value [Enter Value Between 1-5]");
            }
        }

        // Getting User input for starting prime number program
        Scanner userSelectedOptionInput = new Scanner(System.in);
        while (true) {
            this.userMessage(false);

            int userSelectedOption = 0;
            try {
                userSelectedOption = Integer.parseInt(userSelectedOptionInput.nextLine());
            } catch (Exception ex) {
                // Ignoring for now!!!
            }

            if (userSelectedOption == 1) {
                executorImpl.invokeGenerate(primeNumberLimitInput, threadCountInput, null, null, true);
            } else if (userSelectedOption == 2) {
                executorImpl.invokeCancel(null, true);
            } else if (userSelectedOption == 3) {
                System.out.println("Exit Selected - Terminating Program");
                System.exit(0);
            } else {
                System.out.println( "\nPlease Select a Valid Option");
                this.userMessage(true);
            }
        }
    }

    private void userMessage(boolean retry) {
        System.out.println("Selection Prime Number Execution Option : ");
        System.out.println("1 - Generate");
        System.out.println("2 - Cancel");
        System.out.println("3 - Exit");
        if (retry) {
            System.out.print("Retry Selecting Correct Option : ");
        } else {
            System.out.print("Please Select An Option : ");
        }

    }
}
