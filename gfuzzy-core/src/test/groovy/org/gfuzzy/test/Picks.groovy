package org.gfuzzy.test

import org.gfuzzy.util.DoubleCategory

class Picks	implements Comparable<Picks> {

	String scenario, winner, visiting, home
	double confidence, visitingConfidence, homeConfidence

	int compareTo(Picks other) {
		confidence <=> other.confidence
	}

	PickAsStrings asStrings(int digits) {
		PickAsStrings asStrings = new PickAsStrings(scenario:scenario, winner:winner, visiting:visiting, home:home,
			confidence:DoubleCategory.format(confidence, digits),
			visitingConfidence:DoubleCategory.format(visitingConfidence, digits),
			homeConfidence:DoubleCategory.format(homeConfidence, digits)
		)
	}

	String toString() {
		"$winner $confidence" + "   visiting:$visitingConfidence   home:$homeConfidence"
	}
}
