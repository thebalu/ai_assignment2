package at.jku.cp.ai.tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.cp.ai.sampling.BayesNetSampler;
import at.jku.cp.ai.sampling.NodeProbabilityTable;
public class TestBayesNetSampler {
	
	static BayesNetSampler bn_rau = null;
	static BayesNetSampler bn_weather = null;
	static int N = 1000000;
	static Double delta = 0.01;
	
	private static void initRAUNet() {
		List<String> tpo = Arrays.asList("RA", "S", "RE", "HU", "PA", "CO", "CH");
		bn_rau = new BayesNetSampler(tpo);
		NodeProbabilityTable ptRA = new NodeProbabilityTable("RA", null);
		ptRA.setProbability("", 0.1);
		NodeProbabilityTable ptS = new NodeProbabilityTable("S", Arrays.asList("RA"));
		ptS.setProbability("0", 0.02);
		ptS.setProbability("1", 0.8);
		NodeProbabilityTable ptRE = new NodeProbabilityTable("RE", Arrays.asList("RA"));
		ptRE.setProbability("0", 0.7);
		ptRE.setProbability("1", 0.01);
		NodeProbabilityTable ptHU = new NodeProbabilityTable("HU", Arrays.asList("RE"));
		ptHU.setProbability("0", 0.3);
		ptHU.setProbability("1", 0.8);
		NodeProbabilityTable ptPA = new NodeProbabilityTable("PA", null);
		ptPA.setProbability("", 0.3);
		NodeProbabilityTable ptCO = new NodeProbabilityTable("CO", Arrays.asList("HU", "RE", "PA"));
		ptCO.setProbability("000", 0.01);
		ptCO.setProbability("001", 0.24);
		ptCO.setProbability("010", 0.18);
		ptCO.setProbability("011", 0.37);
		ptCO.setProbability("100", 0.2);
		ptCO.setProbability("101", 0.87);
		ptCO.setProbability("110", 0.33);
		ptCO.setProbability("111", 0.95);
		NodeProbabilityTable ptCH = new NodeProbabilityTable("CH", Arrays.asList("CO"));
		ptCH.setProbability("0", 0.25);
		ptCH.setProbability("1", 0.9);
		
		bn_rau.addProbabilities("RA", ptRA);
		bn_rau.addProbabilities("S", ptS);
		bn_rau.addProbabilities("RE", ptRE);
		bn_rau.addProbabilities("HU", ptHU);
		bn_rau.addProbabilities("PA", ptPA);
		bn_rau.addProbabilities("CO", ptCO);
		bn_rau.addProbabilities("CH", ptCH);
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		initRAUNet();
	}
	
	@Test
	public void TestRainbowAndParty()
	{	
	
		int countRainbow = 0;
		int countParty = 0;
		
		for (int i = 0; i < N; ++i) {
			HashMap<String, Boolean> sample = bn_rau.getPriorSample();
			if (sample.get("RA")) {
				++countRainbow;
			}
			if (sample.get("PA")) {
				++countParty;	
			}

		}

		assertEquals(0.1, countRainbow/(double) N, delta); 
		assertEquals(0.3, countParty/(double) N, delta);
	}
	
	@Test
	public void TestCheerful() {
		int countCheerfulCO0 = 0;
		int countNotCheerfoolCO0 = 0;
		int countCheerfulCO1 = 0;
		
		int countCO0 = 0;
		
		for (int i = 0; i < N; ++i) {
			HashMap<String, Boolean> sample = bn_rau.getPriorSample();
				
			if (sample.get("CO")) {
				if (sample.get("CH")) {
					++countCheerfulCO1;
				}
			} else { // !colorful
				if (sample.get("CH")) {
					++countCheerfulCO0;
				} else {
					++countNotCheerfoolCO0;
				}
				++countCO0;
			}
		}
		
		int countCO1 = N - countCO0;
		
		System.out.println(countCheerfulCO0/(double)countCO0);
		System.out.println(countNotCheerfoolCO0/(double)countCO0);
		System.out.println(countCheerfulCO1/(double)countCO1);
		
		assertEquals(0.25, countCheerfulCO0/(double)countCO0, delta);
		assertEquals(0.9, countCheerfulCO1/(double)countCO1, delta);
	}
	
	@Test
	public void TestColorful() {
		int countColorfulHU1RE0PA1 = 0;
		int countColorfulHU0RE0PA1 = 0;
		int countColorfulHU1RE1PA1 = 0;
		
		int countHU1RE0PA1 = 0;
		int countHU0RE0PA1 = 0;
		int countHU1RE1PA1 = 0;
		
		for (int i = 0; i < N; ++i) {
			HashMap<String, Boolean> sample = bn_rau.getPriorSample();
		
			if (sample.get("HU") && !sample.get("RE") && sample.get("PA")) {
				if (sample.get("CO")) {
					++countColorfulHU1RE0PA1;
				}
				++countHU1RE0PA1;
			} else if (!sample.get("HU") && !sample.get("RE") && sample.get("PA")) {
				if (sample.get("CO")) {
					++countColorfulHU0RE0PA1;
				}
				++countHU0RE0PA1;
			} else if (sample.get("HU") && sample.get("RE") && sample.get("PA")) {
				if (sample.get("CO")) {
					++countColorfulHU1RE1PA1;
				}
				++countHU1RE1PA1;
			}
			
		}
		
		System.out.println(countColorfulHU1RE0PA1/(double)countHU1RE0PA1);
		System.out.println(countColorfulHU0RE0PA1/(double)countHU0RE0PA1);
		System.out.println(countColorfulHU1RE1PA1/(double)countHU1RE1PA1);
		
		assertEquals(0.87, countColorfulHU1RE0PA1/(double)countHU1RE0PA1, delta);
		assertEquals(0.24, countColorfulHU0RE0PA1/(double)countHU0RE0PA1, delta);
		assertEquals(0.95, countColorfulHU1RE1PA1/(double)countHU1RE1PA1, delta);
	}
	
}
