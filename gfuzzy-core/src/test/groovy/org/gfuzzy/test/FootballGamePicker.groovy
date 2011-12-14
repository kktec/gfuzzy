package org.gfuzzy.test

import org.gfuzzy.FuzzySetDefinition
import org.gfuzzy.decider.Decider
import org.gfuzzy.inference.Inferencer
import org.gfuzzy.util.DoubleCategory

class FootballGamePicker {

	def win = FuzzySetDefinition.definitionForPeaks('win', [Lose: 0, Tie: 50, Win: 100])

	def winPercentConstraint =
		FuzzySetDefinition.definitionForPeaks('winLoss', [VeryWeak: 0, Weak: 25, Mediocre: 50, Strong: 75, VeryStrong: 100])

	def fieldWinPercentConstraint =
		FuzzySetDefinition.definitionForPeaks('field', [VeryWeak: 0, Weak: 25, Mediocre: 50, Strong: 75, VeryStrong: 100])

	def homeFieldConstraint =
		FuzzySetDefinition.definitionForPeaks('homeField', [Visitor: 0, Home: 1])

	def healthinessConstraint =
		FuzzySetDefinition.definitionForPeaks('healthiness', [Bad: 0, Good: 100])

	def favoredByConstraint =
		FuzzySetDefinition.definitionForPeaks('favoredBy', [NotFavored: 0, Favored: 100])

	Inferencer inferencer = new Inferencer(win)

	Decider decider = new Decider(inferencer)

	FootballGamePicker() {
		initRules()
		initDeciderConstraints()
	}

	private initDeciderConstraints() {
		decider.with {
			addConstraint winPercentConstraint
			addConstraint fieldWinPercentConstraint
			addConstraint homeFieldConstraint
			addConstraint healthinessConstraint
			addConstraint favoredByConstraint
		}
	}

	private initRules() {
		inferencer
				.rule('Win', [winLoss: 'Strong'])
				.rule('Tie', [winLoss: 'Mediocre'])
				.rule('Lose', [winLoss: 'Weak'])
				.rule('Win', [field: 'Strong'], 0.5)
				.rule('Tie', [field: 'Mediocre'], 0.5)
				.rule('Lose', [field: 'Weak'], 0.5)
				.rule('Win', [homeField: 'Home'], 0.01)
				.rule('Lose', [homeField: 'Visitor'], 0.01)
				.rule('Win', [healthiness: 'Good'], 0.2)
				.rule('Lose', [healthiness: 'Bad'], 0.2)
				.rule('Win', [favoredBy: 'Favored'])
				.rule('Tie', [favoredBy: 'NotFavored'])
	}

	Pick pick(String scenarioDescription, Scenario visiting, Scenario home) {
		decider.with {
			addAlternative visiting.team
			addConstraintValuesForScenario visiting
			addAlternative home.team
			addConstraintValuesForScenario home
		}

		Map<String, Number> decisions = decider.decide()
		pick(decisions, scenarioDescription, visiting, home)
	}
	
	private Pick pick(decisions, scenarioDescription, visiting, home) {
		def pick = new Pick()
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

	private void addConstraintValuesForScenario(scenario) {
		final Double FAVORED_BY_SCALING_FACTOR = 15.0
		decider.with {
			scenario.with {
				addConstraintValueForAlternative 'winLoss', percentWins(wins, losses), team
				addConstraintValueForAlternative 'field', percentWins(fieldWins, fieldLosses), team
				addConstraintValueForAlternative 'homeField', athome ? 1d : 0d, team
				addConstraintValueForAlternative 'healthiness', healthiness, team
				addConstraintValueForAlternative 'favoredBy', favoredBy * FAVORED_BY_SCALING_FACTOR, team
			}
		}
	}

	private double percentWins(wins, losses) {
		int total = wins + losses
		100d * wins / total
	}
}
