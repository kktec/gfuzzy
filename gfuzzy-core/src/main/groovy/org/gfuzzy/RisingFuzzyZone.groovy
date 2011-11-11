package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
class RisingFuzzyZone extends FuzzyZone {

	RisingFuzzyZone(String name, Range range) {
		super(name, range)
	}

	void init() {
		fuzzify = { Number value ->
			if (value <= from) { return Fuzzy.MIN }
			else if (value >= to) { return Fuzzy.MAX }
			
			new Fuzzy((value - from) / (to - from))
		}

		defuzzify = { Fuzzy fuzzy -> to * fuzzy }
	}

}