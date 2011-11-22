package org.gfuzzy.examples.swing

import groovy.swing.SwingBuilder

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font

import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JProgressBar
import javax.swing.JSlider
import javax.swing.UIManager
import javax.swing.WindowConstants

import org.gfuzzy.FuzzySet
import org.gfuzzy.examples.ErrorController
import org.gfuzzy.swing.FuzzySetPanel

class ErrorControllerUI {

	ErrorController controller
	def input = 0.0

	JSlider inputSlider
	Color inputColor = new Color(0, 150, 0)
	JLabel inputLabel
	def inputChanged = {
		input = inputSlider.value * 1.0
		inputLabel.text = input
	}

	JSlider setpointSlider
	Color setpointColor = Color.BLUE
	JLabel setpointLabel
	def setpointChanged = {
		def setpoint = setpointSlider.value * 1.0
		controller.setpoint = setpoint
		setpointLabel.text = setpoint
	}

	JLabel errorLabel

	JProgressBar outputBar
	Color outputColor = Color.RED
	JLabel outputLabel

	JButton updateButton
	def updateAction = {
		def output = controller.control(input) * 1.0
		outputLabel.text = output
		outputBar.value = output
		errorLabel.text = controller.error
		trendPanel.addSample()
	}

	ControllerTrendPanel trendPanel
	FuzzySetPanel errorPanel
	FuzzySetPanel outputPanel

	void buildUi(sampleCount) {
		errorPanel = new FuzzySetPanel(fuzzySet: controller.errorSet)
		outputPanel = new FuzzySetPanel(fuzzySet: controller.outputSet)

		def swing = new SwingBuilder()
		println UIManager.getInstalledLookAndFeels()
		swing.lookAndFeel('nimbus')

		def buildHeaderPanel = {
			swing.panel() {
				label(font: new Font('Arial', Font.BOLD, 32), 'Error Velocity Controller')
			}
		}

		def buildInputSlider = {
			inputSlider = swing.slider(orientation: JSlider.VERTICAL)
			inputSlider.with {
				value = 0
				majorTickSpacing = 10
				paintTicks = true
				paintLabels = true
				stateChanged = inputChanged
			}
		}

		def buildSetpointSlider = {
			setpointSlider = swing.slider(orientation: JSlider.VERTICAL)
			setpointSlider.with {
				value = controller.setpoint
				majorTickSpacing = 10
				paintTicks = true
				paintLabels = true
				stateChanged = setpointChanged
			}
		}

		def buildWidgetsPanel = {
			swing.panel(layout: new BorderLayout()) {
				panel(constraints: BorderLayout.WEST) {
					panel(layout: new BorderLayout()) {
						panel(constraints: BorderLayout.CENTER, layout: new BorderLayout()) { buildInputSlider() }
						panel(constraints: BorderLayout.SOUTH) {
							label("Input:", foreground: inputColor)
							inputLabel =  label("$input")
						}
					}
					panel(layout: new BorderLayout()) {
						panel(constraints: BorderLayout.CENTER, layout: new BorderLayout()) { buildSetpointSlider() }
						panel(constraints: BorderLayout.SOUTH) {
							label("Setpoint:", foreground: setpointColor)
							setpointLabel = label("$controller.setpoint")
						}
					}
					panel(layout: new BorderLayout()) {
						panel(constraints: BorderLayout.CENTER, layout: new BorderLayout()) {
							outputBar = progressBar(orientation: JProgressBar.VERTICAL, foreground: outputColor, preferredSize: [0, 200])
						}
						panel(constraints: BorderLayout.SOUTH) {
							label("Output:", foreground: outputColor)
							outputLabel = label("$controller.output")
						}
					}
				}
			}
		}

		def buildFuzzySetPanel = {
			swing.panel() {
				hbox() {
					hstrut(width: 20)
					panel(layout: new BorderLayout()) {
						panel(errorPanel,  preferredSize: [300, 200])
						panel(constraints: BorderLayout.SOUTH) {
							label(horizontalAlignment: JLabel.CENTER, errorPanel.fuzzySet.name)
							errorLabel = label('0.0')
						}
					}
					hstrut(width: 40)
					panel(layout: new BorderLayout()) {
						panel(outputPanel, preferredSize: [300, 200])
						panel(constraints: BorderLayout.SOUTH) {
							label(horizontalAlignment: JLabel.CENTER, outputPanel.fuzzySet.name)
						}
					}
					hstrut(width: 20)
				}
			}
		}

		def buildControlsPanel = {
			swing.panel() {
				hbox() {
					hstrut(width: 20)
					buildWidgetsPanel()
					hstrut(width: 40)
					panel(layout: new BorderLayout()) {
						trendPanel = panel(new ControllerTrendPanel(controller: controller, 
							colors: [inputColor: inputColor, setpointColor: setpointColor, outputColor: outputColor],
							sampleCount: sampleCount), preferredSize: [sampleCount + 2, 202])
					}
					hstrut(width: 20)
				}
			}
		}

		def buildButtonPanel = {
			swing.panel(constraints: BorderLayout.SOUTH) {
				updateButton = button(actionPerformed: updateAction, "Update Controller")
			}
		}


		swing.frame(title: "gfuzzy Controller Demo",
				defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
				location: [100, 200],
				preferredSize: [1000, 660],
				pack:true, show:true) {
					scrollPane {
						panel() {
							vbox() {
								buildHeaderPanel()
								vstrut()
								buildControlsPanel()
								vstrut(height: 20)
								buildFuzzySetPanel()
								vstrut()
								buildButtonPanel()
							}
						}
					}
				}
	}

	static void main(String[] args) {
		ErrorController controller = new ErrorController(range: 0..100, outputRange: 0..100, setpoint: 50,
				errorSet: FuzzySet.createFuzzySetForPeaks("error", [NL:-10, NS:-5, ZE:0, PS:5, PL:10]),
				outputSet: FuzzySet.createFuzzySetForPeaks("output", [NL:-5, NS:-2.5, ZE:0, PS:2.5, PL:5]))

		new ErrorControllerUI(controller: controller).buildUi(500)
	}
}