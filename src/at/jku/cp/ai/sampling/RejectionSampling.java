package at.jku.cp.ai.sampling;

import java.util.HashMap;
public class RejectionSampling {

	BayesNetSampler bn;
	public RejectionSampling(BayesNetSampler bn) {		
		this.bn = bn;
	}
	
	/**
	 * Returns the probability P(X | evidence) via rejection sampling.
	 * Example: What is the probability of rain given the lawn is wet? 
	 * Probabilistic query: P(R|W)
	 * Variables: X and evidence are HashMaps X={"R":true}, evidence={"W":true}
	 * @param X event (x1, ... xk) stored as a HashMap of variable names and true/false values
	 * @param evidence evidence (e1, ... en) stored as HashMap of variable names and true/false values
	 * @param M sample M times from the Bayes Net
	 * @return P(X | evidence)
	 */
	public double estimate(HashMap<String, Boolean> X, HashMap<String, Boolean> evidence, int M) {
		double estimatedProb = 0.0;
		// TODO: implement this function
		return estimatedProb;
	}
		
}
