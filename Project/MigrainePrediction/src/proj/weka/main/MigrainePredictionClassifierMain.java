package proj.weka.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;



public class MigrainePredictionClassifierMain {

	// declare classifier names as constants
	public static final String randF = "Random Forest";
	public static final String naBay = "Naive Bayes";
	public static final String svm = "SVM";
	public static final String desTree = "Decision Tree";
	public static final String oneR = "OneR";
	public static final String dL = "Deep Learning";

	// dataset file
	public static final String fileName = "/Users/deepti/eclipse-workspace/MigrainePrediction/src/proj/weka/resources/Migraine_data_Org.csv";
	// dataset for Deep Learning
	public static final String DATASETPATH = "/Users/deepti/eclipse-workspace/MigrainePrediction/src/proj/weka/resources/Migraine_data_Org.arff";
	public static final String MODElPATH = "/Users/deepti/eclipse-workspace/MigrainePrediction/src/proj/weka/resources/model.bin";

	// Result File
	private static Instances data;
	private static Classifier cl = null;
	private static SMO cl_svm = null;
	public static boolean isSVM = false;
	private static Evaluation eval = null;
	// For Curve
	private static ThresholdCurve tc = new ThresholdCurve();
	private static int classIndex = 0;
	private static Instances result = null;
	private static String classifierName = null;

	private static JTextArea resultsTxArea;
	private static JFrame frame;
	public static final String title = "Migraine Prediction Using Machine Learning";
	private static List<String> selectedList = new ArrayList<String>();
	private static List<String> classifierOptions;

	public MigrainePredictionClassifierMain() {
		new MigraineGUIFrame();
	}

	//Method to create ROC for the selected classifier
//	Known issue here ,each classifier will create its own frame when multiple checkboxes are selected and the combined
//	result is at the topmost frame.For example if you selecect SVM,OneR and Random Forest 3 frames are created and the 
//	uppermost frame has the actual result while closing you have to close 3 frames.
	public static void createPlotForSelectedClassifiers(Instances result, ThresholdVisualizePanel vmc, Color color,
			String plotName) throws Exception {

		final JFrame jf = new JFrame("WEKA ROC: " + vmc.getName());
		jf.setSize(500, 400);
		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(vmc, BorderLayout.CENTER);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setVisible(true);

		PlotData2D tempd = new PlotData2D(result);
		tempd.setPlotName(plotName);
		tempd.addInstanceNumberAttribute();
		// specify which points are connected
		boolean[] cp = new boolean[result.numInstances()];
		for (int n = 1; n < cp.length; n++)
			cp[n] = true;
		tempd.setConnectPoints(cp);
		tempd.setCustomColour(color);
		// add plot
		vmc.setROCString("(Area under ROC = " + Utils.doubleToString(ThresholdCurve.getROCArea(result), 4) + ")");
		vmc.setName(result.relationName());
		vmc.addPlot(tempd);

	}

