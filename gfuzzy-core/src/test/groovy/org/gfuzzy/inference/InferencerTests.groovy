package org.gfuzzy.inference

import static org.gfuzzy.FuzzySetDefinition.*

import org.gfuzzy.Fuzzy
import org.gfuzzy.FuzzySetDefinition
import org.junit.Test

class InferencerTests extends GroovyTestCase {

	FuzzySetDefinition inputSet0 = definitionForPeaks("input0", [N:-5, Z:0, P:5])
	FuzzySetDefinition inputSet1 = definitionForPeaks("input1", [N:-15, Z:0, P:15])
	FuzzySetDefinition inputSet2 = definitionForPeaks("input2", [N:-25, Z:0, P:25])
	FuzzySetDefinition outputSet = definitionForPeaks("output", [N:-2.5, Z:0, P:2.5])

	Inferencer inferencer = new Inferencer('output')

	def input0 = inputSet0.fuzzify(2.5)
	def input1 = inputSet1.fuzzify(5)
	def input2 = inputSet2.fuzzify(-6)
	def output = outputSet.set()

	@Test
	void test_infer_withoutRules() {
		assert 0 == inferencer.rules.size()
		inferencer.infer([input0: input0, input1: input1, input2: input2], output)
		assert output['N'] == Fuzzy.MIN
		assert output['Z'] == Fuzzy.MIN
		assert output['P'] == Fuzzy.MIN
	}

	void test_infer_withRulesUsingDefaultWeight() {
		inferencer
				.rule('N', [input0: 'Z', input1: 'Z', input2: 'N'])
				.rule('Z', [input0: 'Z', input1: 'Z', input2: 'Z'])
				.rule('P', [input0: 'Z', input1: 'P'])
		assertRulesUsingDefaultWeight()
	}
	
	private assertRulesUsingDefaultWeight() {
		assert 3 == inferencer.rules.size()
		inferencer.infer([input0: input0, input1: input1, input2: input2], output)
		assertEquals new Fuzzy(0.24).doubleValue(), output['N'].doubleValue(),  0.01d
		assertEquals new Fuzzy(0.5).doubleValue(), output['Z'].doubleValue(),  0.01d
		assertEquals new Fuzzy(0.33).doubleValue(), output['P'].doubleValue(),  0.01d
	}

		void test_infer_withWeightedRules() {
		inferencer
				.rule('N', [input0: 'Z', input1: 'Z', input2: 'N'], new Fuzzy(0.1))
				.rule('Z', [input0: 'Z', input1: 'Z', input2: 'Z'], new Fuzzy(0.5))
				.rule('P', [input0: 'Z', input1: 'P'], new Fuzzy(0.9))
		assertWeightedRules()
	}
	
	private assertWeightedRules() {
		assert 3 == inferencer.rules.size()
		inferencer.infer([input0: input0, input1: input1, input2: input2], output)
		assertEquals new Fuzzy(0.024).doubleValue(), output['N'].doubleValue(),  0.01d
		assertEquals new Fuzzy(0.25).doubleValue(), output['Z'].doubleValue(),  0.01d
		assertEquals new Fuzzy(0.3).doubleValue(), output['P'].doubleValue(),  0.01d
	}
	
	void test_stringify() {
		inferencer.rule('N', [input0: 'Z']).rule('X', [input0: 'P', input1: 'Z', input2: 'N'], new Fuzzy(0.5))
		def expected = [
			"IF input0.Z THEN output.N 1.0",
			"IF input0.P AND input1.Z AND input2.N THEN output.X 0.5"
		]
		def actual = inferencer.stringify()
		assert 2 == actual.size()
		assert expected == actual
	}

}
