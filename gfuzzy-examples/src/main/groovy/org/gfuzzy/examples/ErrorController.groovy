package org.gfuzzy.examples

import org.gfuzzy.*

/**
 * @author Ken Krebs
 *
 */
class ErrorController {

	static final ZERO = 0D
	
	boolean positioner = false

	Range outputRange

	FuzzySet errorSet

	FuzzySet outputSet

	def infer = { Map errors, Map outputs ->
		outputs.PL |= errors.NL
		outputs.PS |= errors.NS
		outputs.ZE |= errors.ZE
		outputs.NS |= errors.PS
		outputs.NL |= errors.PL
	}

	double setpoint = ZERO

	double output = ZERO

	double control(double input) {
		double error = input - setpoint
		def errors = errorSet.fuzzify(error)
		Map outputs = outputSet.fuzzies()
		infer(errors, outputs)

		double result = outputSet.defuzzify(outputs)
		if(positioner) {
			output = result
		} else {
			output += result
		}

		if(output <= outputRange.from) {
			output = outputRange.from
		}
		else if(output >= outputRange.to) {
			output = outputRange.to
		}
		
		output
	}

}