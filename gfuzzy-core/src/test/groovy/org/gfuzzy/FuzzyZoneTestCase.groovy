package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
abstract class FuzzyZoneTestCase extends GroovyTestCase {
	
	def zoneName = "zone"
	
	FuzzyZone zone
	
	abstract FuzzyZone create(String name, Range range)
	
	@Override
	void setUp() {
		zone = create(zoneName, -50..150)
	}
	
	void test_constructor() {
		zone = create(zoneName, 100..200)
		assertEquals zoneName, zone.name
		assertEquals 100D, zone.from
		assertEquals 200D, zone.to
		println shouldFail(IllegalArgumentException) {create(null, 100..200)}
		println shouldFail(IllegalArgumentException) {create(zoneName, null)}
	}
	
	void test_mid() {
		zone = create(zoneName, -100..100)
		assertEquals 0, zone.mid()
		zone = create(zoneName, -100..300)
		assertEquals 100, zone.mid()
	}
	
	void test_contains() {
		zone = create(zoneName, 100..200)
		zone.with {
			assertFalse contains(99.9)
			assertTrue contains(100)
			assertTrue contains(150D)
			assertTrue contains(200)
			assertFalse contains(200.1)
		}
	}
	
	void test_to_string() {
		assertEquals "$zone.name($zone.range.from..$zone.range.to)", zone.toString()
	}
	
}
