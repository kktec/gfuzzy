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

	/**
	 * Creates a new FuzzySet using the specified Map of zone names to peak values. 
	 * 
	 * @param name of the FuzzySet
	 * @param namePeakMap Map<String, Number> to be used to create the FuzzyZones
	 * @return FuzzySet
	 */
	static FuzzySet createFuzzySetForPeaks(String name, Map namePeakMap) {
		FuzzySet fuzzySet = new FuzzySet(name)
		if(!namePeakMap) {
			throw new IllegalArgumentException("namePeakMap cannot be null or empty")
		}

		if(namePeakMap.size() < 2) {
			throw new IllegalArgumentException("namePeakMap must have at least 2 name:Peak")
		}

		def peakNameMap = [:]
		namePeakMap.each { from, to ->
			peakNameMap[to] = from
		}
		if(peakNameMap.size() != namePeakMap.size()) {
			throw new IllegalArgumentException('duplicate peaks are not allowed')
		}

		def peaks = peakNameMap.keySet().toList()
		peaks.sort()

		def zones = fuzzySet.zones
		int lastIndex = peaks.size() - 1
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

	/**
	 * Creates a new FuzzySet using the specified Map of zone names to ranges. 
	 * 
	 * @param name of the FuzzySet
	 * @param nameRangeMap Map<String, Range> to be used to create the FuzzyZones
	 * @return FuzzySet
	 */
	static FuzzySet createFuzzySetForRanges(String name, Map nameRangeMap) {
		FuzzySet fuzzySet = new FuzzySet(name)

		if(!nameRangeMap) {
			throw new IllegalArgumentException("nameRangeMap cannot be null or empty")
		}

		def zones = []
		if(nameRangeMap.size() == 1) {
			nameRangeMap.each { from, to ->
				zones << new RisingFallingFuzzyZone(from, to)
			}
		}
		else {
			nameRangeMap.each { from, to ->
				zones << new RisingFallingFuzzyZone(from, to)
			}
			zones.sort()
			zones[0] = new FallingFuzzyZone(zones[0].name, zones[0].from..zones[0].to)
			int last = zones.size() - 1
			zones[last] = new RisingFuzzyZone(zones[last].name, zones[last].from..zones[last].to)
		}
		
		fuzzySet.zones = zones
		fuzzySet
	}

	Number getFrom() {
		zones[0].from
	}

	Number getTo() {
		zones[-1].to
	}

	Map fuzzify(Number value) {
		def fuzzies = [:]
		zones.each { zone ->
			fuzzies[zone.name] = zone.fuzzify(value)
		}
		fuzzies
	}

	Map fuzzies() {
		def fuzzies = [:]
		zones.each { zone ->
			fuzzies[zone.name] = Fuzzy.MIN
		}
		fuzzies
	}

	Number defuzzify(def fuzzies) {
		final double ZERO = 0D
		double dividend = ZERO
		double divisor = ZERO
		zones.each { zone ->
			dividend += zone.defuzzify(fuzzies[zone.name])
			divisor += fuzzies[zone.name]
		}
		divisor == ZERO ? ZERO : dividend / divisor
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