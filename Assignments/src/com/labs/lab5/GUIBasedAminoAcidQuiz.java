/*
 * Name:Deepti Nair
 * email:dnair1@uncc.edu
 */
package com.labs.lab5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;


public class GUIBasedAminoAcidQuiz extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	JTextField timeInSec = new JTextField("30");
	private JFrame frame = new JFrame("Quiz Window");
	private JTextArea textArea = new JTextArea(15, 30);
	private JLabel sName = new JLabel("Short Code:");
	private JTextField shrtNmeInput = new JTextField(5);
	private JButton enterButton = new JButton("Enter");
	private JButton quitButton = new JButton("Cancel");
	private JLabel  timerLabel =  new JLabel("Time left 30 secs");
	public static int scores = 0;
	private int count = Integer.parseInt(timeInSec.getText());
	Timer timer;
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
	
	//To create initial screen for the quiz window
	public GUIBasedAminoAcidQuiz() {
		super("Amino Acid Quiz");
		JPanel panel = new JPanel();
		JPanel upPanel = new JPanel(new BorderLayout());
		JLabel welcLabel = new JLabel("Welcome to Amino Acid Short Name Quiz!!", SwingConstants.CENTER);
		JLabel timeLabel = new JLabel("Click Start Or Enter your time");
		JButton start = new JButton("Start Game");
		setSize(350, 350);
		setLocationRelativeTo(null);
		upPanel.add(welcLabel);
		panel.add(timeLabel);
		panel.add(timeInSec);
		panel.add(start);
		getContentPane().add(BorderLayout.CENTER, upPanel);
		getContentPane().add(BorderLayout.SOUTH, panel);
		start.addActionListener(new GUIBasedAminoAcidQuizListner(Integer.parseInt(timeInSec.getText())));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		Event e = new Event();
		start.addActionListener(e);

	}
	
	 private class Event implements ActionListener {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	    		count = Integer.parseInt(timeInSec.getText());
	            timerLabel.setText("Time Remaining " + count + " seconds");
	            TimeClass tc = new TimeClass(count);
	            timer = new Timer(1000, tc);
	            timer.start();
	        }
	    }
	//TimerClass for recording the quiz time
	 private class TimeClass implements ActionListener {
	        int timeLeft;
	        
	        public TimeClass(int count) {
	            this.timeLeft = count;
	        }
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            timeLeft--;
	            count = timeLeft;
	            if (timeLeft <= 5) {
	                Toolkit.getDefaultToolkit().beep();
	            } 
	            if (timeLeft >= 1) {
	                timerLabel.setText("Time Remaining " + timeLeft + " seconds  Score:"+scores);
	                
	            } else {
	                timer.stop();
	                timerLabel.setText("Your time is over!");
	                textArea.setText("Congratulations!!\nYour Final Score: "+scores);
	            }

	        }
	    }

	private class GUIBasedAminoAcidQuizListner implements ActionListener {
		int count=0;
		int fIndex;
		Random r = new Random();

		public GUIBasedAminoAcidQuizListner(int count) {
			this.count = count;
		}
		
		private void createAFrame() {
			//dispose the welcome screen
			dispose();

			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setOpaque(true);
			setLocationRelativeTo(null);
			
			textArea.setWrapStyleWord(true);
			textArea.setEditable(false);
			textArea.setText("Enter one character code for the given amino acid:");
			textArea.setFont(Font.getFont(Font.SANS_SERIF));
			
			JScrollPane scroller = new JScrollPane(textArea);
			scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			
			JPanel inputPanel = new JPanel();
			inputPanel.setLayout(new FlowLayout());
			panel.add(scroller);
			inputPanel.add(sName);
			inputPanel.add(shrtNmeInput);
			inputPanel.add(enterButton);
			inputPanel.add(quitButton);
			
			JPanel timerPanel = new JPanel();
			timerPanel.setLayout(new FlowLayout());
			timerPanel.add(timerLabel);
			
			panel.add(timerPanel);
			panel.add(inputPanel);
			
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.getContentPane().setBackground(Color.RED);
			frame.getContentPane().add(BorderLayout.CENTER, panel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setResizable(false);
			//close the screen if cancel is clicked
			quitButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent evt) {
			        System.exit(0);
			    }
			});
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			createAFrame();
			populateFullNameAA();
			checkTheShortNamesForAA();
		}
		//display full name of amino acids
		private void populateFullNameAA() {
			fIndex = r.nextInt(FULL_NAMES.length);
			textArea.setText("Enter Short Name for:\n " + FULL_NAMES[fIndex]);
			
		}
		//method to check if the user entered amino acid are correct or not
		private void checkTheShortNamesForAA() {
			enterButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						//getting current time from the label displayed on widow
						count = Integer.parseInt((timerLabel.getText()).replaceAll("[^0-9]", ""));
						if (count > 0) {
							if (shrtNmeInput.getText().toUpperCase().equals(SHORT_NAMES[fIndex])) {
								scores++;
								shrtNmeInput.setText("");
								strAA += SHORT_NAMES[fIndex];
								populateFullNameAA();
							} else {
								//If the answer is incorrect
								timer.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										textArea.setText("Incorrect \nThe correct answer for " + FULL_NAMES[fIndex]
												+ " is " + SHORT_NAMES[fIndex]);
										timer.stop();
										textArea.append("\nYour total score is " + scores + "\n\n");
										displayCorrectAnswers(strAA);
										return;
									}
								});
							}
						}

					} catch (NumberFormatException e1) {
						System.out.println("Time Out!!!");
					}
				}
			});
		}
		private void displayCorrectAnswers(String strAA) {
			// Use the strAA to calculate the number of correct amino answered by the user
			// and display it
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
					textArea.append(strAA.charAt(i) + "  was correct  "
							+ count[strAA.charAt(i)] + "  time/s\n");
				}
			}
		}
	}
	public static void main(String[] args) {

		new GUIBasedAminoAcidQuiz();
		
	}
}
