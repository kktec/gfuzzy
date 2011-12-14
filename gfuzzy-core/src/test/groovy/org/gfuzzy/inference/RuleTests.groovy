package org.gfuzzy.inference

import static org.junit.Assert.*

import org.gfuzzy.Fuzzy
import org.junit.Test

class RuleTests {

	// set names
	static final String INPUT0 = 'input0'
	static final String INPUT1 = 'input1'
	static final String OUTPUT = 'output'

	// zone names
	static final String Z = 'Z'
	static final String N = 'N'
	static final String P = 'P'

	Rule rule = new Rule(OUTPUT, Z, [input0:N, input1:P], 0.5)

	@Test
	void all_valuesSet() {
		assertRuleValuesWithWeight(0.5d)
	}

	@Test
	void valuesSet_withDefaultWeight() {
		rule = new Rule(OUTPUT, Z, [input0:N, input1:P])
		assertRuleValuesWithWeight Rule.DEFAULT_WEIGHT
	}

	@Test
	void immutable() {
		new GroovyTestCase().with {
			shouldFail { rule.set = 'set' }
			shouldFail { rule.zone = 'z' }
			shouldFail { rule.predicates = [:] }
			shouldFail { rule.weight = 0.5 }
		}
	}
	
	@Test
	void process_andsThePredicates_andAppliesTheWeight() {
		def input0 = [N: new Fuzzy(0.4), Z: Fuzzy.MIN, P: Fuzzy.MIN]
		def input1 = [N: Fuzzy.MIN, Z: Fuzzy.MIN, P: new Fuzzy(0.6)]
		def inputs = [input0: input0, input1: input1]
		assertEquals "should be the lesser of the applied predicates multiplied by weight",
			new Fuzzy(0.2), rule.process(inputs)
	}

	@Test
	void asString() {
		assertEquals 'IF input0.N AND input1.P THEN output.Z * 0.5', rule.toString()
	}

	private assertRuleValuesWithWeight(double weight) {
		assertEquals Z, rule.zone
		assertEquals N, rule.predicates[INPUT0]
		assertEquals P, rule.predicates[INPUT1]
		assertEquals weight, rule.weight, 0.1
	}
}
