val commons = Seq(
  version := "0.1",
  scalaVersion := "2.11.12",
  crossScalaVersions := Seq("2.11.13", "2.12.8"))

lazy val impl = (project in file("impl"))
  .settings(commons, name := "ulid")

lazy val bench = (project in file("bench"))
  .dependsOn(impl)
  .enablePlugins(JmhPlugin)
  .settings(commons, name := "bench")

lazy val root = (project in file("."))
  .settings(commons, name := "root")
  .aggregate(bench, impl)