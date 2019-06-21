val commons = Seq(
  version := "0.1",
  scalaVersion := "2.11.12",
  crossScalaVersions := Seq("2.11.12", "2.12.8", "2.13.0"))

lazy val impl = (project in file("impl"))
  .settings(commons, name := "ulid")

lazy val bench = (project in file("bench"))
  .dependsOn(impl)
  .enablePlugins(JmhPlugin)
  .settings(commons, name := "bench")

lazy val root = (project in file("."))
  .aggregate(impl)
  .settings(
    commons, 
    name := "root",
    crossScalaVersions := Nil,
    publish / skip := true,
    publishArtifact := false,
    aggregate in update := false)
  