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
		if(!this.range.containsWithinBounds(peak.doubleValue())) {
			throw new IllegalArgumentException("peak value $peak must be in range ${this.range.from} to ${this.range.to}")
		}
		this.peak = peak ?: mid()
	}

	void init() {
		fuzzify = { Number value ->
			if (value <= range.from) {return Fuzzy.MIN }
			else if (value >= range.to) {return Fuzzy.MIN }
			else if (value == peak) { return Fuzzy.MAX }
			else if (value < peak) { return new Fuzzy((value - range.from) / (peak - range.from)) }
			
			new Fuzzy((value - peak) / (range.to - peak)).not()
		}

		defuzzify = { Fuzzy fuzzy -> peak * fuzzy }
	}

	@Override
	String toString() {
		"$name($range.from..$peak..$range.to)"
	}

}