package org.gfuzzy.util


class DoubleCategoryTests extends GroovyTestCase {
	
	void test_defaultDoubleFomat() {
		use (DoubleCategory) {
			assertEquals "0.0", 0D.format()
			assertEquals "0.0", 0D.format(1)
			assertEquals "0.00", 0D.format(2)
			assertEquals "0.0", 0.format()
			assertEquals " 0.0", 0D.formatDefault()
			assertEquals " 100.0", 100D.formatDefault()
			assertEquals " -50.0", (-50D).formatDefault()
		}
	}

}
