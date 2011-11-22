gfuzzy is a simple Groovy library that provides an implementation of Fuxxy Logic,
see http://en.wikipedia.org/wiki/Fuzzy_logic

The project currently has 3 sub-projects, gfuzzy-core, which implements the library itself, gfuzzy-example, which shows how it can be used, and gfuzzy-swing to provide some user interface.

The library can be built and its tests run through the Gradle build system, see http://www.gradle.org/
See build.gradle in the main project for details. A Swing demo app (using Groovy SwingBuilder) that shows an example of the ErrorController can be run with the command "gradle run". Running "gradle distZip" will create a zip file containing all files needed to run the demo on Windows or Unix. A snapshot of this demo zip file is available in the GitHub downloads section.

Eclipse support is provided for use with the Groovy-Eclipse plugin. Eclipse users should set a path variable, GRADLE_M5_CACHE, to point to the cached artifacts in their %USER_HOME% directory, i.e. %USER_HOME%/.gradle/caches/artifacts-3.