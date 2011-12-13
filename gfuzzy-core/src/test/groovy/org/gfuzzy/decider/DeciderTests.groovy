package org.gfuzzy.decider

import static org.junit.Assert.*

import org.gfuzzy.Fuzzy
import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.inference.Rule
import org.junit.Test

class DeciderTests {
	
	static final double ACCURACY = 0.1d

	Decider decider = new Decider()
	
	FuzzySetDefinition win = FuzzySetDefinition.definitionForPeaks('win', [Lose: 0, Tie: 50, Win: 100])
	
	def visiting = 'Visiting'
	
	def home = 'Home'
	
	FuzzySetDefinition winPercentConstraint = 
		FuzzySetDefinition.definitionForPeaks('winLoss', [VeryWeak: 0, Weak: 25, Mediocre: 50, Strong: 75, VeryStrong: 100])
	
	FuzzySetDefinition fieldWinPercentConstraint = 
		FuzzySetDefinition.definitionForPeaks('field', [VeryWeak: 0, Weak: 25, Mediocre: 50, Strong: 75, VeryStrong: 100])
	
	List<Rule> rules = [
		new Rule('Win', [winLoss: 'Strong'], Fuzzy.MAX_VALUE), 
		new Rule('Tie', [winLoss: 'Mediocre'], Fuzzy.MAX_VALUE),
		new Rule('Lose', [winLoss: 'Weak'], Fuzzy.MAX_VALUE),
		new Rule('Win', [field: 'Strong'], 0.1), 
		new Rule('Tie', [field: 'Mediocre'], 0.1),
		new Rule('Lose', [field: 'Weak'], 0.1)
	]

	
	@Test
	void canSetGoal() {
		assertNull decider.goal
		decider.goal = win
		assertSame win, decider.goal
	}
	
	@Test
	void canAddRules() {
		addRules()
		assertSame rules[0], decider.rules[0]
		assertSame rules[1], decider.rules[1]
		assertEquals 6, decider.rules.size()
	}

	@Test
	void canAddAlternatives() {
		addAlternatives()
		assertTrue decider.alternatives.contains(visiting)
		assertTrue decider.alternatives.contains(home)
		assertEquals 2, decider.alternatives.size()
	}

	@Test
	void canAddConstraints() {
		addConstraints()
		assertSame winPercentConstraint, decider.constraints['winLoss']
		assertSame fieldWinPercentConstraint, decider.constraints['field']
		assertEquals 2, decider.constraints.size()
	}
	
	@Test
	void canAddConstraintValuesForAlternatives() {
		addConstraintValuesForAlternatives()
		assertEquals 2, decider.alternativeConstraintValues[visiting].size()
		assertEquals 2, decider.alternativeConstraintValues[home].size()
		assertEquals 65, decider.alternativeConstraintValues[visiting]['winLoss'], ACCURACY
		assertEquals 35, decider.alternativeConstraintValues[visiting]['field'], ACCURACY
		assertEquals 75, decider.alternativeConstraintValues[home]['winLoss'], ACCURACY
		assertEquals 50, decider.alternativeConstraintValues[home]['field'], ACCURACY
	}
	
	@Test
	void canDecide() {
		decider.goal = win
		addRules()
		addAlternatives()
		addConstraints()
		addConstraintValuesForAlternatives()
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
		decider.addConstraintValueForAlternative('winLoss', 65, visiting)
		decider.addConstraintValueForAlternative('field', 35, visiting)
		decider.addConstraintValueForAlternative('winLoss', 75, home)
		decider.addConstraintValueForAlternative('field', 50, home)
	}

	private addRules() {
		rules.each {
			decider.addRule it
		}
	}

}
