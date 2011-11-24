package org.gfuzzy.examples

import org.gfuzzy.*
import org.gfuzzy.inference.Inferencer

/**
 * @author Ken Krebs
 *
 */
class ErrorController {

	boolean positioner = false
	
	Range range

	Range outputRange

	FuzzySetDefinition errorSetDefinition

	FuzzySetDefinition outputSetDefinition
	
	Inferencer inferencer
	
	double setpoint

	double output
	
	double input
	
	double error
	
	double result

	double control(double input) {
		this.input = input
		error = input - setpoint
		
		def errors = errorSetDefinition.fuzzify(error)
		def outputs = outputSetDefinition.fuzzies()
		inferencer.infer(['error': errors], outputs)

		result = outputSetDefinition.defuzzify(outputs)
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