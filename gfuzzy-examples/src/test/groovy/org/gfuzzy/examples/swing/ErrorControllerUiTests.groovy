package org.gfuzzy.examples.swing

import static org.junit.Assert.*

import org.junit.Test

class ErrorControllerUiTests {

	@Test
	void canCreateUI() {
		assertNotNull new ErrorControllerUI(controller:ErrorControllerUI.create())
	}
}
