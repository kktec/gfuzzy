package org.gfuzzy

import groovy.lang.Immutable;

/**
 * 
 * Representation of an immutable Fuzzy Logic value as a double in the range of 0.0 through 1.0. 
 * Standard Fuzzy Logic operations are supported such as AND, OR, and NOT.
 * 
 * @author Ken Krebs
 */
class Fuzzy extends Number {

	static final MIN_VALUE = 0D

	static final MAX_VALUE = 1D

	static final MIN = new Fuzzy(MIN_VALUE)

	static final MAX = new Fuzzy(MAX_VALUE)

	private final double fuzzy

	Fuzzy() {
	}

	Fuzzy(Number fuzzy) {
		if(fuzzy == null) {
			throw new IllegalArgumentException("Fuzzy: cannot be null")
		}

		if(fuzzy < MIN_VALUE) {
			throw new IllegalArgumentException("Fuzzy: $fuzzy must be >= $MIN")
		}

		if(fuzzy > MAX_VALUE) {
			throw new IllegalArgumentException("Fuzzy: $fuzzy must be <= $MAX")
		}

		this.fuzzy = fuzzy.doubleValue()
	}

	Fuzzy and(Fuzzy fuzzy) {
		new Fuzzy(Math.min(this.fuzzy, fuzzy.fuzzy))
	}

	Fuzzy or(Fuzzy fuzzy) {
		new Fuzzy(Math.max(this.fuzzy, fuzzy.fuzzy))
	}

	Fuzzy bitwiseNegate() {
		new Fuzzy(MAX_VALUE - fuzzy)
	}

	Fuzzy not() {
		bitwiseNegate()
	}

	double doubleValue() {
		fuzzy
	}

	float floatValue() {
		fuzzy
	}

	long longValue() {
		fuzzy
	}

	int intValue() {
		fuzzy
	}

	@Override
	boolean equals(Object other) {
		if(other instanceof Fuzzy) {
			return fuzzy == other.fuzzy
		}

		false
	}

	@Override
	int hashCode() {
		fuzzy.hashCode()
	}

	@Override
	String toString() {
		fuzzy.toString()
	}
}