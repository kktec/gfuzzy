package org.gfuzzy.examples

import org.gfuzzy.*

/**
 * @author Ken Krebs
 *
 */
class ErrorController {

	boolean positioner = false
	
	Range range

	Range outputRange

	FuzzySet errorSet

	FuzzySet outputSet
	
	def infer = { errors, outputs ->
		outputs.PL |= errors.NL
		outputs.PS |= errors.NS
		outputs.ZE |= errors.ZE
		outputs.NS |= errors.PS
		outputs.NL |= errors.PL
	}

	double setpoint

	double output
	
	double input
	
	double error
	
	double result

	double control(double input) {
		this.input = input
		error = input - setpoint
		
		def errors = errorSet.fuzzify(error)
		def outputs = outputSet.fuzzies()
		infer(errors, outputs)

		result = outputSet.defuzzify(outputs)
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