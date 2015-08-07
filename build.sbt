scalaVersion := "2.11.7"

scalaSource in Compile := baseDirectory.value / "src"
scalaSource in Test := baseDirectory.value / "test-src"

//Just to stop sbteclipse from creating extra dirs.
javaSource in Compile := baseDirectory.value / "src"
javaSource in Test := baseDirectory.value / "test-src"