	//method to display results to the textarea in the GUI for selected classifer
	private static void displayResultsAndConfusionMatrix(Evaluation eval, String strClassifier) throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append("-----------------------------------\n");
		sb.append("Results for " + strClassifier + "\n");
		sb.append("-----------------------------------\n");
		sb.append("Correct % = " + eval.pctCorrect() + "\n");
		sb.append("Incorrect % = " + eval.pctIncorrect() + "\n");
		sb.append("AUC = " + eval.areaUnderROC(1) + "\n");
		sb.append("kappa = " + eval.kappa() + "\n");
		sb.append("MAE = " + eval.meanAbsoluteError() + "\n");
		sb.append("RMSE = " + eval.rootMeanSquaredError() + "\n");
		sb.append("RAE = " + eval.relativeAbsoluteError() + "\n");
		sb.append("RRSE = " + eval.rootRelativeSquaredError() + "\n");
		sb.append("Precision = " + eval.precision(1) + "\n");
		sb.append("Recall = " + eval.recall(1) + "\n");
		sb.append("fMeasure = " + eval.fMeasure(1) + "\n");
		sb.append("Error Rate = " + eval.errorRate() + "\n");
		sb.append("\n----------------------------------------------------------");
		sb.append(eval.toMatrixString("\nConfusion Matrix for " + strClassifier + "\t\n"));
		sb.append("----------------------------------------------------------\n");
		System.out.println("Completed evaluation for " + strClassifier + "");
		resultsTxArea.setText(sb.toString());

	}
	
	//method to display results to the textarea in the GUI for if Deep Learning radiobutton is selected 
	private static void displayDeepLearningPrediction(DeepLMigrainePredModelGenerator mg, MultilayerPerceptron ann,
			Instances traindataset, Instances testdataset, Filter filter) throws Exception {
		StringBuilder sb = new StringBuilder();

		String evalsummary = mg.evaluateModel(ann, traindataset, testdataset);
		sb.append("-----------------------------------------------------\n");
		sb.append("Results for Deep Learning for Multilayer Perceptron: \n");
		sb.append("-----------------------------------------------------\n");
		sb.append(evalsummary);
		// classifiy a single instance
		DeepLMigrainePredModelClassifier cls = new DeepLMigrainePredModelClassifier();

		// Predict the Type of Migraine
		String classname = cls.classifiy(Filter.useFilter(
				cls.createInstance(20, 1, 4, 2, 2, 3, 1, 1, 1, 1, 1, 2, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1), filter),
				MODElPATH);

		sb.append("-----------------------------------------------------\n");
		sb.append(" The migraine predicted for given testset is  '" + classname + "'\n");
		sb.append("-----------------------------------------------------\n");
		resultsTxArea.setText(sb.toString());

	}

