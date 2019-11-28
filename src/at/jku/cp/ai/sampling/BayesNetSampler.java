package at.jku.cp.ai.sampling;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BayesNetSampler {

	// topological ordering used for sampling
	List<String> topologicalOrdering;
	
	public List<String> getTopologicalOrdering() {
		return topologicalOrdering;
	}

	// stores the conditional probability table for each node of the Bayes Net
	HashMap<String, NodeProbabilityTable> conditionalProbabilityTable;
	
	// stores for each variable on which other variables it depends on
	HashMap<String, List<String>> conditionedOn;

	public BayesNetSampler(List<String> topologicalOrdering) {		
		this.topologicalOrdering = topologicalOrdering;
		this.conditionalProbabilityTable = new HashMap<>();
		this.conditionedOn = new HashMap<>();
	}
	
	public void addProbabilities(String event, NodeProbabilityTable prob) {
		conditionalProbabilityTable.put(event, prob);
		conditionedOn.put(event, prob.getConditionalVars());
	}
	
	/**
	 * Gets a random sample from a Bayesian network.
	 * @return sample: a HashMap of variable names and true/false values. 
	 * Example: Querying the Weather Network could return
	 * sample = {"Cloudy": true, "Sprinkler": false, "Rain": true, "Wet": true}
	 */
	public HashMap<String, Boolean> getPriorSample() {
		Random random = new Random();
		HashMap<String, Boolean> sample = new HashMap<>();
		for (String s : topologicalOrdering) {
			List<String> lookup = conditionedOn.get(s);
			String cond = "";
			if (lookup != null) {
				for (String l : lookup) {
					Boolean b = sample.get(l);
					cond += b ? "1" : "0";
				}
			}
			Double p = conditionalProbabilityTable.get(s).getProbability(cond);
			sample.put(s, random.nextDouble() < p);
		}
		return sample;
	}
}
