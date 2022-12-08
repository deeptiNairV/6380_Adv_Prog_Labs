package com.labs.lab6;

import java.util.Scanner;

public class StartProjectPrime {

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        userMessage(false);

        while (true) {
            int userRunModeSelection = 0;

            try {
                userRunModeSelection = Integer.parseInt(userInput.nextLine());
            } catch (Exception ex) {
               // Ignoring for now!!!
            }

            if (userRunModeSelection == 1) {
                new CommandLineImpl().userInteractionStarterModule();
            } else if (userRunModeSelection == 2) {
                new UserForm();
            } else if (userRunModeSelection == 3) {
                System.out.println("Exit Selected - Terminating Program :-(");
                System.exit(0);
            } else {
                System.out.println( "\n Please Select a Valid Option :-<");
                userMessage(true);
            }
        }
    }

    private static void userMessage(boolean retry) {
        System.out.println("Program Run Option (Enter 1 or 2) :");
        System.out.println("1 - Command Line Mode");
        System.out.println("2 - Run GUI Mode");
        System.out.println("3 - Exit");
        if (retry) {
            System.out.print("Retry Selecting An Option : ");
        } else {
            System.out.print("Please Select An Option : ");
        }

    }
}
