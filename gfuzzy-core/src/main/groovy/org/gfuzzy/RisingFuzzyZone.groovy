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
			if (value <= range.from) { return Fuzzy.MIN }
			else if (value >= range.to) { return Fuzzy.MAX }
			
			new Fuzzy((value - range.from) / (range.to - range.from))
		}

		defuzzify = { Fuzzy fuzzy -> range.to * fuzzy }
	}

}