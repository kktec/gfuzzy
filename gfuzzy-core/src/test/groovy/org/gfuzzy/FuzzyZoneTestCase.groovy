package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
abstract class FuzzyZoneTestCase extends GroovyTestCase {
	
	String zoneName = "zone"
	
	Range range = -50..150
	
	FuzzyZone zone
	
	abstract protected FuzzyZone create(String name, Range range)
	
	@Override
	void setUp() {
		zone = create(zoneName, range)
	}
	
	void test_constructor() {
		assertEquals zoneName, zone.name
		assertEquals (-50D, zone.from)
		assertEquals 150D, zone.to
		shouldFail(IllegalArgumentException) {create(null, range)}
		shouldFail(IllegalArgumentException) {create(zoneName, null)}
	}
	
	void test_mid() {
		assertEquals 50D, zone.mid()
		zone = create(zoneName, -100..100)
		assertEquals 0D, zone.mid()
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
		assertEquals "$zone.name($zone.from..$zone.to)", zone.toString()
	}
	
	void test_equals() {
		assertEquals zone, create(zoneName, range) 
		assertFalse zone == new Object()
		assertFalse zone == create("x", range)
		assertFalse zone == create(zoneName, 0..150)
		assertFalse zone == create(zoneName, -50..100)
	}
	
	void test_hashCode() {
		assertEquals 3744684, zone.hashCode()
	}
	
	void test_comparable() {
		assertEquals 0, zone <=> create(zoneName, range)
		assertEquals 0, FallingFuzzyZoneTests.createFallingFuzzyZone(zoneName, range) <=> FallingFuzzyZoneTests.createFallingFuzzyZone(zoneName, range)
		assertTrue zone > create(zoneName, -51..150) 
		assertTrue FallingFuzzyZoneTests.createFallingFuzzyZone(zoneName, range) > FallingFuzzyZoneTests.createFallingFuzzyZone(zoneName, -51..150)
		assertTrue zone > create(zoneName, -50..149) 
		assertTrue zone < create(zoneName, -49..150) 
		assertTrue zone < create(zoneName, -50..151)
	}
	
}
