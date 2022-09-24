/*
 * Name:Deepti Nair
 * email:dnair1@uncc.edu
 */
package com.labs.lab3;

import java.util.Random;

public class RandomSequence {

	public static String generateRandomSequence(char[] alphabet, float[] weights, int length) throws Exception
	{
		String randSeq = "";
		Random r = new Random();
		char[] alphaArr = new char[30]; 
		float[] weightArr = new float[30];
		
//		Check if the length of the array is greater than 0 and both arrays have equal length
		if((alphabet.length > 0 && weights.length>0) && (alphabet.length == weights.length)) {
			float totalWeight = 0.0f;
//			Sum all the weights and check if it sums to 1 else throw an exception
			for(int i=0;i<weights.length;i++){
				totalWeight += weights[i];
			}
			System.out.println(totalWeight);
			
			if(totalWeight != 1.0) {
				throw new Exception("Background rate of residues not summing to 1");
			}
//			Create array of random alphabet with its respective weights 
			for(int i=0;i<length;i++) {
				int index = r.nextInt(alphabet.length);
				alphaArr[i] = alphabet[index];
				weightArr[i] = weights[index];
			}
		}else {
			throw new Exception("Length of both arrays do not match OR The Length is 0");
		}
		
//		Create string to display the alphabet and its respective weight
		for(int i=0; i<alphaArr.length;i++) {
			
			randSeq += alphaArr[i] + " "+ weightArr[i]+"\n";
			
		}
		
		return randSeq;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		float[] dnaWeights = { .3f, .3f, .2f, .2f };
		char[] dnaChars = { 'A', 'C', 'G', 'T'  };
		
		// a random DNA 30 mer
		System.out.println("DNA");
		System.out.println(generateRandomSequence(dnaChars, dnaWeights,30));
		// background rate of residues from https://www.science.org/doi/abs/10.1126/science.286.5438.295
		float proteinBackground[] =
			{0.072658f, 0.024692f, 0.050007f, 0.061087f,
		        0.041774f, 0.071589f, 0.023392f, 0.052691f, 0.063923f,
		        0.089093f, 0.023150f, 0.042931f, 0.052228f, 0.039871f,
		        0.052012f, 0.073087f, 0.055606f, 0.063321f, 0.012720f,
		        0.032955f}; 
			

		char[] proteinResidues = 
				new char[] { 'A', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
							 'V', 'W', 'Y' };
		
		// a random protein with 30 residues
		System.out.println("Proteins");
		System.out.println(generateRandomSequence(proteinResidues, proteinBackground, 30));
		
	}
}
