package org.gfuzzy.examples.swing

import java.awt.Color
import java.awt.Graphics

import javax.swing.JPanel

import org.gfuzzy.examples.ErrorController

class ControllerTrendPanel extends JPanel {

	ErrorController controller
	
	def colors

	def sampleCount

	def samples = []

	int currentSample = 0

	void addSample() {
		samples << new Expando(input: controller.input, setpoint: controller.setpoint, output: controller.output)
		currentSample++
		repaint()
	}

	@Override
	void paint(Graphics g) {
		super.paint g
		g.color = Color.WHITE
		int w = width - 1
		int h = height - 1
		g.fillRect(x, y, w, h)
		g.color = Color.BLACK
		g.drawRect(x, y, w, h)
		drawSamples g
	}

	void drawSamples(Graphics g) {
		if(samples.size() == 0) {
			return
		}
		
		for (int i in 0..<sampleCount) {
			int index = i
			if(samples.size() > sampleCount) {
				index += samples.size() - sampleCount
			}
			def sample0 = samples[index]
			def sample1 = sample0
			if (index < samples.size() - 1) { 
				sample1 =  samples[index + 1]
			}
			
			g.color = colors.setpointColor
			g.drawLine(i, calculateY(sample0.setpoint), i + 1, calculateY(sample1.setpoint))
			
			g.color = colors.inputColor
			int iy0 = calculateY(sample0.input)
			int iy1 = calculateY(sample1.input)
			g.drawLine(i, iy0, i + 1, iy1)
			
			g.color = colors.outputColor
			g.drawLine(i, calculateY(sample0.output), i + 1, calculateY(sample1.output))
			
			if(index >= samples.size() - 1) {
				break;
			}
		}
	}
	
	int calculateY(v) {
		double range = controller.range.to - controller.range.from
		double plotRange = height - 3
		int y = v / range * plotRange 
		int translateY = plotRange - y + 1
		translateY
	}
}
