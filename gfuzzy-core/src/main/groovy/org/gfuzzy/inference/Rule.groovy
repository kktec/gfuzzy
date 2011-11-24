package org.gfuzzy.inference

import org.gfuzzy.Fuzzy
import org.gfuzzy.FuzzySet

class Rule {

	String zone
	
	Map<String, String> predicates
	
	Rule(String zone, Map<String, String> predicates) {
		this.zone = zone
		this.predicates = predicates
	}

}
