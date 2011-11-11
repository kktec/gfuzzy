package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
abstract class FuzzyZone {

	final String name

	private final Range range

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
		(to - from) * 0.5D + from
	}

	boolean contains(Number value) {
		range.containsWithinBounds(value.doubleValue())
	}
	
	Comparable getFrom() { range.from }

	Comparable getTo() { range.to }

	@Override
	String toString() {
		"$name($from..$to)"
	}

}