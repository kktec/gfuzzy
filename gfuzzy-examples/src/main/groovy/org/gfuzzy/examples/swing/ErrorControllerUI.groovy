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

import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.examples.ErrorController
import org.gfuzzy.inference.Inferencer
import org.gfuzzy.inference.Rule
import org.gfuzzy.swing.FuzzySetPanel
import org.gfuzzy.util.DoubleCategory

class ErrorControllerUI {

	def swing = new SwingBuilder()
	def sampleCount

	ErrorController controller
	def input = 0.0

	JSlider inputSlider
	Color inputColor = new Color(0, 150, 0)
	JLabel inputLabel
	def inputChanged = {
		input = inputSlider.value
		use(DoubleCategory) {
			inputLabel.text = input.formatDefault()
		}
	}

	JSlider setpointSlider
	Color setpointColor = Color.BLUE
	JLabel setpointLabel
	def setpointChanged = {
		def setpoint = setpointSlider.value
		controller.setpoint = setpoint
		use(DoubleCategory) {
			setpointLabel.text = setpoint.formatDefault()
		}
	}

	JLabel errorLabel

	JProgressBar outputBar
	Color outputColor = Color.RED
	JLabel outputLabel
	JLabel resultLabel

	JButton updateButton
	def updateAction = {
		def output = controller.control(input)
		use(DoubleCategory) {
			outputLabel.text = output.formatDefault()
			outputBar.value = output
			errorLabel.text = controller.error
			trendPanel.addSample()
			errorPanel.update controller.error
			outputPanel.update controller.result
			resultLabel.text = controller.result.formatDefault()
		}
	}

	ControllerTrendPanel trendPanel
	FuzzySetPanel errorPanel
	FuzzySetPanel outputPanel

	void init() {
		errorPanel = new FuzzySetPanel(fuzzySetDefinition: controller.errorSetDefinition)
		outputPanel = new FuzzySetPanel(fuzzySetDefinition: controller.outputSetDefinition)

		def lafs = UIManager.installedLookAndFeels
		def nimbus = lafs.find { it.name == 'Nimbus' }
		if (nimbus) {
			swing.lookAndFeel('nimbus')
		}

		swing.frame(title: "gfuzzy Controller Demo",
				defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
				location: [100, 200],
				preferredSize: [1000, 660],
				pack:true, show:true) {
					scrollPane {
						panel {
							vbox {
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

	def buildHeaderPanel = {
		swing.panel {
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
		use(DoubleCategory) {
			swing.panel(layout: new BorderLayout()) {
				panel(constraints: BorderLayout.WEST) {
					panel(layout: new BorderLayout()) {
						panel(constraints: BorderLayout.CENTER, layout: new BorderLayout()) { buildInputSlider() }
						panel(constraints: BorderLayout.SOUTH) {
							label("Input:", foreground: inputColor)
							inputLabel =  label(input.formatDefault())
						}
					}
					panel(layout: new BorderLayout()) {
						panel(constraints: BorderLayout.CENTER, layout: new BorderLayout()) { buildSetpointSlider() }
						panel(constraints: BorderLayout.SOUTH) {
							label("Setpoint:", foreground: setpointColor)
							setpointLabel = label(controller.setpoint.formatDefault())
						}
					}
					panel(layout: new BorderLayout()) {
						panel(constraints: BorderLayout.CENTER, layout: new BorderLayout()) {
							outputBar = progressBar(orientation: JProgressBar.VERTICAL, foreground: outputColor, preferredSize: [0, 200])
						}
						panel(constraints: BorderLayout.SOUTH) {
							label("Output:", foreground: outputColor)
							outputLabel = label(controller.output.formatDefault())
						}
					}
				}
			}
		}
	}

	def buildFuzzySetPanel = {
		swing.panel {
			hbox {
				hstrut(width: 20)
				panel(layout: new BorderLayout()) {
					panel(errorPanel,  preferredSize: [300, 200])
					panel(constraints: BorderLayout.SOUTH) {
						label(horizontalAlignment: JLabel.CENTER, errorPanel.fuzzySetDefinition.name)
						use(DoubleCategory) {
							errorLabel = label(controller.error.formatDefault())
						}
					}
				}
				hstrut(width: 40)
				panel(layout: new BorderLayout()) {
					panel(outputPanel, preferredSize: [300, 200])
					panel(constraints: BorderLayout.SOUTH) {
						label(horizontalAlignment: JLabel.CENTER, outputPanel.fuzzySetDefinition.name)
						use(DoubleCategory) {
							resultLabel = label(controller.result.formatDefault())
						}
					}
				}
				hstrut(width: 20)
			}
		}
	}

	def buildControlsPanel = {
		swing.panel {
			hbox {
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


	static void main(String[] args) {
		ErrorController controller = new ErrorController(range: 0..100, outputRange: 0..100, setpoint: 50,
				errorSetDefinition: FuzzySetDefinition.definitionForPeaks("error", [NL:-10, NS:-5, ZE:0, PS:5, PL:10]),
				outputSetDefinition: FuzzySetDefinition.definitionForPeaks("output", [NL:-5, NS:-2.5, ZE:0, PS:2.5, PL:5]))

		Inferencer inferencer = new Inferencer()
		inferencer << new Rule('PL', ['error': 'NL'])
		inferencer << new Rule('PS', ['error': 'NS'])
		inferencer << new Rule('ZE', ['error': 'ZE'])
		inferencer << new Rule('NS', ['error': 'PS'])
		inferencer << new Rule('NL', ['error': 'PL'])
		controller.outputInferencer = inferencer

		new ErrorControllerUI(controller: controller, sampleCount: 500).init()
	}
}
