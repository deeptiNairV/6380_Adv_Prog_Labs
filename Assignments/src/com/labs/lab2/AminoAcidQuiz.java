/*
 * Name:Deepti Nair
 * email:dnair1@uncc.edu
 */
package com.labs.lab2;

import java.util.Random;
import java.util.Scanner;

public class AminoAcidQuiz {
	
	public static int scores = 0;
	public static long quizStart = 0;
	
	public static int timeInSec = 30;
	public static long quizend = 0;
	public static String flag = "None";
	public static String strAA = "";
	
	public static String[] SHORT_NAMES = 
		{ "A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V" };
	
	public static String[] FULL_NAMES = 
		{
		"alanine","arginine", "asparagine", 
		"aspartic acid", "cysteine",
		"glutamine",  "glutamic acid",
		"glycine" ,"histidine","isoleucine",
		"leucine",  "lysine", "methionine", 
		"phenylalanine", "proline", 
		"serine","threonine","tryptophan", 
		"tyrosine", "valine"};
	
	
	public static void main(String args[]) {
		System.out.println("**************************************");
		System.out.println("Welcome to Amino Acid Short Names Quiz");
		System.out.println("**************************************");
		System.out.println("For 30secs press enter\nElse enter time in sec");

		Scanner user_ans = new Scanner(System.in);
		String userTimeInSec = user_ans.nextLine().toUpperCase();
		quizStart = System.currentTimeMillis();
		//If the user enters number of sec to play use it else set the default 30 sec as game time
		try 
		{ 
			timeInSec =  Integer.parseInt(userTimeInSec);
			quizend = quizStart + timeInSec * 1000; 
		}  
		catch (NumberFormatException e)  
		{ 
			System.out.println("30secs it is!!!.."); 
		} 
		
		
		Random r = new Random();
		quizend = quizStart + timeInSec * 1000;
		while (System.currentTimeMillis() < quizend) {
			//Check if the array length of short and full name of amino acids are same
			if (SHORT_NAMES.length != FULL_NAMES.length) {
				System.out.println("Length of Full Name does not match to that of the Short names");
				break;
			} else {
				System.out.println("Enter One character code for the given amino acid");
				System.out.println("You may quit the game anytime by typing \"quit\"\n");
				System.out.println("You will be given "+timeInSec+" secs to play\nLets begin....your time starts now!!!\n");
				Boolean isCorrect = checkTheShortNamesForAA(user_ans,r);
				
				//If user has entered an incorrect answer a report is displayed whihc gives information on all correct aa
				if (!(null == isCorrect) && isCorrect == false) {
					System.out.println("End of Quiz Below are the correct answers(if you answered any)");
					//Use the strAA to calculate the number of correct amino answered by the user and display it 
					int len = strAA.length();
					int count[] = new int[100];

					for (int i = 0; i < len; i++) {
						count[strAA.charAt(i)]++;
					}
					char aaArray[] = new char[len];
					for (int i = 0; i < len; i++) {
						aaArray[i] = strAA.charAt(i);
						int counter = 0;
						for (int j = 0; j <= i; j++) {
							if (strAA.charAt(i) == aaArray[j])
								counter++;
						}
						if (counter == 1) {
							System.out.println(
									"Amio acid Short Name "+strAA.charAt(i) + " was correct " + count[strAA.charAt(i)] +" time/s");
						}
					}
					break;
				//when all the answers are correct show the score to user
				} else if (!(null == isCorrect)) {
					System.out.println("You answered " +scores +" short name/s correctly");
				}else {
					System.out.println("The Quiz ends here");
					break;
				}

			}

		}
	}
	
	private static Boolean checkTheShortNamesForAA(Scanner user_ans, Random r) {
		// TODO Auto-generated method stub
		Boolean ans = null;
		long currTime = System.currentTimeMillis();
		// Loop through the while loop until the time ends
		while (currTime < quizend) {
			// get the index of random aa
			int fIndex = r.nextInt(FULL_NAMES.length);
			currTime = System.currentTimeMillis();
			if (currTime < quizend) {
				System.out.println(FULL_NAMES[fIndex]);
				System.out.println("Short name:");
				String user_aa = user_ans.nextLine().toUpperCase();
				// if user enters quit to end the game
				if (user_aa.equalsIgnoreCase("quit")) {
					System.out.println("You have choosen to quit");
					ans = null;
					return ans;
				}
				//if user enters some value
				if (!user_aa.isEmpty()) {
					//when correct answer entered
					if (user_aa.equals(SHORT_NAMES[fIndex])) {
						scores++;
						System.out.println("Right..  Score = " + scores + ", time = " + (currTime - quizStart) / 1000.0f
								+ " out of " + timeInSec + " seconds");
						System.out.println("---------------------------------------------------");
						strAA += SHORT_NAMES[fIndex];
						ans = true;
					//when incorrect answer entered
					} else {
						System.out.println("---------------------------------------------------");
						System.out.println("Incorrect..The correct answer is " + SHORT_NAMES[fIndex]);
						ans = false;
						return ans;
					}
				} else {
					System.out.println("Cannot enter blank value\n");
					break;
				}

			} else {
				System.out.println("\nSorry Time Out!!!");
				break;
			}
		}
//		}
		return ans;

	}
}

