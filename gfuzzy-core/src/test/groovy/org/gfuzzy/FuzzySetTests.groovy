package org.gfuzzy

import static org.gfuzzy.FuzzySet.*
import static org.gfuzzy.Fuzzy.*

import com.kktec.fuzzy4g.zone.*

/**
 * @author Ken Krebs
 *
 */
class FuzzySetTests extends GroovyTestCase {
	
	FuzzySet fuzzySet = createFuzzySetForRanges("test", [NL:-100..-50, NS:-100..0, ZE:-50..50, PS:0..100, PL:50..100])
	
	void test_peak_factory_with_5_zones() {
		FuzzySet fuzzySet = createFuzzySetForPeaks("test", [NL:-100, NS:-50, ZE:0, PS:50, PL:100])
		assertEquals "test", fuzzySet.name
		assertTrue fuzzySet["NL"] instanceof FallingFuzzyZone
		assertTrue fuzzySet["NS"] instanceof RisingFallingFuzzyZone
		assertTrue fuzzySet["ZE"] instanceof RisingFallingFuzzyZone
		assertTrue fuzzySet["PS"] instanceof RisingFallingFuzzyZone
		assertTrue fuzzySet["PL"] instanceof RisingFuzzyZone
		assertEquals 5, fuzzySet.zones.size
	}
	
	void test_range_factory_nameRange_invalid() {
		shouldFail(IllegalArgumentException) { createFuzzySetForRanges(null, ["":-10..10]) }
		shouldFail(IllegalArgumentException) { createFuzzySetForRanges("test", null) }
		shouldFail(IllegalArgumentException) { createFuzzySetForRanges("test", [:]) }
	}
	
	void test_peak_factory_namePeak_invalid() {
		shouldFail(IllegalArgumentException) { createFuzzySetForPeaks(null, ["":0]) }
		shouldFail(IllegalArgumentException) { createFuzzySetForPeaks("test", null) }
		shouldFail(IllegalArgumentException) { createFuzzySetForPeaks("test", [:]) }
		shouldFail(IllegalArgumentException) { createFuzzySetForPeaks("test", ["":0]) }
	}
	
	void test_range_factory_nameRange_with_1_zone() {
		fuzzySet = createFuzzySetForRanges("test", [ZE:-10..10])
		assertEquals "test", fuzzySet.name
		assertTrue fuzzySet["ZE"] instanceof RisingFallingFuzzyZone
		assertEquals 1, fuzzySet.zones.size
	}
	
	void test_range_factory_nameRange_with_2_zones() {
		fuzzySet = createFuzzySetForRanges("test", [N:-10..0, P:0..10])
		assertEquals "test", fuzzySet.name
		assertTrue fuzzySet["N"] instanceof FallingFuzzyZone
		assertTrue fuzzySet["P"] instanceof RisingFuzzyZone
		assertEquals 2, fuzzySet.zones.size
	}
	
	void test_range_factory_nameRange_with_5_zones() {
		assertEquals "test", fuzzySet.name
		assertTrue fuzzySet["NL"] instanceof FallingFuzzyZone
		assertTrue fuzzySet["NS"] instanceof RisingFallingFuzzyZone
		assertTrue fuzzySet["ZE"] instanceof RisingFallingFuzzyZone
		assertTrue fuzzySet["PS"] instanceof RisingFallingFuzzyZone
		assertTrue fuzzySet["PL"] instanceof RisingFuzzyZone
		assertEquals 5, fuzzySet.zones.size
		assertEquals (-100, fuzzySet.from)
		assertEquals 100, fuzzySet.to
	}
	
	void test_fuzzify_with_5_zones() {
		assertFuzzify(-100, [1d, 0d, 0d, 0d, 0d])
		assertFuzzify(-62.5, [0.25d, 0.75d, 0d, 0d, 0d])
		assertFuzzify(-50, [0d, 1d, 0d, 0d, 0d])
		assertFuzzify(-12.5, [0d, 0.25d, 0.75d, 0d, 0d])
		assertFuzzify(0, [0d, 0d, 1d, 0d, 0d])
		assertFuzzify(12.5, [0d, 0d, 0.75d, 0.25d, 0d])
		assertFuzzify(25, [0d, 0d, 0.5d, 0.5d, 0d])
		assertFuzzify(50, [0d, 0d, 0d, 1d, 0d])
		assertFuzzify(75, [0d, 0d, 0d, 0.5d, 0.5d])
		assertFuzzify(100, [0d, 0d, 0d, 0d, 1d])
	}
	
