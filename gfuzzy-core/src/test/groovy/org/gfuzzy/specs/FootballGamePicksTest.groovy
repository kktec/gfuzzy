package org.gfuzzy.specs

import org.concordion.integration.junit4.ConcordionRunner
import org.gfuzzy.inference.Inferencer
import org.gfuzzy.test.FootballGamePicker
import org.gfuzzy.test.Pick
import org.gfuzzy.test.Scenario
import org.junit.runner.RunWith

@RunWith(ConcordionRunner)
class FootballGamePicksTest {
	
	Map visitingScenarios = [:]

	Map homeScenarios = [:]
	
	void addVisitingScenario(scenario, team, wins, losses, fieldWins, fieldLosses, healthiness, favoredBy) {
		visitingScenarios[scenario] = new Scenario(team, false, wins, losses, fieldWins, fieldLosses, healthiness, favoredBy)
	}

	void addHomeScenario(scenario, team, wins, losses, fieldWins, fieldLosses, healthiness, favoredBy) {
		homeScenarios[scenario] = new Scenario(team, true, wins, losses, fieldWins, fieldLosses, healthiness, favoredBy)
	}

	List<Pick> picks() {
		def picks = []
		visitingScenarios.each { scenarioDescription, Scenario visiting ->
			FootballGamePicker picker = new FootballGamePicker()
			Scenario home = homeScenarios[scenarioDescription]
			picks << picker.pick(scenarioDescription, visiting, home)
		}
		picks
	}

	List<String> ruleStrings() {
		FootballGamePicker picker = new FootballGamePicker()
		Inferencer inferencer = new Inferencer(picker.win.name)
		inferencer.rules = picker.rules
		inferencer.stringify()
	}


}
