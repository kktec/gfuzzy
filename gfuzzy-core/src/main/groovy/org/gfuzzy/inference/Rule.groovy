package org.gfuzzy.inference

import org.gfuzzy.Fuzzy

class Rule {

	static final double DEFAULT_WEIGHT = 1d

	private final String set

	private final String zone

	private final Map<String, String> predicates

	private final double weight

	Rule(set, zone, predicates, weight) {
		this.set = set
		this.zone = zone
		this.predicates = predicates
		this.weight = weight
	}

	Rule(set, zone, predicates) {
		this(set, zone, predicates, DEFAULT_WEIGHT)
	}
	
	Fuzzy process(inputs) {
		Fuzzy result = Fuzzy.MAX
		predicates.each { key, value ->
			def set = inputs[key]
			Fuzzy fuzzy = set[value]
				result &= fuzzy
			}
		new Fuzzy(result * weight)
	}

	String toString() {
		String string = 'IF '
		boolean firstPredicate = true
		predicates.each { key, value ->
			if (!firstPredicate) {
				string += 'AND '
			}
			string += "$key.$value "
			firstPredicate = false
		}
		string += "THEN $set.$zone * $weight"
	}
}
