scalaVersion := "2.11.7"

scalaSource in Compile := baseDirectory.value / "src"
scalaSource in Test := baseDirectory.value / "test-src"

//Just to stop sbteclipse from creating extra dirs.
javaSource in Compile := baseDirectory.value / "src"
javaSource in Test := baseDirectory.value / "test-src"

initialCommands in console := "import icfp2015._"

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

//Disambiguate
libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % "2.11.7", "org.scala-lang.modules" %% "scala-xml" % "1.0.4")

//Avoid Eclipse overwriting SBT output.
EclipseKeys.eclipseOutput in ThisBuild := Some("bin")
