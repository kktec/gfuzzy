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
		rules << new Rule(zone, predicates)
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
			string += "THEN $name.$rule.zone"
			strings << string
		}
		strings
	}
	
	private Fuzzy processRule(Map inputs, rule) {
		Fuzzy result = Fuzzy.MAX
		rule.predicates.each { key, value ->
			result &= inputs[key][value]
		}
		result
	}
}
