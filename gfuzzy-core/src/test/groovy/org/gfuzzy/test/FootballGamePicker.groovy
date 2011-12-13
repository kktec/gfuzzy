package org.gfuzzy.test

import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.decider.Decider
import org.gfuzzy.inference.Rule
import org.gfuzzy.util.DoubleCategory

class FootballGamePicker {

	FuzzySetDefinition win = FuzzySetDefinition.definitionForPeaks('win', [Lose: 0, Tie: 50, Win: 100])

	FuzzySetDefinition winPercentConstraint = 
		FuzzySetDefinition.definitionForPeaks('winLoss', [VeryWeak: 0, Weak: 25, Mediocre: 50, Strong: 75, VeryStrong: 100])

	FuzzySetDefinition fieldWinPercentConstraint = 
		FuzzySetDefinition.definitionForPeaks('field', [VeryWeak: 0, Weak: 25, Mediocre: 50, Strong: 75, VeryStrong: 100])

	FuzzySetDefinition homeFieldConstraint = 
		FuzzySetDefinition.definitionForPeaks('homeField', [Visitor: 0, Home: 1])

	FuzzySetDefinition healthinessConstraint = 
		FuzzySetDefinition.definitionForPeaks('healthiness', [Bad: 0, Good: 100])

	FuzzySetDefinition favoredByConstraint = 
		FuzzySetDefinition.definitionForPeaks('favoredBy', [NotFavored: 0, Favored: 100])

	List<Rule> rules = [
		new Rule('Win', [winLoss: 'Strong'], 1.0),
		new Rule('Tie', [winLoss: 'Mediocre'], 1.0),
		new Rule('Lose', [winLoss: 'Weak'], 1.0),
		new Rule('Win', [field: 'Strong'], 0.5),
		new Rule('Tie', [field: 'Mediocre'], 0.5),
		new Rule('Lose', [field: 'Weak'], 0.5),
		new Rule('Win', [homeField: 'Home'], 0.01),
		new Rule('Lose', [homeField: 'Visitor'], 0.01),
		new Rule('Win', [healthiness: 'Good'], 0.2),
		new Rule('Lose', [healthiness: 'Bad'], 0.2),
		new Rule('Win', [favoredBy: 'Favored'], 1.0),
		new Rule('Tie', [favoredBy: 'NotFavored'], 1.0),
	]

	Decider decider = new Decider(goal:win, rules: rules)

	FootballGamePicker() {
		decider.with {
			addConstraint winPercentConstraint
			addConstraint fieldWinPercentConstraint
			addConstraint homeFieldConstraint
			addConstraint healthinessConstraint
			addConstraint favoredByConstraint
		}
	}

	Pick pick(String scenarioDescription, Scenario visiting, Scenario home) {
		decider.with {
			addAlternative visiting.team
			addConstraintValuesForScenario visiting
			addAlternative home.team
			addConstraintValuesForScenario home
		}

		def pick = new Pick()
		Map<String, Number> decisions = decider.decide()
		pick.with {
			scenario = scenarioDescription
			double vc = decisions[visiting.team]
			visitingConfidence = DoubleCategory.format(vc, 2)
			double hc = decisions[home.team]
			homeConfidence = DoubleCategory.format(hc, 2)
			double c = Math.abs(hc - vc)
			confidence = DoubleCategory.format(c, 2)
			if (confidence == '0.00') {
				winner = 'Cannot pick a Winner'
			} else {
				winner = hc > vc ? "$home.team" : "$visiting.team"
			}
		}
		pick
	}

	void addConstraintValuesForScenario(scenario) {
		final Double FAVORED_BY_SCALING_FACTOR = 15.0
		decider.with {
			scenario.with {
				def alternative = team
				addConstraintValueForAlternative 'winLoss', percentWins(wins, losses), alternative
				addConstraintValueForAlternative 'field', percentWins(fieldWins, fieldLosses), alternative
				addConstraintValueForAlternative 'homeField', athome ? 1d : 0d, alternative
				addConstraintValueForAlternative 'healthiness', healthiness, team
				addConstraintValueForAlternative 'favoredBy', favoredBy * FAVORED_BY_SCALING_FACTOR, alternative
			}
		}
	}
	
	double percentWins(wins, losses) {
		int total = wins + losses
		100d * wins / total
	}
	
}
