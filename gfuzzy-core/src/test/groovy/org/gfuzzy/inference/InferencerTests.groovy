package org.gfuzzy.inference

import static org.gfuzzy.FuzzySetDefinition.*

import org.gfuzzy.Fuzzy
import org.gfuzzy.FuzzySetDefinition

class InferencerTests extends GroovyTestCase {

	FuzzySetDefinition inputSet = definitionForPeaks("input", [N:-5, Z:0, P:5])
	FuzzySetDefinition outputSet = definitionForPeaks("output", [N:-2.5, Z:0, P:2.5])
	
	Inferencer inferencer = new Inferencer()
	
	void test_infer_withoutRules() {
		def input = inputSet.fuzzify(2.5)
		def output = outputSet.fuzzies()
		inferencer.infer([input, output])  
		assert output['N'] == Fuzzy.MIN
		assert output['Z'] == Fuzzy.MIN
		assert output['P'] == Fuzzy.MIN
	}
}
