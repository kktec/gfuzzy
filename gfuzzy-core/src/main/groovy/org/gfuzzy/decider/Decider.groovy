package org.gfuzzy.decider

import org.gfuzzy.FuzzySet
import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.inference.Inferencer

class Decider {
	
	private final Inferencer inferencer

	private final Set<String> alternatives = []

	private final Map<String, FuzzySetDefinition> constraints = [:]

	private final Map<String, Map<String, Number>> alternativeConstraintValues = [:]
	
	Decider(Inferencer inferencer) {
		this.inferencer = inferencer
	}

	void addAlternative(alternative) {
		alternatives << alternative
	}

	void addConstraint(FuzzySetDefinition constraint) {
		constraints[constraint.name] = constraint
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
//			println constraintFuzzySets
			decisions[alternative] = inferencer.infer(constraintFuzzySets)
		}
		decisions
	}

	private Map<String, FuzzySet> constraintFuzzySets(Map<String, Number> constraintValues) {
		Map<String, FuzzySet> constraintFuzzySets = [:]
		constraintValues.each { constraint, value ->
			FuzzySet set = constraints[constraint].fuzzify(value)
			constraintFuzzySets[constraint] = set
		}
		constraintFuzzySets
	}

//	private Map<String, FuzzySet> constraintFuzzySets(Map<String, Number> constraintValues) {
//		Map<String, FuzzySet> constraintFuzzySets = [:]
//		alternatives.each { alternative ->
//			constraints.each { constraint ->
//
//			}
//		}
//		constraintFuzzySets
//	}

//	private FuzzySet constraintFuzzySet(FuzzySetDefinition constraint, Map<String, Number> constraintValues) {
//		FuzzySet set
//		def value = constraintValues[constraint.name]
//		if(value != null) {
//			set = constraints[constraint.name].fuzzify(value)
//		} else {
//			set = constraint.set()
//		}
//	}

}



