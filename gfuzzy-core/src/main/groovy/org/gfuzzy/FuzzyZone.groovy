package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
abstract class FuzzyZone {

	final String name

	final Range range

	def fuzzify

	def defuzzify

	abstract protected void init()

	FuzzyZone(String name, Range range) {
		if(!name) {
			throw new IllegalArgumentException("name cannot be null")
		}
		this.name = name

		if(!range) {
			throw new IllegalArgumentException("range cannot be null")
		}
		this.range = new ObjectRange(new Double(range.from), new Double(range.to))

		init()
	}

	Number mid() {
		(range.to - range.from) * 0.5D + range.from
	}

	boolean contains(Number value) {
		range.containsWithinBounds(value.doubleValue())
	}

	@Override
	String toString() {
		"$name($range.from..$range.to)"
	}

}