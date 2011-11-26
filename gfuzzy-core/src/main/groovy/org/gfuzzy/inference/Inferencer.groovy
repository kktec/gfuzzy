package org.gfuzzy.inference

import org.gfuzzy.Fuzzy
import org.gfuzzy.FuzzySet

class Inferencer {
	
	List<Rule> rules = []
	
	void infer(Map<String, FuzzySet> inputs, FuzzySet output) {
		rules.each { rule ->
			output[rule.zone] |= processRule(inputs, rule)
		}
	}
	
	Inferencer rule(String zone, Map<String, String>predicates) {
		rules << new Rule(zone, predicates)
		this
	}
	
	private Fuzzy processRule(Map inputs, rule) {
		Fuzzy result = Fuzzy.MAX
		rule.predicates.each { key, value ->
			result &= inputs[key][value]
		}
		result
	}
}
