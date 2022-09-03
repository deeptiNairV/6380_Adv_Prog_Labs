/*
 * Name:Deepti Nair
 * email:dnair1@uncc.edu
 */
package com.labs.lab1;
import java.util.Random;


public class RandomNumberAndFreq {
	public static int counter1 = 0;
	public static int counter2 = 0;

	
	public static void main(String[] args) {
		int len = 3;
//		base is string of 4 nucleic acid base each with 25% frequency
		String base = "ATGC";
		
		//Used String builder since they can be easily mutable(also improves performance)
		StringBuilder sb1 = null;
		StringBuilder sb2 = null;
		
		Random r = new Random();

		//Iterate over 1000 times to generate 1000 DNA 3'mer
		for (int j = 0; j < 1000; j++) {
			sb1 = new StringBuilder(len);
			sb2 = new StringBuilder();
			
			for (int i = 0; i < len; i++) {
				
				//sb1 will generate 3 mer from nt 
				sb1.append(base.charAt(r.nextInt(base.length())));
				
				float rValue = r.nextFloat();
				//sb2 will generate DNA 3'mer (frequency of 0.12,0.38,0.39,0.11 for ACGT respectively)
				if(rValue < 0.12){
					sb2.append("A");
					
				}else if(rValue >= 0.12 && rValue < 0.50) {
					sb2.append("C");
									
				}else if(rValue >= 0.50 && rValue < 0.89) {
					sb2.append("G");
					
				}else {
					sb2.append("T");
					
				}
				
			}
//			Bellow are all the randomly generated DNA 3'mers each with 25% base frequency for A,C,G and T
			System.out.println(sb1.toString() + " >> " + j);
			
//			counter1 to count 'AAA' DNA 3'mer (25% base frequency for A,C,G,T)
			if(sb1.toString().equals("AAA")) {
				counter1++;
				
			}
//			counter2 to count 'AAA' DNA 3'mer (0.12,0.38,0.39,0.11 respectively for A,C,G,T )
			if(sb2.toString().equals("AAA")) {
				counter2++;
				
			}
		
		}
		System.out.println("There are "+counter1+" 'AAA' in the Random sequence each with 25% freq for A,C,G,T");
		System.out.println("There are "+counter2+" 'AAA' in the Random sequence with frequency of 0.12,0.38,0.39,0.11 respectively for A,C,G,T ");


	}
}
	