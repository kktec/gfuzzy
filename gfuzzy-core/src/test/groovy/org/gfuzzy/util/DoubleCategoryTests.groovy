package org.gfuzzy.util

import static org.junit.Assert.*

import org.junit.Test

class DoubleCategoryTests {
	
	@Test
	void format_withFractionalDigits() {
		use (DoubleCategory) {
			assertEquals "0.0", 0D.format(1)
			assertEquals "0.00", 0D.format(2)
		}
	}

	@Test
	void format_withOneFractionalDigit() {
		use (DoubleCategory) {
			assertEquals "0.0", 0D.format()
			assertEquals "0.0", 0.format()
		}
	}

	@Test
	void formatDefault() {
		use (DoubleCategory) {
			assertEquals " 0.0", 0D.formatDefault()
			assertEquals " 100.0", 100D.formatDefault()
			assertEquals " -50.0", (-50D).formatDefault()
		}
	}

}
