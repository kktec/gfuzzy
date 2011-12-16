package org.gfuzzy.specs

import org.concordion.integration.junit4.ConcordionRunner
import org.gfuzzy.test.FootballGamePicker
import org.gfuzzy.test.Picks
import org.gfuzzy.test.Scenario
import org.junit.runner.RunWith

@RunWith(ConcordionRunner)
class FootballGamePicksTest {

	Map visitingScenarios = [:]

	Map homeScenarios = [:]

	FootballGamePicker picker = new FootballGamePicker()
	
	FootballGamePicksTest() {
		picker.initRules()
		picker.initDeciderConstraints()
	}


	void addVisitingScenario(scenario, team, wins, losses, fieldWins, fieldLosses, healthiness, favoredBy) {
		visitingScenarios[scenario] = new Scenario(team, false, wins, losses, fieldWins, fieldLosses, healthiness, favoredBy)
	}

	void addHomeScenario(scenario, team, wins, losses, fieldWins, fieldLosses, healthiness, favoredBy) {
		homeScenarios[scenario] = new Scenario(team, true, wins, losses, fieldWins, fieldLosses, healthiness, favoredBy)
	}

	def picks() {
		visitingScenarios.collect { scenarioDescription, Scenario visiting ->
			Scenario home = homeScenarios[scenarioDescription]
			Picks picks = picker.picks(scenarioDescription, visiting, home)
			picks.asStrings(2)
		}
	}

	def rules() {
		picker.inferencer.stringifyRules()
	}
	
}
