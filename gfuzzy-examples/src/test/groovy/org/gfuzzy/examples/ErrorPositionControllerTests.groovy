package org.gfuzzy.examples

import static org.gfuzzy.FuzzySetDefinition.*

import org.gfuzzy.*
import org.gfuzzy.inference.Inferencer
import org.gfuzzy.zone.*
import org.junit.Before
import org.junit.Test

/**
 * @author Ken Krebs
 *
 */
class ErrorPositionControllerTests {
	
	ErrorController controller = new ErrorController()

	@Before
	void setUp() {
		controller.with {
			outputRange = 0D..100D
			positioner = true
			setpoint = 50D
			output = 50D

			errorSetDefinition = definitionForPeaks('error', [NL:-10, NS:-5, ZE:0, PS:5, PL:10])
			outputSetDefinition = definitionForPeaks('output', [NL:0, NS:25, ZE:50, PS:75, PL:100])
			
			outputInferencer = new Inferencer(outputSetDefinition)
					.rule('PL', [error: 'NL'])
					.rule('PS', [error: 'NS'])
					.rule('ZE', [error: 'ZE'])
					.rule('NS', [error: 'PS'])
					.rule('NL', [error: 'PL'])
		}
	}

	@Test
	void error_NL() {
		assertEquals 100D, controller.control(40D)
		assertEquals 100D, controller.control(40D)
	}

	@Test
	void error_NL_NS() {
		assertEquals 87.5D, controller.control(42.5D)
		assertEquals 87.5D, controller.control(42.5D)
	}

	@Test
	void error_NS() {
		assertEquals 75D, controller.control(45D)
		assertEquals 75D, controller.control(45D)
	}

	@Test
	void test_error_NS_ZE() {
		assertEquals 62.5D, controller.control(47.5D)
		assertEquals 62.5D, controller.control(47.5D)
	}

	@Test
	void error_ZE() {
		assertEquals 50D, controller.control(50D)
		assertEquals 50D, controller.control(50D)
	}

	@Test
	void error_ZE_PS() {
		assertEquals 37.5D, controller.control(52.5D)
		assertEquals 37.5D, controller.control(52.5D)
	}

	@Test
	void error_PS() {
		assertEquals 25D, controller.control(55D)
		assertEquals 25D, controller.control(55D)
	}

	@Test
	void error_PS_PL() {
		assertEquals 12.5D, controller.control(57.5D)
		assertEquals 12.5D, controller.control(57.5D)
	}

	@Test
	void error_PL() {
		assertEquals 0D, controller.control(60D)
		assertEquals 0D, controller.control(60D)
	}
	
	private void assertEquals(double expected, double actual) {
		final double ACCCURACY = 0.01d
		org.junit.Assert.assertEquals('', expected, actual, ACCCURACY)
	}
}