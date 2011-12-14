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
class ErrorVelocityControllerTests {

	ErrorController controller = new ErrorController()

	@Before
	void setUp() {
		controller.with {
			outputRange = 0D..100D
			setpoint = 50D
			output = 50D

			errorSetDefinition = definitionForPeaks('error', [NL:-10, NS:-5, ZE:0, PS:5, PL:10])
			outputSetDefinition = definitionForPeaks('output', [NL:-5, NS:-2.5, ZE:0, PS:2.5, PL:5])
			
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
		assertEquals 55D, controller.control(40D)
		assertEquals 60D, controller.control(40D)
	}

	@Test
	void error_NL_NS() {
		assertEquals 53.75D, controller.control(42.5D)
		assertEquals 57.5D, controller.control(42.5D)
	}

	@Test
	void error_NS() {
		assertEquals 52.5D, controller.control(45D)
		assertEquals 55D, controller.control(45D)
	}

	@Test
	void error_NS_ZE() {
		assertEquals 51.25D, controller.control(47.5D)
		assertEquals 52.5D, controller.control(47.5D)
	}

	@Test
	void error_ZE() {
		assertEquals 50D, controller.control(50D)
		assertEquals 50D, controller.control(50D)
	}

	@Test
	void error_ZE_PS() {
		assertEquals 48.75D, controller.control(52.5D)
		assertEquals 47.5D, controller.control(52.5D)
	}

	@Test
	void error_PS() {
		assertEquals 47.5D, controller.control(55D)
		assertEquals 45D, controller.control(55D)
	}

	@Test
	void error_PS_PL() {
		assertEquals 46.25D, controller.control(57.5D)
		assertEquals 42.5D, controller.control(57.5D)
	}

	@Test
	void error_PL() {
		assertEquals 45D, controller.control(60D)
		assertEquals 40D, controller.control(60D)
	}
	
	private void assertEquals(double expected, double actual) {
		final double ACCCURACY = 0.01d
		org.junit.Assert.assertEquals('', expected, actual, ACCCURACY)
	}
}