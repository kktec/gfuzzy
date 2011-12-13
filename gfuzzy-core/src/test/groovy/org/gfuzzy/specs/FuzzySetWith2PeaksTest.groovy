package org.gfuzzy.specs

import org.concordion.integration.junit4.ConcordionRunner
import org.gfuzzy.FuzzySet
import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.util.DoubleCategory
import org.junit.runner.RunWith

@RunWith(ConcordionRunner)
class FuzzySetWith2PeaksTest {

	String name

	def names = []

	def peaks = []

	FuzzySetDefinition definition

	FuzzySet fuzzy

	void addZoneName(String name) {
		names << name
	}

	void addZonePeak(String peak) {
		peaks << Integer.parseInt(peak)
	}

	FuzzySetDefinition create(name) {
		Map peakMap = [:]
		for (int i in 0..names.size() - 1) {
			peakMap[names[i]] = peaks[i]
		}
		definition = FuzzySetDefinition.definitionForPeaks(name, peakMap)
	}

	Result fuzzify(String value) {
		fuzzy = definition.fuzzify(Double.parseDouble(value))
		new Result()
	}

	class Result {
		String fuzzyForZone(zoneName) {
			DoubleCategory.format(fuzzy[zoneName])
		}
	}
}


