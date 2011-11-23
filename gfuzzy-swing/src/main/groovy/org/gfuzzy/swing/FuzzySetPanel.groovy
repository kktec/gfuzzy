package org.gfuzzy.swing

import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics

import javax.swing.JPanel

import org.gfuzzy.FallingFuzzyZone
import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.RisingFallingFuzzyZone
import org.gfuzzy.RisingFuzzyZone

class FuzzySetPanel extends JPanel {

	FuzzySetDefinition fuzzySetDefinition

	def sizeInfo
	
	double value

	@Override
	void paint(Graphics g) {
		super.paint g

		JPanel p = new JPanel()
		g.color = p.background
		int w = width - 1
		int h = height - 1
		g.fillRect(x, y, w, h)
		g.color = Color.BLACK
		g.drawRect(x, y, w, h)

		sizeInfo = createSizeInfo()

		int textHeight = sizeInfo.minY / 3
		Font font =  new Font('Arial', Font.BOLD, textHeight)
		g.font = font

		int extraX = width * 0.05
		g.drawLine sizeInfo.minX - extraX, sizeInfo.minY, sizeInfo.maxX + extraX, sizeInfo.minY
		g.drawLine sizeInfo.minX - extraX, sizeInfo.maxY, sizeInfo.maxX + extraX, sizeInfo.maxY
		fuzzySetDefinition.zones.each { 
			drawZone(it, g)
		}
		drawValue(g)
	}
	
	void update(double v) {
		if(v < fuzzySetDefinition.from) {
			value = fuzzySetDefinition.from
		}
		else if(v > fuzzySetDefinition.to) {
			value = fuzzySetDefinition.to
		}
		else {
			value = v
		}
		repaint()
	}

	Expando createSizeInfo() {
		Expando sizeInfo = new Expando()
		sizeInfo.range = fuzzySetDefinition.to - fuzzySetDefinition.from
		int minY = height * 0.15
		sizeInfo.minY = minY
		int maxY = height - minY
		sizeInfo.maxY = maxY
		int minX = width * 0.1
		sizeInfo.minX = minX
		int maxX = width - minX
		sizeInfo.maxX = maxX
		int rangeX = maxX - minX
		sizeInfo.rangeX = rangeX
		sizeInfo
	}
	
	void drawValue(Graphics g) {
		int x = calculateX(value - fuzzySetDefinition.from)
		g.color = new Color(176, 176, 0)
		g.drawLine x, sizeInfo.minY + 1, x, sizeInfo.maxY - 1
		
	}

	void drawZone(FallingFuzzyZone z, Graphics g) {
		double from = z.from - fuzzySetDefinition.from
		double to = z.to - fuzzySetDefinition.from
		int x = calculateX(from)
		g.drawLine x, sizeInfo.minY, calculateX(to), sizeInfo.maxY
		drawZoneName z, x, g
		drawZonePeak z.from, x, g
	}

	void drawZone(RisingFallingFuzzyZone z, Graphics g) {
		double to = z.to - fuzzySetDefinition.from
		double peak = z.peak - fuzzySetDefinition.from
		int xP = calculateX(peak)
		g.drawLine calculateX(to), sizeInfo.maxY, xP, sizeInfo.minY
		double from = z.from - fuzzySetDefinition.from
		g.drawLine xP, sizeInfo.minY, calculateX(from), sizeInfo.maxY
		drawZoneName z, xP, g
		drawZonePeak z.peak, xP, g
	}

	void drawZone(RisingFuzzyZone z, Graphics g) {
		double to = z.to - fuzzySetDefinition.from
		double from = z.from - fuzzySetDefinition.from
		int x = calculateX(to)
		g.drawLine x, sizeInfo.minY, calculateX(from), sizeInfo.maxY
		drawZoneName z, x, g
		drawZonePeak z.to, x, g
	}

	void drawZoneName(z, x, Graphics g) {
		FontMetrics fm = g.fontMetrics
		int width = fm.stringWidth(z.name)
		g.drawString z.name, x - width / 2, sizeInfo.minY - 5 * fm.leading - fm.descent
	}

	void drawZonePeak(v, x, Graphics g) {
		FontMetrics fm = g.fontMetrics
		int width = fm.stringWidth(v.toString())
		g.drawString v.toString(), x - width / 2, sizeInfo.maxY + 5 * fm.leading + fm.ascent
	}

	int calculateX(v) {
		sizeInfo.with {
			(v / range * rangeX) + minX
		}
	}
}
