package org.gfuzzy.examples

import static org.gfuzzy.FuzzySetDefinition.*

import org.gfuzzy.*
import org.gfuzzy.inference.Inferencer
import org.gfuzzy.zone.*

/**
 * @author Ken Krebs
 *
 */
class ErrorVelocityControllerTests extends GroovyTestCase {

	ErrorController controller = new ErrorController()

	@Override
	void setUp() {
		controller.with { 
			outputRange = 0D..100D
			setpoint = 50D
			output = 50D
	
			errorSetDefinition = definitionForPeaks("error", [NL:-10, NS:-5, ZE:0, PS:5, PL:10])
			outputSetDefinition = definitionForPeaks("output", [NL:-5, NS:-2.5, ZE:0, PS:2.5, PL:5])
//			inferencer = new Inferencer(inputs: [errorSetDefinition], outputs: [outputSetDefinition]//,
//				rules: [
//					new Rule(effect: outputSet['PL'], predicates: [errorSet['NL']]),
//					new Rule(effect: outputSet['SL'], predicates: [errorSet['NS']]),
//					new Rule(effect: outputSet['ZE'], predicates: [errorSet['ZE']]),
//					new Rule(effect: outputSet['NS'], predicates: [errorSet['PS']]),
//					new Rule(effect: outputSet['NL'], predicates: [errorSet['PL']])
//				]
//			)
		}
	}

	void test_error_NL() {
		assertEquals 55D, controller.control(40D)
		assertEquals 60D, controller.control(40D)
	}

	void test_error_NL_NS() {
		assertEquals 53.75D, controller.control(42.5D)
		assertEquals 57.5D, controller.control(42.5D)
	}

	void test_error_NS() {
		assertEquals 52.5D, controller.control(45D)
		assertEquals 55D, controller.control(45D)
	}

	void test_error_NS_ZE() {
		assertEquals 51.25D, controller.control(47.5D)
		assertEquals 52.5D, controller.control(47.5D)
	}

	void test_error_ZE() {
		assertEquals 50D, controller.control(50D)
	}

	void test_error_ZE_PS() {
		assertEquals 48.75D, controller.control(52.5D)
		assertEquals 47.5D, controller.control(52.5D)
	}

	void test_error_PS() {
		assertEquals 47.5D, controller.control(55D)
		assertEquals 45D, controller.control(55D)
	}

	void test_error_PS_PL() {
		assertEquals 46.25D, controller.control(57.5D)
		assertEquals 42.5D, controller.control(57.5D)
	}

	void test_error_PL() {
		assertEquals 45D, controller.control(60D)
		assertEquals 40D, controller.control(60D)
	}

}