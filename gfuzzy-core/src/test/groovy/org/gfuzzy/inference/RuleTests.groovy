package org.gfuzzy.inference

import org.junit.Test

class RuleTests {

	Rule rule = new Rule('z', [i0: 'x', i1: 'y'], 1.0)

	@Test
	void test_rule() {
		assert 'z' == rule.zone
		assert 'x' == rule.predicates['i0']
		assert 'y' == rule.predicates['i1']
		assert 1d == rule.weight
	}

	@Test
	void test_immutable() {
		new GroovyTestCase().with {
			shouldFail { rule.zone = 'zone' }
			shouldFail { rule.predicates = [:] }
			shouldFail { rule.weight = 0.5 }
		}
	}
}
