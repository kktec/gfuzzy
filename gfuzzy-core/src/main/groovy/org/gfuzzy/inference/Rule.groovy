package org.gfuzzy.inference

import org.gfuzzy.Fuzzy

class Rule {

	static final double DEFAULT_WEIGHT = 1d

	private final String actionSetName

	private final String actionZoneName

	private final Map<String, String> predicates

	private final double weight

	Rule(actionSetName, actionZoneName, predicates, weight) {
		this.actionSetName = actionSetName
		this.actionZoneName = actionZoneName
		this.predicates = predicates
		this.weight = weight
	}

	Rule(actionSetName, actionZoneName, predicates) {
		this(actionSetName, actionZoneName, predicates, DEFAULT_WEIGHT)
		printf 'called without weight'
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
		builder.toString()
	}
}
