gfuzzy is a simple Groovy library that provides an implementation of Fuxxy Logic,
see http://en.wikipedia.org/wiki/Fuzzy_logic

The project currently has 3 sub-projects, gfuzzy-core, which implements the library itself, gfuzzy-example, which shows how it can be used, and gfuzzy-swing to provide some user interface.

The library can be built and its tests run through the Gradle build system, see http://www.gradle.org/
See build.gradle in the main project for details.

A Swing demo app (using Groovy SwingBuilder) that shows an example of the ErrorController can be run with the command "gradle run". Running "gradle distZip" will create a zip file containing all files needed to run the demo on Windows or Unix.
A snapshot of this demo zip file is available in the GitHub downloads section. To see how the controller behaves, tweak the Input and Setpoint sliders and press the the Update Controller button.

There are also some Acceptance Tests that provide some living user documentation via Concordion, see http://http://concordion.org/.

NEW!!!
======
1 of these tests shows the use of the Decider class which can be to make business type decisions considering a number of fuzzy factors.
This test demonstrates how to make a selection of a winner of an American style Football game based upon various statistics and factors.

The Decider takes an Inferencer with its FuzzySetDefinition to provide ratings on the configured alternatives using various fuzzy constraints
along with data about how those alternatives perform against those constraints.


Eclipse support is provided for use with the Groovy-Eclipse plugin. 

NOTE: a recent change
Eclipse users should set a path variable, GRADLE_USER_HOME, to point to the .gradle in their %USER_HOME% directory, i.e. %USER_HOME%/.gradle or ~/.gradle

The project depends upon Java6.

