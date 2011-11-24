package org.gfuzzy

import java.util.Map;

class FuzzySet {

	Map<String, Fuzzy> fuzzies
	
	FuzzySet(Map<String, Fuzzy> fuzzies) {
		this.fuzzies = fuzzies
	}
	
	Fuzzy getAt(String zoneName) {
		fuzzies[zoneName]
	}
	
	Fuzzy putAt(String zoneName, Fuzzy fuzzy) {
		fuzzies.put(zoneName, fuzzy)
	}
	
	@Override
	String toString() {
		String s = ''
		fuzzies.each { key, value ->
			s << "$key:$value "
		}
	}
	
	
}
