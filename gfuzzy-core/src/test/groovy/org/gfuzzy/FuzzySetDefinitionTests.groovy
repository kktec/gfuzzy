package org.gfuzzy

import static org.gfuzzy.FuzzySetDefinition.*
import static org.gfuzzy.Fuzzy.*

import com.kktec.fuzzy4g.zone.*

/**
 * @author Ken Krebs
 *
 */
class FuzzySetDefinitionTests extends GroovyTestCase {

	FuzzySetDefinition fuzzySetDefinition = createDefinitionForRanges("test", [NL:-100..-50, NS:-100..0, ZE:-50..50, PS:0..100, PL:50..100])

	void test_peak_factory_with_5_zones() {
		assert5Zones createDefinitionForPeaks("test", [NL:-100, NS:-50, ZE:0, PS:50, PL:100])
	}

	void test_peak_factory_with_5_zones_reorders_ascending() {
		assert5Zones createDefinitionForPeaks("test", [PL:100, PS:50, ZE:0, NS:-50, NL:-100])
	}

	void test_peak_factory_with_5_zones_and_duplicate_peak() {
		assertEquals 'duplicate peaks are not allowed',
				shouldFail((IllegalArgumentException)) { createDefinitionForPeaks("test", [PL:100, PS:50, ZE:0, NS:-50, NL:100]) }
	}

	private assert5Zones(FuzzySetDefinition definition) {
		assertEquals "test", definition.name
		assertTrue definition["NL"] instanceof FallingFuzzyZone
		assertTrue definition["NS"] instanceof RisingFallingFuzzyZone
		assertTrue definition["ZE"] instanceof RisingFallingFuzzyZone
		assertTrue definition["PS"] instanceof RisingFallingFuzzyZone
		assertTrue definition["PL"] instanceof RisingFuzzyZone
		assertEquals 5, definition.zones.size
		assertEquals (-100, definition.from)
		assertEquals 100, definition.to
	}

	void test_range_factory_nameRange_invalid() {
		assertEquals 'name cannot be null',
				shouldFail(IllegalArgumentException) { createDefinitionForRanges(null, ["Z":-10..10]) }
		assertEquals 'nameRangeMap cannot be null or empty',
				shouldFail(IllegalArgumentException) { createDefinitionForRanges("test", null) }
		assertEquals 'nameRangeMap cannot be null or empty',
				shouldFail(IllegalArgumentException) { createDefinitionForRanges("test", [:]) }
	}

	void test_peak_factory_namePeak_invalid() {
		assertEquals 'name cannot be null',
				shouldFail(IllegalArgumentException) { createDefinitionForPeaks(null, ["N":-10, "P":10]) }
		assertEquals 'namePeakMap cannot be null or empty',
				shouldFail(IllegalArgumentException) { createDefinitionForPeaks("test", null) }
		assertEquals 'namePeakMap cannot be null or empty',
				shouldFail(IllegalArgumentException) { createDefinitionForPeaks("test", [:]) }
		assertEquals 'namePeakMap must have at least 2 name:Peak',
				shouldFail(IllegalArgumentException) { createDefinitionForPeaks("test", ["Z":0]) }
	}

	void test_range_factory_nameRange_with_1_zone() {
		fuzzySetDefinition = createDefinitionForRanges("test", [ZE:-10..10])
		assertEquals "test", fuzzySetDefinition.name
		assertTrue fuzzySetDefinition["ZE"] instanceof RisingFallingFuzzyZone
		assertEquals 1, fuzzySetDefinition.zones.size
	}

	void test_range_factory_nameRange_with_2_zones() {
		fuzzySetDefinition = createDefinitionForRanges("test", [N:-10..0, P:0..10])
		assertEquals "test", fuzzySetDefinition.name
		assertTrue fuzzySetDefinition["N"] instanceof FallingFuzzyZone
		assertTrue fuzzySetDefinition["P"] instanceof RisingFuzzyZone
		assertEquals 2, fuzzySetDefinition.zones.size
	}

	void test_range_factory_nameRange_with_5_zones() {
		assert5Zones fuzzySetDefinition
	}

	void test_range_factory_nameRange_with_5_zones_reorders_ascending() {
		assert5Zones createDefinitionForRanges("test", [PL:50..100, PS:0..100, ZE:-50..50, NS:-100..0, NL:-100..-50])
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
			def fuzzify = fuzzySetDefinition.fuzzify(value)
			def defuzzify = fuzzySetDefinition.defuzzify(fuzzify)
			assertEquals value, defuzzify, 0.0001d
		}
	}

	void test_find_zone_getAt_with_5_zones() {
		def zones = fuzzySetDefinition.zones
		shouldFail(IllegalArgumentException) { fuzzySetDefinition[""] }
		assertSame zones[0], fuzzySetDefinition["NL"]
		assertSame zones[1], fuzzySetDefinition["NS"]
		assertSame zones[2], fuzzySetDefinition["ZE"]
		assertSame zones[3], fuzzySetDefinition["PS"]
		assertSame zones[4], fuzzySetDefinition["PL"]
	}

	void test_zone_index_with_5_zones() {
		assertEquals 'could not find zone xxx', shouldFail(IllegalArgumentException) { fuzzySetDefinition.zoneIndex("xxx") }
		fuzzySetDefinition.with {
			assertEquals 0, zoneIndex("NL")
			assertEquals 1, zoneIndex("NS")
			assertEquals 2, zoneIndex("ZE")
			assertEquals 3, zoneIndex("PS")
			assertEquals 4, zoneIndex("PL")
		}
	}

	void test_fuzzies_with_5_zones() {
		fuzzySetDefinition.fuzzies().with {
			assertEquals MIN, NL
			assertEquals MIN, NS
			assertEquals MIN, ZE
			assertEquals MIN, PS
			assertEquals MIN, PL
		}
	}

	void test_to_string() {
		assertEquals fuzzySetDefinition.zones.toString(), fuzzySetDefinition.toString()
	}

	protected void assertFuzzify(Number value, List expected) {
		def fuzzies = fuzzySetDefinition.fuzzify(value)
		fuzzies.with {
			assertEquals expected[0] , NL
			assertEquals expected[1] , NS
			assertEquals expected[2] , ZE
			assertEquals expected[3] , PS
			assertEquals expected[4] , PL
		}
		"fuzzify($value) = $fuzzies"
	}

	protected void assertDefuzzify(Number expected, List values) {
		def fuzzies = expectedFuzzies(values)
		Number defuzzify = fuzzySetDefinition.defuzzify(fuzzies)
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