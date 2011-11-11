package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
class RisingFallingFuzzyZoneTests extends FuzzyZoneTestCase {
	
	void test_with_invalid_peak() {
		println shouldFail(IllegalArgumentException) { new RisingFallingFuzzyZone(zoneName, 0D..10D, 20D) }
	}
	
	void test_asymmetric_bipolar_fuzzify() {
		// FIXME: not asymmetric
		zone = create(zoneName, -50..150)
		zone.with {
			assertEquals zoneName, name
			assertEquals 0D, fuzzify(-50.1)
			assertEquals 0D, fuzzify(-50)
			assertEquals 0.25D, fuzzify(-25)
			assertEquals 0.5D, fuzzify(0F)
			assertEquals 0.75D, fuzzify(25)
			assertEquals 1D, fuzzify(50)
			assertEquals 0.75D, fuzzify(75)
			assertEquals 0.5D, fuzzify(100D)
			assertEquals 0.25D, fuzzify(125)
			assertEquals 0D, fuzzify(150)
			assertEquals 0D, fuzzify(150.1)
		}
	}
	
	void test_symmetric_bipolar_fuzzify() {
		zone = create(zoneName, -50..50)
		zone.with {
			assertEquals 0D, peak
			assertEquals zoneName, name
			assertEquals 0D, fuzzify(-50.1)
			assertEquals 0D, fuzzify(-50)
			assertEquals 0.25D, fuzzify(-37.5)
			assertEquals 0.5D, fuzzify(-25)
			assertEquals 0.75D, fuzzify(-12.5)
			assertEquals 1D, fuzzify(0)
			assertEquals 0.75D, fuzzify(12.5)
			assertEquals 0.5D, fuzzify(25)
			assertEquals 0.25D, fuzzify(37.5)
			assertEquals 0D, fuzzify(50)
			assertEquals 0D, fuzzify(50.1)
		}
	}
	
	void test_asymmetric_unipolar_fuzzify() {
		zone = new RisingFallingFuzzyZone(zoneName, 0D..10D, 2D)
		zone.with {
			assertEquals zoneName, name
			assertEquals 0D, fuzzify(-0.1)
			assertEquals 0D, fuzzify(0)
			assertEquals 0.25D, fuzzify(0.5)
			assertEquals 0.5D, fuzzify(1)
			assertEquals 0.75D, fuzzify(1.5)
			assertEquals 1D, fuzzify(2)
			assertEquals 0.75D, fuzzify(4)
			assertEquals 0.5D, fuzzify(6)
			assertEquals 0.25D, fuzzify(8)
			assertEquals 0D, fuzzify(10)
			assertEquals 0D, fuzzify(10.1)
		}
	}
	
	void test_symmetric_unipolar_fuzzify() {
		zone = new RisingFallingFuzzyZone(zoneName, 0D..10D, 5D)
		zone.with {
			assertEquals zoneName, name
			assertEquals 0D, fuzzify(-0.1)
			assertEquals 0D, fuzzify(0)
			assertEquals 0.25D, fuzzify(1.25)
			assertEquals 0.5D, fuzzify(2.5)
			assertEquals 0.75D, fuzzify(3.75)
			assertEquals 1D, fuzzify(5)
			assertEquals 0.75D, fuzzify(6.25)
			assertEquals 0.5D, fuzzify(7.5)
			assertEquals 0.25D, fuzzify(8.75)
			assertEquals 0D, fuzzify(10)
			assertEquals 0D, fuzzify(10.1)
		}
	}
	
	// FIXME: what about bipolar with peak of 0D ?
	void test_defuzzify() {
		zone.with {
			assertEquals(50D, defuzzify(Fuzzy.MAX))
			assertEquals(37.5D, defuzzify(new Fuzzy(0.75)))
			assertEquals(25D, defuzzify(new Fuzzy(0.5)))
			assertEquals(12.5D, defuzzify(new Fuzzy(0.25)))
			assertEquals(0D, defuzzify(Fuzzy.MIN))
		}
	}
	
	void test_to_string() {
		assertEquals "$zone.name($zone.range.from..$zone.peak..$zone.range.to)", zone.toString()
	}
	
	FuzzyZone create(String name, Range range) {
		new RisingFallingFuzzyZone(name, range)
	}
	
}