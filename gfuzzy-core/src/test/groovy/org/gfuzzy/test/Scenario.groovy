package org.gfuzzy.test

class Scenario {

	String team
	boolean athome
	int wins, losses, fieldWins, fieldLosses 
	double healthiness, favoredBy 

	Scenario(team, athome, wins, losses, fieldWins, fieldLosses, healthiness, favoredBy) {
		this.team = team
		this.athome = athome
		this.wins = Integer.parseInt(wins)
		this.losses = Integer.parseInt(losses)
		this.fieldWins = Integer.parseInt(fieldWins)
		this.fieldLosses = Integer.parseInt(fieldLosses)
		this.healthiness = Double.parseDouble(healthiness)
		this.favoredBy = Double.parseDouble(favoredBy)
	}
	
	String toString() {
		"$team $athome"
	}
}
