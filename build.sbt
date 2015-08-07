scalaVersion := "2.11.7"

scalaSource in Compile := baseDirectory.value / "src"
scalaSource in Test := baseDirectory.value / "test-src"

//Just to stop sbteclipse from creating extra dirs.
javaSource in Compile := baseDirectory.value / "src"
javaSource in Test := baseDirectory.value / "test-src"

initialCommands in console := "import icfp2015._"

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"
