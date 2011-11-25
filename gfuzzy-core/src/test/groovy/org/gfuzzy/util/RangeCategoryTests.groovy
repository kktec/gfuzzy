package org.gfuzzy.util


class RangeCategoryTests extends GroovyTestCase {

	void test_limitTo_int() {
		Range r = -10..10
		use(RangeCategory) {
			assertEquals 0, r.limit(0)
			assertEquals 10, r.limit(11)
			assertEquals(-10, r.limit(-11))
		}
	}

	void test_limitTo_double() {
		Range r = -10D..10D
		use(RangeCategory) {
			assertEquals 0D, r.limit(0.0)
			assertEquals 10D, r.limit(10.1)
			assertEquals(-10D, r.limit(-10.1))
		}
	}

	void test_limitTo_wrong_type() {
		Range r = -10..10
		use(RangeCategory) {
			shouldFail { assertEquals 0, r.limit(new Date()) }
		}
	}
}

