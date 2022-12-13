package proj.weka.main;

import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class DeepLMigrainePredModelClassifier {

	private DeepLMigrainePredModelAttributes modelAtrr = new DeepLMigrainePredModelAttributes();

	private ArrayList<Attribute> attributes;
	private ArrayList<String> classType;
	private Instances dataRaw;

	public DeepLMigrainePredModelClassifier() {
		modelAtrr.setAge(new Attribute("Age"));
		modelAtrr.setDuration(new Attribute("Duration"));
		modelAtrr.setFrequency(new Attribute("Frequency"));
		modelAtrr.setLocation(new Attribute("Location"));
		modelAtrr.setCharacter(new Attribute("Character"));
		modelAtrr.setIntensity(new Attribute("Intensity"));
		modelAtrr.setNausea(new Attribute("Nausea"));
		modelAtrr.setVomit(new Attribute("Vomit"));
		modelAtrr.setPhonophobia(new Attribute("Phonophobia"));
		modelAtrr.setPhotophobia(new Attribute("Photophobia"));
		modelAtrr.setVisual(new Attribute("Visual"));
		modelAtrr.setSensory(new Attribute("Sensory"));
		modelAtrr.setDysphasia(new Attribute("Dysphasia"));
		modelAtrr.setDysarthria(new Attribute("Dysarthria"));
		modelAtrr.setVertigo(new Attribute("Vertigo"));
		modelAtrr.setTinnitus(new Attribute("Tinnitus"));
		modelAtrr.setHypoacusis(new Attribute("Hypoacusis"));
		modelAtrr.setDiplopia(new Attribute("Diplopia"));
		modelAtrr.setDefect(new Attribute("Defect"));
		modelAtrr.setAtaxia(new Attribute("Ataxia"));
		modelAtrr.setConscience(new Attribute("Conscience"));
		modelAtrr.setParesthesia(new Attribute("Paresthesia"));
		modelAtrr.setDPF(new Attribute("DPF"));

		attributes = new ArrayList<Attribute>();
		classType = new ArrayList<String>();
		classType.add("Typical aura with migraine");
		classType.add("Migraine without aura");
		classType.add("Familial hemiplegic migraine");
		classType.add("Typical aura without migraine");
		classType.add("Basilar-type aura");
		classType.add("Other");
		classType.add("Sporadic hemiplegic migraine");

		attributes.add(modelAtrr.getAge());
		attributes.add(modelAtrr.getDuration());
		attributes.add(modelAtrr.getFrequency());
		attributes.add(modelAtrr.getLocation());
		attributes.add(modelAtrr.getCharacter());
		attributes.add(modelAtrr.getIntensity());
		attributes.add(modelAtrr.getNausea());
		attributes.add(modelAtrr.getVomit());
		attributes.add(modelAtrr.getPhonophobia());
		attributes.add(modelAtrr.getPhotophobia());
		attributes.add(modelAtrr.getVisual());
		attributes.add(modelAtrr.getSensory());
		attributes.add(modelAtrr.getDysphasia());
		attributes.add(modelAtrr.getDysarthria());
		attributes.add(modelAtrr.getVertigo());
		attributes.add(modelAtrr.getTinnitus());
		attributes.add(modelAtrr.getHypoacusis());
		attributes.add(modelAtrr.getDiplopia());
		attributes.add(modelAtrr.getDefect());
		attributes.add(modelAtrr.getAtaxia());
		attributes.add(modelAtrr.getConscience());
		attributes.add(modelAtrr.getParesthesia());
		attributes.add(modelAtrr.getDPF());

		attributes.add(new Attribute("class", classType));
		dataRaw = new Instances("TestInstances", attributes, 0);
		dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
	}

	public Instances createInstance(double age, double duration, double frequency, double location, double character,
			double intensity, double nausea, double vomit, double phonophobia, double photophobia, double visual,
			double sensory, double dysphasia, double dysarthria, double vertigo, double tinnitus, double hypoacusis,
			double diplopia, double defect, double ataxia, double conscience, double paresthesia, double dPF,
			double result) {
		dataRaw.clear();
		double[] instanceValue1 = new double[] { age, duration, frequency, location, character, intensity, nausea,
				vomit, phonophobia, photophobia, visual, sensory, dysphasia, dysarthria, vertigo, tinnitus, hypoacusis,
				diplopia, defect, ataxia, conscience, paresthesia, dPF, 0 };
		dataRaw.add(new DenseInstance(1.0, instanceValue1));
		return dataRaw;
	}

	public String classifiy(Instances insts, String path) {
		String result = "Not classified!!";
		Classifier cls = null;
		try {
			cls = (MultilayerPerceptron) SerializationHelper.read(path);
			result = classType.get((int) cls.classifyInstance(insts.firstInstance()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public Instances getInstance() {
		return dataRaw;
	}

}