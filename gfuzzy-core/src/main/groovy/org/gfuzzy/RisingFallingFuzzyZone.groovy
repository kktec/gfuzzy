package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
class RisingFallingFuzzyZone extends FuzzyZone {

	final Double peak

	RisingFallingFuzzyZone(String name, Range range) {
		super(name, range)
		peak = mid()
	}

	RisingFallingFuzzyZone(String name, Range range, Number peak) {
		super(name, range)
		if(!range.containsWithinBounds(peak.doubleValue())) {
			throw new IllegalArgumentException("peak value $peak must be in range ${range.from} to ${range.to}")
		}
		this.peak = peak ?: mid()
	}

	void init() {
		fuzzify = { Number value ->
			if (value <= from) {return Fuzzy.MIN }
			else if (value >= to) {return Fuzzy.MIN }
			else if (value == peak) { return Fuzzy.MAX }
			else if (value < peak) { return new Fuzzy((value - from) / (peak - from)) }
			
			new Fuzzy((value - peak) / (to - peak)).not()
		}

		defuzzify = { Fuzzy fuzzy -> peak * fuzzy }
	}

	@Override
	String toString() {
		"$name($from..$peak..$to)"
	}

}