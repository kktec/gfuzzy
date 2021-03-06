package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
class RisingFuzzyZoneTests extends FuzzyZoneTestCase {
	
	void test_fuzzify() {
		zone.with {
			assertEquals zoneName, name
			assertEquals 0D, fuzzify(-50.1)
			assertEquals 0D, fuzzify(-50)
			assertEquals 0.25D, fuzzify(0F)
			assertEquals 0.5D, fuzzify(50)
			assertEquals 0.75D, fuzzify(100D)
			assertEquals 1D, fuzzify(150)
			assertEquals 1D, fuzzify(150.1)
		}
	}
	
	void test_defuzzify() {
		assertEquals(150D, zone.defuzzify(Fuzzy.MAX))
		assertEquals(75D, zone.defuzzify(new Fuzzy(0.5)))
		assertEquals(0D, zone.defuzzify(Fuzzy.MIN))
	}
	
	protected FuzzyZone create(String name, Range range) {
		createRisingFuzzyZone(name, range)
	}
	
	static FuzzyZone createRisingFuzzyZone(String name, Range range) {
		new RisingFuzzyZone(name, range)
	}
	
}