package org.gfuzzy


/**
 * @author Ken Krebs
 *
 */
class FallingFuzzyZone extends FuzzyZone {

	FallingFuzzyZone(String name, Range range) {
		super(name, range)
	}

	void init() {
		fuzzify = { Number value ->
			if (value <= range.from) { return Fuzzy.MAX }
			else if (value >= range.to) { return Fuzzy.MIN }
			
			new Fuzzy((value - range.from) / (range.to - range.from)).not()
		}

		defuzzify = { Fuzzy fuzzy -> range.from * fuzzy }
	}

}