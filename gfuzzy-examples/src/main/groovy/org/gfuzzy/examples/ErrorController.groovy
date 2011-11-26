package org.gfuzzy.examples

import org.gfuzzy.*
import org.gfuzzy.inference.Inferencer
import org.gfuzzy.util.RangeCategory

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
	
	Inferencer outputInferencer
	
	double setpoint

	double output
	
	double input
	
	double error
	
	double result

	double control(double input) {
		this.input = input
		error = input - setpoint
		
		def errors = errorSetDefinition.fuzzify(error)
		def outputs = outputSetDefinition.set()
		def inputs = [error: errors]
		outputInferencer.infer(inputs, outputs)

		result = outputSetDefinition.defuzzify(outputs)
		if(positioner) {
			output = result
		} else {
			output += result
		}
		
		use(RangeCategory) {
			output = outputRange.limit(output)
		}
	}

}