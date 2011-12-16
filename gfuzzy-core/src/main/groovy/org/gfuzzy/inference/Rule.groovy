package org.gfuzzy.inference

import org.gfuzzy.Fuzzy

class Rule {

	static final double DEFAULT_WEIGHT = 1d

	private final String actionSetName

	private final String actionZoneName

	private final Map<String, String> predicates

	private final double weight

	Rule(set, zone, predicates, weight) {
		this.actionSetName = set
		this.actionZoneName = zone
		this.predicates = predicates
		this.weight = weight
	}

	Rule(set, zone, predicates) {
		this(set, zone, predicates, DEFAULT_WEIGHT)
	}
	
	Fuzzy action(conditions) {
		Fuzzy action = Fuzzy.MAX
		predicates.each { setName, zoneName ->
			def condition = conditions[setName]
			Fuzzy fuzzy = condition[zoneName]
				action &= fuzzy
			}
		new Fuzzy(action * weight)
	}
	
	@Override
	String toString() {
		StringBuilder builder = new StringBuilder('IF ')
		boolean firstPredicate = true
		predicates.each { key, value ->
			if (!firstPredicate) {
				builder << 'AND '
			}
			builder << "$key.$value "
			firstPredicate = false
		}
		builder << "THEN $actionSetName.$actionZoneName * $weight"
	}
}
