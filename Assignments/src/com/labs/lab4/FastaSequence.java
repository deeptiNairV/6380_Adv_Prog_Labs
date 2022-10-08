/*
 * Name:Deepti Nair
 * email:dnair1@uncc.edu
 */
package com.labs.lab4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FastaSequence {

	private String header = null;
	private String sequence = null;
	private float gcRatio = 0.0f;
	private String seqID = null;
	private int countA = 0;
	private int countC = 0;
	private int countG = 0;
	private int countT = 0;

	public String getSeqID() {
		return seqID;
	}

	public void setSeqID(String seqID) {
		this.seqID = seqID;
	}

	public int getCountA() {
		return countA;
	}

	public void setCountA(int countA) {
		this.countA = countA;
	}

	public int getCountC() {
		return countC;
	}

	public void setCountC(int countC) {
		this.countC = countC;
	}

	public int getCountG() {
		return countG;
	}

	public void setCountG(int countG) {
		this.countG = countG;
	}

	public int getCountT() {
		return countT;
	}

	public void setCountT(int countT) {
		this.countT = countT;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public float getGCRatio() {
		return gcRatio;
	}

	public void setGCRatio(float gcRatio) {
		this.gcRatio = gcRatio;
	}

	public static void main(String[] args) throws Exception {
//		Get the location of the file
		List<FastaSequence> fastaList = FastaSequence
				.readFastaFile("/Users/deepti/git/6380_Adv_Prog_Labs/Assignments/src/com/labs/lab4/FastaFile.txt");

		for (FastaSequence fs : fastaList) {
			System.out.println(fs.getHeader());
			System.out.println(fs.getSequence());
			System.out.println(String.format("%.04f", fs.getGCRatio()));
		}
//     Write the file to out.txt tab separated file
		File myFile = new File("/Users/deepti/git/6380_Adv_Prog_Labs/Assignments/src/com/labs/lab4/out.txt");

		writeTableSummary(fastaList, myFile);
	}

	
//	This method will read the file and populate each each with its sequence to a List
	public static List<FastaSequence> readFastaFile(String filepath) throws Exception {

		String line = null;
		List<FastaSequence> fsList = new ArrayList<FastaSequence>();
		FastaSequence fSeq = null;
		String sequence = null;
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
//		Create an object of FastaSequence with Header and its respective sequence value
		while ((line = reader.readLine()) != null) {
			line = line.trim();
//			If the first character of the sequence contains '>' it is considered as header else concatenate all the
//			remaining line as one sequence and add it to list of FastaSequences
			if (line.charAt(0) == ('>')) {
				fSeq = new FastaSequence();
				fsList.add(fSeq);
				fSeq.setHeader(line.substring(1).trim());
				sequence = "";
			} else {
				sequence += line;
				fSeq.setSequence(sequence);
			}

		}
		reader.close();
//		Calculate GC content under each header
		for (FastaSequence fs : fsList) {

			fs.setGCRatio(calculateGCContent(fs.getSequence()));
		}

		return fsList;
	}
//	This method calculates GC content of every sequence
	private static float calculateGCContent(String seq) {

		int GCCounter = 0;
		int totalCounter = 0;
		float GCPercentage = 0.0f;
		for (char base : seq.toCharArray()) {
			if (base == 'G' || base == 'C') {
				GCCounter += 1;
			}
			totalCounter += 1;
		}
		GCPercentage = (float) GCCounter / totalCounter * 100;
		return GCPercentage;
	}
	
//	For every Fasta sequence calculate count of each aa and also extract the sequence ID from header and write 
//	the output as a tab separated text file
	private static void writeTableSummary(List<FastaSequence> fastaList, File myFile) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(myFile));
		final String TAB = "\t";
		final String NEW_LINE = "\n";
		
//		create header row labels
		List<String> headers = new ArrayList<String>();
		headers.add("SequenceID");
		headers.add("numA");
		headers.add("numC");
		headers.add("numG");
		headers.add("numT");
		headers.add("Sequence");
		
//		write the header lables to txt file
		for (String hd : headers) {
			writer.write(hd + "" + TAB);
		}
		
		for (FastaSequence fs : fastaList) {
//			For each sequence extract the sequence ID
			extractAndSetSequencID(fs);
//			count the number of A,C,G,T in the sequence
			calculateAnsSetACGTCount(fs);
//			write data into file
			writer.write(NEW_LINE + 
					    fs.getSeqID()  + TAB + 
					    fs.getCountA() + TAB + 
					    fs.getCountC() + TAB +
					    fs.getCountG() + TAB +
					    fs.getCountT() + TAB + 
					    fs.getSequence());
		}
		writer.flush();
		writer.close();
	}
	
	
//Implementation to get and set Sequence ID
	private static void extractAndSetSequencID(FastaSequence fs) {
		String sId = fs.getHeader();
		sId = sId.substring(0,sId.indexOf(" "));
		fs.setSeqID(sId);
	}
	
	
//Implemetation to calculate count of each nucleotide
	private static void calculateAnsSetACGTCount(FastaSequence fs) {
		int countA = 0,countC=0,countG=0,countT = 0;
		String seq = fs.getSequence();
		
		for(char c:seq.toCharArray()){
			switch(c) {
			case 'A':
					countA++;
					break;
			case 'C':
					countC++;
					break;
			case 'G':
					countG++;
					break;
			case 'T':
					countT++;
					break;
			}
			
		}
		fs.setCountA(countA);
		fs.setCountC(countC);
		fs.setCountG(countG);
		fs.setCountT(countT);
	}

}
