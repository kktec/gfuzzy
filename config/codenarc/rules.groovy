ruleset {
	ruleset('rulesets/basic.xml')
	ruleset('rulesets/braces.xml') {
		'IfStatementBraces'  enabled:false
	}
	ruleset('rulesets/concurrency.xml')
	ruleset('rulesets/exceptions.xml')
	ruleset('rulesets/imports.xml')
	ruleset('rulesets/logging.xml')
	ruleset('rulesets/naming.xml') {
		'FactoryMethodName' enabled:false
	}
	ruleset('rulesets/unnecessary.xml') {
		'UnnecessaryGString' enabled:false
		'UnnecessaryObjectReferences' enabled:false
	}
	ruleset('rulesets/unused.xml')
}