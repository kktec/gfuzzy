package org.gfuzzy.examples.swing

import static org.junit.Assert.*

import java.awt.event.WindowEvent

import org.junit.Test

class ErrorControllerUISmokeTests {

	@Test
	void canCreateUI_withoutDisplayingIt() {
		def ui = new ErrorControllerUI(controller:ErrorControllerUI.create())
		ui.initUI()
		ui.close()
	}
}
