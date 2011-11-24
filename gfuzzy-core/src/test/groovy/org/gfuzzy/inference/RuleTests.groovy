package org.gfuzzy.inference

class RuleTests extends GroovyTestCase {
	
	Rule rule = new Rule('z', [i0: 'x', i1: 'y'])
	
	void test_rule() {
		assert 'z' == rule.zone
		assert 'x' == rule.predicates['i0']
		assert 'y' == rule.predicates['i1']
	}
	
	void test_immutable() {
		shouldFail { rule.zone = 'zone' }
		shouldFail { rule.predicates = [:] }
	}
	
}
