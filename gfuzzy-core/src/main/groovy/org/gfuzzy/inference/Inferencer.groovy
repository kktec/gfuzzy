package org.gfuzzy.inference

import org.gfuzzy.FuzzySet
import org.gfuzzy.FuzzySetDefinition

class Inferencer {

	final FuzzySetDefinition output
	
	List<Rule> rules = []

	Inferencer(FuzzySetDefinition output) {
		this.output = output
	}

	double infer(Map<String, FuzzySet> inputs) {
		FuzzySet out = output.set()
		rules.each { rule ->
			out[rule.zone] |= rule.process(inputs)
		}
		output.defuzzify(out)
	}

	Inferencer rule(String zone, Map<String, String> predicates) {
		rule(zone, predicates, Rule.DEFAULT_WEIGHT)
	}

	Inferencer rule(String zone, Map<String, String> predicates, double weight) {
		rules << new Rule(output.name, zone, predicates, weight)
		this
	}

	List<String> stringifyRules() {
		rules*.toString()
	}


}
