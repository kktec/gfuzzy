package org.gfuzzy.inference

import static org.gfuzzy.FuzzySetDefinition.*
import static org.junit.Assert.*

import org.gfuzzy.Fuzzy
import org.gfuzzy.FuzzySet
import org.gfuzzy.FuzzySetDefinition
import org.junit.Test

class InferencerTests {

	FuzzySetDefinition inputDef0 = definitionForPeaks('input0', [N:-5, Z:0, P:5])
	FuzzySetDefinition inputDef1 = definitionForPeaks('input1', [N:-15, Z:0, P:15])
	FuzzySetDefinition inputDef2 = definitionForPeaks('input2', [N:-25, Z:0, P:25])
	FuzzySetDefinition outputDef = definitionForPeaks('output', [N:-2.5, Z:0, P:2.5])

	def fuzzySet0 = inputDef0.fuzzify(2.5)
	def fuzzySet1 = inputDef1.fuzzify(5)
	def fuzzySet2 = inputDef2.fuzzify(-6)

	Inferencer inferencer = new Inferencer(outputDef)

	@Test
	void infer_withoutRules() {
		assertEquals 0, inferencer.rules.size()
		assertEquals 0, infer(), 0.01
	}

	@Test
	void infer_withRules_usingDefaultWeight() {
		inferencer
				.rule('N', [input0:'Z', input1:'Z', input2:'N'])
				.rule('Z', [input0:'Z', input1:'Z', input2:'Z'])
				.rule('P', [input0:'Z', input1:'P'])
		assertEquals 0.22, infer(), 0.01
	}

	@Test
	void infer_withWeightedRules() {
		inferencer
				.rule('N', [input0:'Z', input1:'Z', input2:'N'], 0.1)
				.rule('Z', [input0:'Z', input1:'Z', input2:'Z'], 0.5)
				.rule('P', [input0:'Z', input1:'P'], 0.9)
		assertEquals 1.20, infer(), 0.01
	}

	@Test
	void can_stringifyRules() {
		inferencer
				.rule('N', [input0:'Z'])
				.rule('X', [input0:'P', input1:'Z', input2:'N'], 0.5)
		def expectations = [
			"IF input0.Z THEN output.N * 1.0",
			"IF input0.P AND input1.Z AND input2.N THEN output.X * 0.5"
		]
		def actuals = inferencer.stringifyRules()
		for (int i = 0; i < expectations.size(); i++) { 
			assertEquals expectations[i], actuals[i]
		}
		assertEquals inferencer.rules.size(), actuals.size()
	}

	private double infer() {
		inferencer.infer([input0: fuzzySet0, input1: fuzzySet1, input2: fuzzySet2])
	}

}
