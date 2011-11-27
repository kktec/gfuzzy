package org.gfuzzy.inference

import static org.gfuzzy.FuzzySetDefinition.*

import org.gfuzzy.Fuzzy
import org.gfuzzy.FuzzySetDefinition

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

	void test_infer_withoutRules() {
		assert 0 == inferencer.rules.size()
		inferencer.infer([input0: input0, input1: input1, input2: input2], output)
		assert output['N'] == Fuzzy.MIN
		assert output['Z'] == Fuzzy.MIN
		assert output['P'] == Fuzzy.MIN
	}

	void test_infer_withRules() {
		inferencer
				.rule('N', [input0: 'Z', input1: 'Z', input2: 'N'])
				.rule('Z', [input0: 'Z', input1: 'Z', input2: 'Z'])
				.rule('P', [input0: 'Z', input1: 'P'])
		assertRules()
	}
	
	void test_stringify() {
		inferencer.rule('N', [input0: 'Z']).rule('X', [input0: 'P', input1: 'Z', input2: 'N'])
		def expected = [
			"IF input0.Z THEN output.N",
			"IF input0.P AND input1.Z AND input2.N THEN output.X"
		]
		def actual = inferencer.stringify()
		assert 2 == actual.size()
		assert expected == actual
	}

	private assertRules() {
		assert 3 == inferencer.rules.size()
		inferencer.infer([input0: input0, input1: input1, input2: input2], output)
		assertEquals new Fuzzy(0.24).doubleValue(), output['N'].doubleValue(),  0.01d
		assertEquals new Fuzzy(0.5).doubleValue(), output['Z'].doubleValue(),  0.01d
		assertEquals new Fuzzy(0.33).doubleValue(), output['P'].doubleValue(),  0.01d
	}
}
