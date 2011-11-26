package org.gfuzzy.util

import java.text.NumberFormat

class DoubleCategory {
	
	static final PATTERN = ' ##0.0; -##0.0'
	
	static String format(v) {
		format(v, 1)
	}
	
	static String format(v, fractionalDigits) {
		def f = NumberFormat.instance
		f.decimalSeparatorAlwaysShown = true
		f.minimumIntegerDigits = 1
		f.minimumFractionDigits = fractionalDigits
		f.maximumFractionDigits = fractionalDigits
		f.format(v)
	}
	
	static String formatDefault(v) {
		def f = NumberFormat.instance
		f.applyPattern PATTERN
		f.format(v)
	}

}
