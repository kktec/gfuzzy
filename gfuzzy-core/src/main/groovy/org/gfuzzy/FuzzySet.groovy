package org.gfuzzy

/**
 * @author Ken Krebs
 *
 */
class FuzzySet {

	def zones = []

	String name

	private FuzzySet(String name) {
		if(!name) {
			throw new IllegalArgumentException("name cannot be null")
		}
		this.name = name
	}

	static FuzzySet createFuzzySetForPeaks(String name, Map namePeakMap) {
		FuzzySet fuzzySet = new FuzzySet(name)
		if(!namePeakMap) {
			throw new IllegalArgumentException("namePeakMap cannot be null")
		}

		if(namePeakMap.size() < 2) {
			throw new IllegalArgumentException("namePeakMap must have at least 2 name:Peak")
		}

		def peakNameMap = [:]
		namePeakMap.each { from, to ->
			peakNameMap[to] = from
		}
		def peaks = peakNameMap.keySet().toArray()

		def zones = fuzzySet.zones
		int lastIndex = peaks.length - 1
		for (int index in 0..lastIndex) {
			String rangeName = peakNameMap[peaks[index]]
			if(index == 0) {
				Range range = new ObjectRange(peaks[0], peaks[1])
				zones << new FallingFuzzyZone(rangeName, range)
			}
			else if (index == lastIndex) {
				Range range = new ObjectRange(peaks[index - 1], peaks[index])
				zones << new RisingFuzzyZone(rangeName, range)
			}
			else {
				Range range = new ObjectRange(peaks[index - 1],  peaks[index + 1])
				zones << new RisingFallingFuzzyZone(rangeName, range)
			}
		}
		fuzzySet
	}

	static FuzzySet createFuzzySetForRanges(String name, Map nameRangeMap) {
		FuzzySet fuzzySet = new FuzzySet(name)

		if(!nameRangeMap) {
			throw new IllegalArgumentException("nameRangeMap cannot be null or empty")
		}

		if(nameRangeMap.size() == 1) {
			nameRangeMap.each { from, to ->
				fuzzySet.zones << new RisingFallingFuzzyZone(from, to)
			}
		}
		else {
			nameRangeMap.each { from, to ->
				def zones = fuzzySet.zones
				if(zones.size == 0) {
					zones << new FallingFuzzyZone(from, to)
				}
				else if (zones.size == nameRangeMap.size() - 1){
					zones << new RisingFuzzyZone(from, to)
				}
				else {
					zones << new RisingFallingFuzzyZone(from, to)
				}
			}
		}
		fuzzySet
	}
	
	Number getFrom() {
		zones[0].range.from
	}

	Number getTo() {
		zones[-1].range.to
	}

	Map fuzzify(Number value) {
		def fuzzies = [:]
		zones.each { zone -> fuzzies[zone.name] = zone.fuzzify(value) }
		fuzzies
	}

	Map fuzzies() {
		def fuzzies = [:]
		zones.each { zone -> fuzzies[zone.name] = Fuzzy.MIN }
		fuzzies
	}

	Number defuzzify(def fuzzies) {
		double dividend = 0d
		double divisor = 0d
		zones.each { zone ->
			dividend += zone.defuzzify(fuzzies[zone.name])
			divisor += fuzzies[zone.name]
		}
		divisor == 0d ? 0d : dividend / divisor
	}

	FuzzyZone getAt(String zoneName) {
		for(zone in zones) {
			if(zone.name == zoneName) {
				return zone
			}
		}
		throw new IllegalArgumentException("could not find zone ${name}")
	}

	int zoneIndex(String zoneName) {
		for(index in 0..<zones.size()) {
			if(zones[index].name == zoneName) {
				return index
			}
		}
		throw new IllegalArgumentException("could not find zone ${zoneName}")
	}

	@Override
	String toString() {
		zones.toString()
	}

}