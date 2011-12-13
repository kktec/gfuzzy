package org.gfuzzy.decider

import org.gfuzzy.FuzzySet
import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.inference.Inferencer
import org.gfuzzy.inference.Rule

class Decider {

	private FuzzySetDefinition goal

	private Set<String> alternatives = []

	private Map<String, FuzzySetDefinition> constraints = [:]

	private Map<String, Map<String, Number>> alternativeConstraintValues = [:]

	private List<Rule> rules = []

	void addAlternative(alternative) {
		alternatives << alternative
	}

	void addConstraint(FuzzySetDefinition constraint) {
		constraints[constraint.name] = constraint
	}

	void addRule(Rule rule) {
		rules << rule
	}

	void addConstraintValueForAlternative(String constraint, Number value, String alternative) {
		def values = alternativeConstraintValues[alternative]
		if(values == null) {
			values = [:]
		}
		values[constraint] = value
		alternativeConstraintValues[alternative] = values
	}

	Map<String, Number> decide() {
		Map<String, Number> decisions = [:]
		alternatives.each { alternative ->
			Map<String, Number> constraintValues = alternativeConstraintValues[alternative]
			Map<String, FuzzySet> constraintFuzzySets = constraintFuzzySets(constraintValues)
			double decision = infer(alternative, constraintFuzzySets)
			decisions[alternative] = decision
		}
		decisions
	}

	private double infer(alternative, constraintFuzzySets) {
		Inferencer inferencer = new Inferencer(alternative)
		inferencer.rules = rules
		FuzzySet goalSet = goal.set()
		inferencer.infer(constraintFuzzySets, goalSet)
		goal.defuzzify(goalSet)
	}

	private Map<String, FuzzySet> constraintFuzzySets(Map constraintValues) {
		Map<String, FuzzySet> constraintFuzzySets = [:]
		constraintValues.each { constraint, value ->
			FuzzySet set = constraints[constraint].fuzzify(value)
			constraintFuzzySets[constraint] = set
		}
		constraintFuzzySets
	}
}