// Write the prediction distribution,prediction weight,actual prediction and the predicted value to a txt file to view the results
	private static void generatePredictions(Evaluation eval, Instances result, ThresholdCurve tc, String classifierName)
			throws IOException {
		// generate curve
		String clsFilePath = "/Users/deepti/eclipse-workspace/MigrainePrediction/src/proj/weka/resources/"
				+ classifierName + ".txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(clsFilePath)));

		writer.write("distribution\tweight\tactual\tpredicted\n");

		for (Prediction p : eval.predictions()) {
			NominalPrediction pred = (NominalPrediction) p;

			writer.write(pred.distribution()[0] + "\t\t" + pred.weight() + "\t" + pred.actual() + "\t"
					+ pred.predicted() + "\n");
		}
		writer.flush();
		writer.close();

	}
	
	//Create GUI

	private class MigraineGUIFrame {

		public MigraineGUIFrame() {

			frame = new JFrame(title);
			frame.setSize(640, 430);
			frame.setLocationRelativeTo(null);

			JPanel mainPanel = new JPanel();
			mainPanel.setBorder(new TitledBorder("Migraine Prediction"));
			mainPanel.setLayout(new GridLayout(2, 2));

			JPanel panel1 = new JPanel();
			panel1.setBorder(new TitledBorder("ChooseClassifier"));
			panel1.setLayout(new GridLayout(1, 2));

			JPanel panel1_left = new JPanel();
			panel1_left.setLayout(new BorderLayout());

			JLabel chseLabel = new JLabel("Select to view Results:");
			panel1.add(chseLabel);
			
			//6 radio buttons for 6 options 
			JPanel choiceRadioPanel = new JPanel();
			ButtonGroup group = new ButtonGroup();
			JRadioButton rb1 = new JRadioButton(randF, true);
			JRadioButton rb2 = new JRadioButton(naBay);
			JRadioButton rb3 = new JRadioButton(svm);
			JRadioButton rb4 = new JRadioButton(desTree);
			JRadioButton rb5 = new JRadioButton(oneR);
			JRadioButton rb6 = new JRadioButton(dL);
			rb1.setActionCommand(randF);
			rb2.setActionCommand(naBay);
			rb3.setActionCommand(svm);
			rb4.setActionCommand(desTree);
			rb5.setActionCommand(oneR);
			rb6.setActionCommand(dL);
			group.add(rb1);
			group.add(rb2);
			group.add(rb3);
			group.add(rb4);
			group.add(rb5);
			group.add(rb6);
			choiceRadioPanel.setLayout(new BoxLayout(choiceRadioPanel, BoxLayout.Y_AXIS));
			choiceRadioPanel.add(rb1);
			choiceRadioPanel.add(rb2);
			choiceRadioPanel.add(rb3);
			choiceRadioPanel.add(rb4);
			choiceRadioPanel.add(rb5);
			choiceRadioPanel.add(rb6);

			// View Results Button
			JPanel panel1_rb = new JPanel();
			panel1_rb.setLayout(new BorderLayout());
			panel1_rb.add(choiceRadioPanel, BorderLayout.CENTER);
			final JButton resButton = new JButton("View Results");
			panel1_rb.add(resButton, BorderLayout.SOUTH);
			resButton.addActionListener(new ViewResultsListener(group));

			panel1_left.add(panel1_rb);
			panel1.add(panel1_left);
			mainPanel.add(panel1);

			// For Right side of the panel
			JLabel checkBxLabel = new JLabel("Select to view ROC:");
			panel1.add(checkBxLabel);

			JPanel chckBoxPanel = new JPanel();
			JCheckBox cb1 = new JCheckBox(randF);
			JCheckBox cb2 = new JCheckBox(naBay);
			JCheckBox cb3 = new JCheckBox(svm);
			JCheckBox cb4 = new JCheckBox(desTree);
			JCheckBox cb5 = new JCheckBox(oneR);
			cb1.setActionCommand(randF);
			cb2.setActionCommand(naBay);
			cb3.setActionCommand(svm);
			cb4.setActionCommand(desTree);
			cb5.setActionCommand(oneR);

			cb1.addItemListener(new ROCCbxItemListner());
			cb2.addItemListener(new ROCCbxItemListner());
			cb3.addItemListener(new ROCCbxItemListner());
			cb4.addItemListener(new ROCCbxItemListner());
			cb5.addItemListener(new ROCCbxItemListner());

			chckBoxPanel.setLayout(new BoxLayout(chckBoxPanel, BoxLayout.Y_AXIS));
			chckBoxPanel.add(cb1);
			chckBoxPanel.add(cb2);
			chckBoxPanel.add(cb3);
			chckBoxPanel.add(cb4);
			chckBoxPanel.add(cb5);
			
			// Shwo ROC Button
			JPanel panel2_rb = new JPanel();
			panel2_rb.setLayout(new BorderLayout());
			panel2_rb.add(chckBoxPanel, BorderLayout.CENTER);
			final JButton rocButton = new JButton("Show ROC");
			panel2_rb.add(rocButton, BorderLayout.SOUTH);
			rocButton.addActionListener(new ViewROCListener(chckBoxPanel));

			panel1.add(panel2_rb);
			JPanel panel2 = new JPanel();
			panel2.setBorder(new TitledBorder("Results Screen"));
			panel2.setLayout(new BorderLayout());

			resultsTxArea = new JTextArea();
			resultsTxArea.setText("");
			resultsTxArea.setEditable(false);
			resultsTxArea.setLineWrap(true);
			DefaultCaret caret = (DefaultCaret) resultsTxArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
			panel2.add(new JScrollPane(resultsTxArea));

			mainPanel.add(panel1);
			mainPanel.add(panel2);

			frame.add(mainPanel);
//			frame.pack();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);

		}

	}

	//Listener when any of the checkbooks is selected or reselected
	static class ROCCbxItemListner implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {

			JCheckBox chkBx = (JCheckBox) e.getItem();
			for (String clsOpt : classifierOptions) {
				if (clsOpt.equals(chkBx.getText())) {
					if (chkBx.isSelected()) {
						selectedList.add(clsOpt);
					} else {
						chkBx.setSelected(false);
						selectedList.remove(clsOpt);
					}
				}
			}

		}

	}

