package org.gfuzzy

import static org.gfuzzy.FuzzySetDefinition.*
import static org.gfuzzy.Fuzzy.*

import com.kktec.fuzzy4g.zone.*

/**
 * @author Ken Krebs
 *
 */
class FuzzySetDefinitionTests extends GroovyTestCase {

	FuzzySetDefinition fuzzySetDefinition =
	definitionForRanges("test", [NL:-100..-50, NS:-100..0, ZE:-50..50, PS:0..100, PL:50..100])

	void test_range() {
		assert -100 == fuzzySetDefinition.range.from 
		assert 100 == fuzzySetDefinition.range.to 
	}
	
	void test_peak_factory_with_5_zones() {
		assert5Zones definitionForPeaks("test", [NL:-100, NS:-50, ZE:0, PS:50, PL:100])
	}

	void test_peak_factory_with_5_zones_reorders_ascending() {
		assert5Zones definitionForPeaks("test", [PL:100, PS:50, ZE:0, NS:-50, NL:-100])
	}

	void test_peak_factory_with_5_zones_and_duplicate_peak() {
		assert 'duplicate peaks are not allowed' ==
			shouldFail((IllegalArgumentException)) { definitionForPeaks("test", [PL:100, PS:50, ZE:0, NS:-50, NL:100]) }
	}

	private assert5Zones(FuzzySetDefinition definition) {
		assert "test" == definition.name
		assert definition["NL"] instanceof FallingFuzzyZone
		assert definition["NS"] instanceof RisingFallingFuzzyZone
		assert definition["ZE"] instanceof RisingFallingFuzzyZone
		assert definition["PS"] instanceof RisingFallingFuzzyZone
		assert definition["PL"] instanceof RisingFuzzyZone
		assert 5 == definition.zones.size
		assert -100 == definition.from
		assert 100 == definition.to
	}

	void test_range_factory_nameRange_invalid() {
		assert 'name cannot be null' ==
			shouldFail(IllegalArgumentException) { definitionForRanges(null, ["Z":-10..10]) }
		assert 'nameRangeMap cannot be null or empty' ==
			shouldFail(IllegalArgumentException) { definitionForRanges("test", null) }
		assert 'nameRangeMap cannot be null or empty' ==
			shouldFail(IllegalArgumentException) { definitionForRanges("test", [:]) }
	}

	void test_peak_factory_namePeak_invalid() {
		assert 'name cannot be null' ==
			shouldFail(IllegalArgumentException) { definitionForPeaks(null, ["N":-10, "P":10]) }
		assert 'namePeakMap cannot be null or empty' ==
			shouldFail(IllegalArgumentException) { definitionForPeaks("test", null) }
		assert 'namePeakMap cannot be null or empty' ==
			shouldFail(IllegalArgumentException) { definitionForPeaks("test", [:]) }
		assert 'namePeakMap must have at least 2 name:Peak' ==
			shouldFail(IllegalArgumentException) { definitionForPeaks("test", ["Z":0]) }
	}

	void test_range_factory_nameRange_with_1_zone() {
		fuzzySetDefinition = definitionForRanges("test", [ZE:-10..10])
		assert "test", fuzzySetDefinition.name
		assert fuzzySetDefinition["ZE"] instanceof RisingFallingFuzzyZone
		assert 1 == fuzzySetDefinition.zones.size
	}

	void test_range_factory_nameRange_with_2_zones() {
		fuzzySetDefinition = definitionForRanges("test", [N:-10..0, P:0..10])
		assert "test" == fuzzySetDefinition.name
		assert fuzzySetDefinition["N"] instanceof FallingFuzzyZone
		assert fuzzySetDefinition["P"] instanceof RisingFuzzyZone
		assert 2 == fuzzySetDefinition.zones.size
	}

	void test_range_factory_nameRange_with_5_zones() {
		assert5Zones fuzzySetDefinition
	}

	void test_range_factory_nameRange_with_5_zones_reorders_ascending() {
		assert5Zones definitionForRanges("test", [PL:50..100, PS:0..100, ZE:-50..50, NS:-100..0, NL:-100..-50])
	}

	void test_fuzzify_with_5_zones() {
		assertFuzzify(-100, [1, 0, 0, 0, 0])
		assertFuzzify(-62.5, [0.25, 0.75, 0, 0, 0])
		assertFuzzify(-50, [0, 1, 0, 0, 0])
		assertFuzzify(-12.5, [0, 0.25, 0.75, 0, 0])
		assertFuzzify(0, [0, 0, 1, 0, 0])
		assertFuzzify(12.5, [0, 0, 0.75, 0.25, 0])
		assertFuzzify(25, [0, 0, 0.5, 0.5, 0])
		assertFuzzify(50, [0, 0, 0, 1, 0])
		assertFuzzify(75, [0, 0, 0, 0.5, 0.5])
		assertFuzzify(100, [0, 0, 0, 0, 1])
	}

	void test_defuzzify_with_5_zones() {
		assertDefuzzify(-100, [1, 0, 0, 0, 0])
		assertDefuzzify(-62.5, [0.25, 0.75, 0, 0, 0])
		assertDefuzzify(-50, [0, 1, 0, 0, 0])
		assertDefuzzify(-12.5, [0, 0.25, 0.75, 0, 0])
		assertDefuzzify(0, [0, 0, 1, 0, 0])
		assertDefuzzify(12.5, [0, 0, 0.75, 0.25, 0])
		assertDefuzzify(25, [0, 0, 0.5, 0.5, 0])
		assertDefuzzify(50, [0, 0, 0, 1, 0])
		assertDefuzzify(75, [0, 0, 0, 0.5, 0.5])
		assertDefuzzify(100, [0, 0, 0, 0, 1])
	}

	void test_defuzzify_with_all_zero_zones() {
		assertDefuzzify(0, [0, 0, 0, 0, 0])
	}

	void test_fuzzify_defuzzify_with_5_zones() {
		for(value in (-100..100).step(10)) {
			FuzzySet set = fuzzySetDefinition.fuzzify(value)
			double defuzzify = fuzzySetDefinition.defuzzify(set)
			assertEquals value, defuzzify, 0.0001
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
			assert 0 == zoneIndex("NL")
			assert 1 == zoneIndex("NS")
			assert 2 == zoneIndex("ZE")
			assert 3 == zoneIndex("PS")
			assert 4 == zoneIndex("PL")
		}
	}

	void test_fuzzies_with_5_zones() {
		def set = fuzzySetDefinition.set()
		assert MIN == set['NL']
		assert MIN == set['NS']
		assert MIN == set['ZE']
		assert MIN == set['PS']
		assert MIN == set['PL']
	}

	void test_to_string() {
		fuzzySetDefinition.with {
			assertEquals "$name:$zones", it.toString()
		}
	}

	protected void assertFuzzify(Number value, List expected) {
		def set = fuzzySetDefinition.fuzzify(value)
		assert expected[0] == set['NL']
		assert expected[1] == set['NS']
		assert expected[2] == set['ZE']
		assert expected[3] == set['PS']
		assert expected[4] == set['PL']
	}

	protected void assertDefuzzify(Number expected, List values) {
		def fuzzies = expectedFuzzies(values)
		Number defuzzify = fuzzySetDefinition.defuzzify(fuzzies)
		assert expected == defuzzify
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