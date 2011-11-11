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
			if (value <= from) { return Fuzzy.MAX }
			else if (value >= to) { return Fuzzy.MIN }
			
			new Fuzzy((value - from) / (to - from)).not()
		}

		defuzzify = { Fuzzy fuzzy -> from * fuzzy }
	}

}