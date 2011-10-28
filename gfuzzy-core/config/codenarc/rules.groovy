ruleset {
	ruleset('rulesets/basic.xml')
	ruleset('rulesets/braces.xml')
	ruleset('rulesets/exceptions.xml')
	ruleset('rulesets/imports.xml')
	ruleset('rulesets/logging.xml') {
		'Println' enabled:false
		'PrintStackTrace' priority: 1
	}
	ruleset('rulesets/naming.xml') {
		'FieldName' finalRegex:'[a-z][a-zA-Z0-9]*'
	}
	ruleset('rulesets/unnecessary.xml') {
		'UnnecessaryGString' enabled:false
	}
	ruleset('rulesets/unused.xml')
}