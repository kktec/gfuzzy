package org.gfuzzy.examples.swing

import static org.junit.Assert.*

import org.junit.After
import org.junit.Test

class ErrorControllerUISmokeTests {

	def controller = ErrorControllerUI.create()
	def ui = new ErrorControllerUI(controller:controller)

	@After
	void teardown() {
		ui?.closeWindow()
	}

	@Test
	void canCreateUI_withoutDisplayingIt() {
		assertNotNull ui
		ui.initUI()
	}
}
