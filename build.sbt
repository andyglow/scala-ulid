import xerial.sbt.Sonatype._
import ReleaseTransformations._
import scala.sys.process._

// https://github.com/xerial/sbt-sonatype/issues/71
publishTo in ThisBuild := sonatypePublishTo.value

val commons = Seq(
  
  organization := "com.github.andyglow",
  
  homepage := Some(new URL("http://github.com/andyglow/scala-ulid")),
  
  startYear := Some(2019),
  
  organizationName := "andyglow",
  
  scalaVersion := "2.11.12",
  
  crossScalaVersions := Seq("2.11.12", "2.12.8", "2.13.0"),
  
  scalacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation",
    //  "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Xfuture"),

  scalacOptions in (Compile,doc) ++= Seq(
    "-groups",
    "-implicits",
    "-no-link-warnings"),

  licenses := Seq(("LGPL-3.0", url("http://opensource.org/licenses/LGPL-3.0"))),

  sonatypeProfileName := "com.github.andyglow",

  publishMavenStyle := true,

  sonatypeProjectHosting := Some(
    GitHubHosting(
      "andyglow",
      "scala-ulid",
      "andyglow@gmail.com")),

  scmInfo := Some(
    ScmInfo(
      url("https://github.com/andyglow/scala-ulid"),
      "scm:git@github.com:andyglow/scala-ulid.git")),

  developers := List(
    Developer(
      id    = "andyglow",
      name  = "Andriy Onyshchuk",
      email = "andyglow@gmail.com",
      url   = url("https://ua.linkedin.com/in/andyglow"))),

  releaseCrossBuild := true,

  releasePublishArtifactsAction := PgpKeys.publishSigned.value,

  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
    pushChanges)
)

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
