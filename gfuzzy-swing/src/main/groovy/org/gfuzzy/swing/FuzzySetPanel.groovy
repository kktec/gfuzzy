package org.gfuzzy.swing

import groovy.util.Expando

import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics

import javax.swing.JPanel

import org.gfuzzy.FallingFuzzyZone
import org.gfuzzy.FuzzySet
import org.gfuzzy.RisingFallingFuzzyZone
import org.gfuzzy.RisingFuzzyZone

class FuzzySetPanel extends JPanel {

	def FuzzySet fuzzySet

	def sizeInfo

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
		fuzzySet.zones.each { drawZone(it, g) }
	}

	Expando createSizeInfo() {
		Expando sizeInfo = new Expando()
		sizeInfo.range = fuzzySet.to - fuzzySet.from
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

	void drawZone(FallingFuzzyZone z, Graphics g) {
		double from = z.from - fuzzySet.from
		double to = z.to - fuzzySet.from
		int x = calculateX(from)
		g.drawLine x, sizeInfo.minY, calculateX(to), sizeInfo.maxY
		drawZoneName z, x, g
		drawZonePeak z.from, x, g
	}

	void drawZone(RisingFallingFuzzyZone z, Graphics g) {
		double to = z.to - fuzzySet.from
		double peak = z.peak - fuzzySet.from
		int xP = calculateX(peak)
		g.drawLine calculateX(to), sizeInfo.maxY, xP, sizeInfo.minY
		double from = z.from - fuzzySet.from
		g.drawLine xP, sizeInfo.minY, calculateX(from), sizeInfo.maxY
		drawZoneName z, xP, g
		drawZonePeak z.peak, xP, g
	}

	void drawZone(RisingFuzzyZone z, Graphics g) {
		double to = z.to - fuzzySet.from
		double from = z.from - fuzzySet.from
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
