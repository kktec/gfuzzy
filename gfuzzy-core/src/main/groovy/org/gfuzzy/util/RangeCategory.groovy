package org.gfuzzy.util

class RangeCategory {

	static limit(Range range, v) {
		if(v >= range.to) {
			range.to
		}
		else if(v <= range.from) {
			range.from
		}
		else {
			v
		}
	}
}
