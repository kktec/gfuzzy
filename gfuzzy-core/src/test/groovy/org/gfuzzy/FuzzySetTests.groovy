package org.gfuzzy

class FuzzySetTests extends GroovyTestCase {

	FuzzySet set = new FuzzySet([n: new Fuzzy(0.4), z: new Fuzzy(0.6), p: new Fuzzy(0.8)])
	
	void test_fuzzies() {
		assert 0.4 == set['n']
		assert 0.6 == set['z']
		assert 0.8 == set['p']
		assert 3 == set.fuzzies.size()
	}
	
	void test_putAt() {
		set['n'] = new Fuzzy(0.5)
		assert 0.5 == set['n']
		
	}
	
	void test_toString() {
		assertEquals '{n=0.4, z=0.6, p=0.8}', set.toString()
	}
}