// Listener called when 'Show ROC' button is clicked
	static class ViewROCListener implements ActionListener {
		Container chckBoxPanel;

		public ViewROCListener(JPanel chckBoxPanel) {
			this.chckBoxPanel = chckBoxPanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(selectedList);
			Color color = Color.red;
			ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
			for (String selCbox : selectedList) {
				System.out.println(selCbox);
				switch (selCbox) {
				case randF:
					classifierName = randF;
					cl = new RandomForest();
					color = Color.black;
					break;
				case naBay:
					classifierName = naBay;
					cl = new NaiveBayes();
					color = Color.blue;
					break;
				case svm:
					classifierName = svm;
					color = Color.green;
					break;
				case desTree:
					classifierName = desTree;
					cl = new J48();
					color = Color.gray;
					break;
				case oneR:
					classifierName = oneR;
					cl = new OneR();
					color = Color.cyan;
					break;
				default:
					System.out.println("None Selected");
				}
				try {
					if (!classifierName.equals(svm)) {
						cl.buildClassifier(data);
					} else {
						cl_svm = new SMO();
						cl_svm.buildClassifier(data);
					}
					System.out.println("Evaluation started for " + classifierName + "......");
					evaluatePredictions(data);
					createPlotForSelectedClassifiers(result, vmc, color, selCbox);
					vmc.setName(result.relationName());
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		}

	}

// Execute on onclick of 'View Result' Button
	static class ViewResultsListener implements ActionListener {

		ButtonGroup group = new ButtonGroup();

		public ViewResultsListener(ButtonGroup group) {
			super();
			this.group = group;
		}

		public void actionPerformed(ActionEvent e) {
			String cmd = group.getSelection().getActionCommand();
			switch (cmd) {
			case randF:
				classifierName = randF;
				cl = new RandomForest();
				break;
			case naBay:
				classifierName = naBay;
				cl = new NaiveBayes();
				break;
			case svm:
				classifierName = svm;
				break;
			case desTree:
				classifierName = desTree;
				cl = new J48();
				break;
			case oneR:
				classifierName = oneR;
				cl = new OneR();
				break;
			case dL:
				classifierName = dL;
				break;
			default:
				System.out.println("None Selected");
			}

			if (classifierName != null) {
				try {

					if (classifierName.equals(dL)) {
						try {
							evaluateDeepLearningPrediction();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					} else {
						if (classifierName.equals(svm)) {
							isSVM = true;
							cl_svm = new SMO();
							cl_svm.buildClassifier(data);
						} else {
							isSVM = false;
							cl.buildClassifier(data);
						}
						evaluatePredictions(data);
						generatePredictions(eval, result, tc, classifierName);
						displayResultsAndConfusionMatrix(eval, classifierName);
					}

				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}

		}

	}
	
	//Evaluate predictions using Deep Learning model
	private static void evaluateDeepLearningPrediction() throws Exception {
//	    Deep Learning using Multilayer Perceptron
		DeepLMigrainePredModelGenerator mg = new DeepLMigrainePredModelGenerator();
		Instances dataset = mg.loadDataset(DATASETPATH);
		Filter filter = new Normalize();

//        divide dataset: 80% training, 20% testing
		int trainSize = (int) Math.round(dataset.numInstances() * 0.8);
		int testSize = dataset.numInstances() - trainSize;
		dataset.randomize(new Debug.Random(1));

//        Normalize dataset
		filter.setInputFormat(dataset);
		Instances datasetnor = Filter.useFilter(dataset, filter);
		Instances traindataset = new Instances(datasetnor, 0, trainSize);
		Instances testdataset = new Instances(datasetnor, trainSize, testSize);

//        Use training dataset to build the classifier
		MultilayerPerceptron mlp = (MultilayerPerceptron) mg.buildClassifier(traindataset);
		displayDeepLearningPrediction(mg, mlp, traindataset, testdataset, filter);

	}

	//Evaluate Predictions for all classifiers(except DeepLearning)
	private static void evaluatePredictions(Instances data) throws Exception {
		eval = new Evaluation(data);
		if (isSVM) {
			eval.crossValidateModel(cl_svm, data, 10, new Random(1));
		} else {
			eval.crossValidateModel(cl, data, 10, new Random(1));
		}
		result = tc.getCurve(eval.predictions(), classIndex);

	}

	public static void main(String[] args) throws Exception {

// 	    load CSV
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(fileName));
//		get instances object
		data = loader.getDataSet();
		data.setClassIndex(data.numAttributes() - 1);

//		List of all available classifiers
		classifierOptions = new ArrayList<String>();
		classifierOptions.add(randF);
		classifierOptions.add(naBay);
		classifierOptions.add(svm);
		classifierOptions.add(desTree);
		classifierOptions.add(oneR);

		new MigrainePredictionClassifierMain();

	}

}