	void test_defuzzify_with_5_zones() {
		assertDefuzzify(-100, [1d, 0d, 0d, 0d, 0d])
		assertDefuzzify(-62.5, [0.25d, 0.75d, 0d, 0d, 0d])
		assertDefuzzify(-50, [0d, 1d, 0d, 0d, 0d])
		assertDefuzzify(-12.5, [0d, 0.25d, 0.75d, 0d, 0d])
		assertDefuzzify(0, [0d, 0d, 1d, 0d, 0d])
		assertDefuzzify(12.5, [0d, 0d, 0.75d, 0.25d, 0d])
		assertDefuzzify(25, [0d, 0d, 0.5d, 0.5d, 0d])
		assertDefuzzify(50, [0d, 0d, 0d, 1d, 0d])
		assertDefuzzify(75, [0d, 0d, 0d, 0.5d, 0.5d])
		assertDefuzzify(100, [0d, 0d, 0d, 0d, 1d])
	}
	
	void test_defuzzify_with_all_zero_zones() {
		assertDefuzzify(0, [0d, 0d, 0d, 0d, 0d])
	}
	
	void test_fuzzify_defuzzify_with_5_zones() {
		for(value in (-100d..100d).step(10)) {
			def fuzzify = fuzzySet.fuzzify(value)
			def defuzzify = fuzzySet.defuzzify(fuzzify)
			assertEquals value, defuzzify, 0.0001d
		}
	}
	
	void test_find_zone_getAt_with_5_zones() {
		def zones = fuzzySet.zones
		shouldFail(IllegalArgumentException) { fuzzySet[""] }
		assertSame zones[0], fuzzySet["NL"]
		assertSame zones[1], fuzzySet["NS"]
		assertSame zones[2], fuzzySet["ZE"]
		assertSame zones[3], fuzzySet["PS"]
		assertSame zones[4], fuzzySet["PL"]
	}
	
	void test_zone_index_with_5_zones() {
		shouldFail(IllegalArgumentException) { fuzzySet.zoneIndex("") }
		fuzzySet.with {
			assertEquals 0, zoneIndex("NL")
			assertEquals 1, zoneIndex("NS")
			assertEquals 2, zoneIndex("ZE")
			assertEquals 3, zoneIndex("PS")
			assertEquals 4, zoneIndex("PL")
		}
	}
	
	void test_fuzzies_with_5_zones() {
		fuzzySet.fuzzies().with {
			assertEquals MIN, NL
			assertEquals MIN, NS
			assertEquals MIN, ZE
			assertEquals MIN, PS
			assertEquals MIN, PL
		}
	}
	
	void test_to_string() {
		assertEquals fuzzySet.zones.toString(), fuzzySet.toString()
	}
	
	void assertFuzzify(Number value, List expected) {
		def fuzzies = fuzzySet.fuzzify(value)
		fuzzies.with {
			assertEquals expected[0] , NL
			assertEquals expected[1] , NS
			assertEquals expected[2] , ZE
			assertEquals expected[3] , PS
			assertEquals expected[4] , PL
		}
		"fuzzify($value) = $fuzzies"
	}
	
	void assertDefuzzify(Number expected, List values) {
		def fuzzies = expectedFuzzies(values)
		Number defuzzify = fuzzySet.defuzzify(fuzzies)
		"defuzzify($values) = $defuzzify"
		assertEquals(expected , defuzzify)
	}
	
	private Map expectedFuzzies(List values) {
		def fuzzies = [:]
		fuzzies.with {
			NL = new Fuzzy(values[0])
			NS = new Fuzzy(values[1])
			ZE = new Fuzzy(values[2])
			PS = new Fuzzy(values[3])
			PL = new Fuzzy(values[4])
		}
		fuzzies
	}
	
}