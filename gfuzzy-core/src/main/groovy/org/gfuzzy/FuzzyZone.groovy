package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
abstract class FuzzyZone implements Comparable<FuzzyZone> {

	final String name

	protected final Range range

	def fuzzify

	def defuzzify

	abstract protected void init()

	protected FuzzyZone(String name, Range range) {
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

	Comparable getFrom() {
		range.from
	}

	Comparable getTo() {
		range.to
	}

	@Override
	String toString() {
		"$name($from..$to)"
	}

	@Override
	// NOTE: this never gets called as Groovy uses compareTo
	boolean equals(Object that) {
		this <=> that
	}

	@Override
	int hashCode() {
		name.hashCode()
	}

	@Override
	int compareTo(FuzzyZone that) {
		if (from > that.from || to > that.to) { 1 }
		else if (from < that.from || to < that.to || name != that.name) { -1 }
		else { 0 }
	}
}