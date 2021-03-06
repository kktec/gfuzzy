package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
class FallingFuzzyZoneTests extends FuzzyZoneTestCase {
	
	void test_fuzzify() {
		zone.with {
			assertEquals zoneName, name
			assertEquals 1D, fuzzify(-50.1)
			assertEquals 1D, fuzzify(-50)
			assertEquals 0.75D, fuzzify(0F)
			assertEquals 0.5D, fuzzify(50)
			assertEquals 0.25D, fuzzify(100D)
			assertEquals 0D, fuzzify(150)
			assertEquals 0D, fuzzify(150.1)
		}
	}
	
	void test_defuzzify() {
		zone.with {
			assertEquals(-50D, defuzzify(Fuzzy.MAX))
			assertEquals(-25D, defuzzify(new Fuzzy(0.5)))
			assertEquals(0D, Math.abs(defuzzify(Fuzzy.MIN)))
		}
	}
	
	protected FuzzyZone create(String name, Range range) {
		createFallingFuzzyZone(name, range)
	}
	
	static FuzzyZone createFallingFuzzyZone(String name, Range range) {
		new FallingFuzzyZone(name, range)
	}
	
}