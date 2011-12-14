package org.gfuzzy.specs

import org.concordion.integration.junit4.ConcordionRunner
import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.util.DoubleCategory
import org.junit.runner.RunWith

@RunWith(ConcordionRunner)
class FuzzySetWith2PeaksTest {

	def names = []

	def peaks = []

	FuzzySetDefinition definition

	def fuzzy

	void addZoneName(name) {
		names << name
	}

	void addZonePeak(peak) {
		peaks << Integer.parseInt(peak)
	}

	FuzzySetDefinition create(name) {
		def peakMap = [:]
		for (i in 0..names.size() - 1) {
			peakMap[names[i]] = peaks[i]
		}
		definition = FuzzySetDefinition.definitionForPeaks(name, peakMap)
	}

	Result fuzzify(value) {
		fuzzy = definition.fuzzify(Double.parseDouble(value))
		new Result()
	}

	class Result {
		String fuzzyForZone(zoneName) {
			DoubleCategory.format fuzzy[zoneName]
		}
	}
}


