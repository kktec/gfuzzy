package org.gfuzzy.decider

import static org.junit.Assert.*

import org.gfuzzy.Fuzzy
import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.inference.Inferencer
import org.junit.Before
import org.junit.Test

class DeciderTests {

	static final double ACCURACY = 0.1d

	def win = FuzzySetDefinition.definitionForPeaks('win', [Lose: 0, Tie: 50, Win: 100])

	Inferencer inferencer = new Inferencer(win)

	Decider decider = new Decider(inferencer)

	def visiting = 'Visiting'

	def home = 'Home'

	def winPercentConstraint =
		FuzzySetDefinition.definitionForPeaks('winLoss', [VeryWeak: 0, Weak: 25, Mediocre: 50, Strong: 75, VeryStrong: 100])

	def fieldWinPercentConstraint =
		FuzzySetDefinition.definitionForPeaks('field', [VeryWeak: 0, Weak: 25, Mediocre: 50, Strong: 75, VeryStrong: 100])

	@Before
	void setup() {
		inferencer
				.rule('Win', [winLoss: 'Strong'], Fuzzy.MAX_VALUE)
				.rule('Tie', [winLoss: 'Mediocre'], Fuzzy.MAX_VALUE)
				.rule('Lose', [winLoss: 'Weak'], Fuzzy.MAX_VALUE)
				.rule('Win', [field: 'Strong'], 0.1)
				.rule('Tie', [field: 'Mediocre'], 0.1)
				.rule('Lose', [field: 'Weak'], 0.1)
	}

	@Test
	void can_addAlternatives() {
		addAlternatives()
		assertTrue decider.alternatives.contains(visiting)
		assertTrue decider.alternatives.contains(home)
		assertEquals 2, decider.alternatives.size()
	}

	@Test
	void can_addConstraints() {
		addConstraints()
		assertSame winPercentConstraint, decider.constraints['winLoss']
		assertSame fieldWinPercentConstraint, decider.constraints['field']
		assertEquals 2, decider.constraints.size()
	}

	@Test
	void can_addConstraintValuesForAlternatives() {
		addConstraintValuesForAlternatives()
		assertEquals 2, decider.alternativeConstraintValues[visiting].size()
		assertEquals 2, decider.alternativeConstraintValues[home].size()
		assertEquals 65, decider.alternativeConstraintValues[visiting]['winLoss'], ACCURACY
		assertEquals 35, decider.alternativeConstraintValues[visiting]['field'], ACCURACY
		assertEquals 75, decider.alternativeConstraintValues[home]['winLoss'], ACCURACY
		assertEquals 50, decider.alternativeConstraintValues[home]['field'], ACCURACY
	}

	@Test
	void can_decide() {
		addAlternatives()
		addConstraints()
		addConstraintValuesForAlternatives()
		Map<String, Number> decisions = decider.decide()
		assertEquals 75.5d, decisions[visiting], ACCURACY
		assertEquals 95.5d, decisions[home], ACCURACY
		assertEquals 2, decisions.size()
	}

	//TODO:	@Test
	void can_decide_withMissingConstraintValuesForAlternative() {
		addAlternatives()
		addConstraints()
		decider.addConstraintValueForAlternative 'winLoss', 65, visiting
		decider.addConstraintValueForAlternative 'field', 35, visiting
		decider.addConstraintValueForAlternative 'winLoss', 75, home

		Map<String, Number> decisions = decider.decide()
		assertEquals 75.5d, decisions[visiting], ACCURACY
		assertEquals 95.5d, decisions[home], ACCURACY
		assertEquals 2, decisions.size()
	}

	private addAlternatives() {
		decider.addAlternative visiting
		decider.addAlternative home
	}

	private addConstraints() {
		decider.addConstraint winPercentConstraint
		decider.addConstraint fieldWinPercentConstraint
	}

	private addConstraintValuesForAlternatives() {
		decider.addConstraintValueForAlternative 'winLoss', 65, visiting
		decider.addConstraintValueForAlternative 'field', 35, visiting
		decider.addConstraintValueForAlternative 'winLoss', 75, home
		decider.addConstraintValueForAlternative 'field', 50, home
	}

}
