package org.gfuzzy

import static org.gfuzzy.Fuzzy.*

/**
 * @author Ken Krebs
 *
 */
class FuzzyTests extends GroovyTestCase {
	
	Fuzzy fuzzy = new Fuzzy(0.25)
	
	double diff = 0.000001
	
	void test_limits() {
		assert 0D == MIN_VALUE
		assert MIN.doubleValue() == 0D
		assert 1D == MAX_VALUE
		assert MAX.doubleValue() == 1D
	}

	void test_construction() {
		assert 0D == new Fuzzy()
		assert 0D == new Fuzzy(0)
		assert 0.25D == new Fuzzy(0.25F)
		assert 0.5D == new Fuzzy(0.5)
		assert 0.75D == new Fuzzy(0.75D)
		assert 1D == new Fuzzy(1)
	}
	
	void test_invalid_construction() {
		shouldFail(IllegalArgumentException) {new Fuzzy(MIN - diff)}
		shouldFail(IllegalArgumentException) {new Fuzzy(MAX + diff)}
		shouldFail(IllegalArgumentException) {new Fuzzy(null)}
		shouldFail(IllegalArgumentException) {new Fuzzy((Number) null)}
		shouldFail(ReadOnlyPropertyException) {new Fuzzy(fuzzy: 0.5)}
	}
	
	void test_immutable() {
		shouldFail {fuzzy.fuzzy = 0.75}
	}
	
	void test_and() {
		Fuzzy smaller = new Fuzzy(0.24)
		Fuzzy larger = new Fuzzy(0.26)
		assert smaller == (larger & smaller)
		assert smaller == (fuzzy & smaller)
		assert fuzzy == (fuzzy & larger)
	}
	
	void test_or() {
		Fuzzy smaller = new Fuzzy(0.24)
		Fuzzy larger = new Fuzzy(0.26)
		assert larger == (smaller | larger)
		assert fuzzy == (fuzzy | smaller)
		assert larger == (fuzzy | larger)
	}
	
	void test_negate() {
		assert 0D == ~MAX
		assert 1D == ~MIN
		assert 0.75D == ~fuzzy
	}
	
	void test_not() {
		assert 0D == MAX.not()
		assert 1D == MIN.not()
		assert 0.75D == fuzzy.not()
	}
	
	void test_as() {
		assert 0.25 == fuzzy as BigDecimal
		assert 0.25D == fuzzy as Double
		assert 0L == fuzzy as Long
		assert 0.25F == fuzzy as Float
		assert 0I == fuzzy as Integer
	}
	
	void test_equals_with_null_and_object() {
		assert fuzzy != null 
		assert fuzzy != new Object()
	}
	
	void test_equals_with_fuzzy() {
		assert fuzzy == fuzzy
		assert fuzzy != new Fuzzy(0.24)
		assert fuzzy == new Fuzzy(0.25)
		assert fuzzy != new Fuzzy(0.26)
	}
	
	void test_hashcode() {
		assert 0.25D.hashCode() == fuzzy.hashCode()
		assert 0.75D.hashCode() == new Fuzzy(0.75).hashCode()
	}
	
	void test_number_values() {
		assert 0.25 == fuzzy.doubleValue()
		assert 0.25D == fuzzy.doubleValue()
		assert 0L == fuzzy.longValue()
		assert 0.25F == fuzzy.floatValue()
		assert 0I == fuzzy.intValue()
	}
	
	void test_to_string() {
		assert '0.25' == fuzzy.toString()
	}
	
}