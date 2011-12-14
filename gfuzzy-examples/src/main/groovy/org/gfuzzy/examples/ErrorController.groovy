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

	double input
	
	double error
	
	double output
	
	double result

	double control(double input) {
		this.input = input
		error = input - setpoint
		
		def errors = errorSetDefinition.fuzzify(error)
		def inputs = [error: errors]
		
		result = outputInferencer.infer(inputs)
		if(positioner) {
			output = result
		} else {
			output += result
		}
		
		output = RangeCategory.limit(outputRange, output)
	}

}