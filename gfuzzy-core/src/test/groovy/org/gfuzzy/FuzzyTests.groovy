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
		assertEquals 0D, MIN_VALUE
		assertEquals MIN, 0D
		assertEquals 1D, MAX_VALUE
		assertEquals MAX, 1D
	}

	void test_construction() {
		assertEquals MIN_VALUE, new Fuzzy()
		assertEquals 0D, new Fuzzy(0)
		assertEquals 0.25D, new Fuzzy(0.25F)
		assertEquals 0.5D, new Fuzzy(0.5)
		assertEquals 0.75D, new Fuzzy(0.75D)
		assertEquals 1D, new Fuzzy(1)
	}
	
	void test_invalid_construction() {
		println shouldFail(IllegalArgumentException) {new Fuzzy(MIN - diff)}
		println shouldFail(IllegalArgumentException) {new Fuzzy(MAX + diff)}
		println shouldFail(IllegalArgumentException) {new Fuzzy(null)}
		println shouldFail(IllegalArgumentException) {new Fuzzy((Number) null)}
		println shouldFail(ReadOnlyPropertyException) {new Fuzzy("fuzzy":0.5)}
	}
	
	void test_immutable() {
		println shouldFail {fuzzy.fuzzy = 0.75}
	}
	
	void test_as_number() {
		fuzzy = new Fuzzy()
		assertEquals 0D, fuzzy
		assertEquals 0F, fuzzy
		assertEquals 0I, fuzzy
		assertEquals 0L, fuzzy
	}
	
	void test_and() {
		Fuzzy smaller = new Fuzzy(0.24)
		Fuzzy larger = new Fuzzy(0.26)
		assertEquals smaller, larger & smaller
		assertEquals smaller, fuzzy & smaller
		assertEquals fuzzy, fuzzy & larger
	}
	
	void test_or() {
		Fuzzy smaller = new Fuzzy(0.24)
		Fuzzy larger = new Fuzzy(0.26)
		assertEquals larger, smaller | larger
		assertEquals fuzzy, fuzzy | smaller
		assertEquals larger, fuzzy | larger
	}
	
	void test_negate() {
		assertEquals 0D, ~MAX
		assertEquals 1D, ~MIN
		assertEquals 0.75D, ~fuzzy
	}
	
	void test_not() {
		assertEquals 0D, MAX.not()
		assertEquals 1D, MIN.not()
		assertEquals 0.75D, fuzzy.not()
	}
	
	void test_as() {
		assertEquals 0.25, fuzzy as BigDecimal
		assertEquals 0.25D, fuzzy as Double
		assertEquals 0L, fuzzy as Long
		assertEquals 0.25F, fuzzy as Float
		assertEquals 0I, fuzzy as Integer
	}
	
	void test_equals_with_null_and_object() {
		assertNotNull fuzzy
		assertFalse fuzzy == new Object()
	}
	
	void test_equals_with_fuzzy() {
		assertEquals fuzzy, fuzzy.fuzzy
		assertFalse fuzzy == new Fuzzy(0.24)
		assertEquals fuzzy, new Fuzzy(0.25)
		assertFalse fuzzy == new Fuzzy(0.26)
	}
	
	void test_equals_with_numbers() {
		assertFalse fuzzy == 0.24
		assertEquals fuzzy, 0.25
		assertFalse fuzzy == 0.26
	}
	
	void test_hashcode() {
		assertEquals 0.25D.hashCode(), fuzzy.hashCode()
		assertEquals 0.75D.hashCode(), new Fuzzy(0.75).hashCode()
	}
	
	void test_number_values() {
		assertEquals 0.25, fuzzy.doubleValue()
		assertEquals 0.25D, fuzzy.doubleValue()
		assertEquals 0L, fuzzy.longValue()
		assertEquals 0.25F, fuzzy.floatValue()
		assertEquals 0I, fuzzy.intValue()
	}
	
	void test_to_string() {
		fuzzy = new Fuzzy(0.5D)
		String toString = fuzzy.toString()
		assertEquals "0.5", toString
	}
	
}