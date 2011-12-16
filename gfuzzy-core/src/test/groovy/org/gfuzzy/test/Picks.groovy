package org.gfuzzy.test

import static org.gfuzzy.util.DoubleCategory.*

class Picks	implements Comparable<Picks> {

	String scenario, winner, visiting, home
	double confidence, visitingConfidence, homeConfidence

	int compareTo(Picks other) {
		confidence <=> other.confidence
	}

	PickAsStrings asStrings(int digits) {
		new PickAsStrings(scenario:scenario, winner:winner, visiting:visiting, home:home,
				confidence:format(confidence, digits),
				visitingConfidence:format(visitingConfidence, digits),
				homeConfidence:format(homeConfidence, digits)
				)
	}

	String toString() {
		"$winner $confidence   visiting:$visitingConfidence   home:$homeConfidence"
	}
}
