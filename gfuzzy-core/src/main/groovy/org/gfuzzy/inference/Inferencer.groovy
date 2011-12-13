package org.gfuzzy.inference

import org.gfuzzy.Fuzzy
import org.gfuzzy.FuzzySet

class Inferencer {

	final String name

	List<Rule> rules = []

	Inferencer(String name) {
		this.name = name
	}

	void infer(Map<String, FuzzySet> inputs, FuzzySet output) {
		rules.each { rule ->
			output[rule.zone] |= processRule(inputs, rule)
		}
	}

	Inferencer rule(String zone, Map<String, String>predicates) {
		rule(zone, predicates, Fuzzy.MAX)
	}

	Inferencer rule(String zone, Map<String, String>predicates, Fuzzy weight) {
		rules << new Rule(zone, predicates, weight.doubleValue())
		this
	}

	List<String> stringify() {
		List<String> strings = []
		rules.each { rule ->
			def string = 'IF '
			boolean firstPredicate = true
			rule.predicates.each { key, value ->
				if (!firstPredicate) {
					string += 'AND '
				}
				string += "$key.$value "
				firstPredicate = false
			}
			string += "THEN $name.$rule.zone $rule.weight"
			strings << string
		}
		strings
	}


	private Fuzzy processRule(Map<String, FuzzySet> inputs, rule) {
		Fuzzy result = Fuzzy.MAX
		rule.predicates.each { key, value ->
			Fuzzy fuzzy = inputs[key][value]
			if(fuzzy != null) {
				result &= fuzzy
			}
		}
		new Fuzzy(result * rule.weight)
	}
}
