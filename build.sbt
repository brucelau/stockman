name := "stockman"

organization := "com.mlh"

version := "0.1.0-SNAPSHOT"

homepage := Some(url("https://github.com/mhamrah/stockman"))

startYear := Some(2013)

scmInfo := Some(
  ScmInfo(
    url("https://github.com/mhamrah/stockman"),
    "scm:git:https://github.com/mhamrah/stockman.git",
    Some("scm:git:git@github.com:mhamrah/stockman.git")
  )
)

/* scala versions and options */
scalaVersion := "2.10.3"

crossScalaVersions := Seq(
/*  "2.9.3-RC1",
  "2.9.2",
  "2.9.1", "2.9.1-1",
  "2.9.0", "2.9.0-1",
  "2.8.0", "2.8.1", "2.8.2" */
)

// These options will be used for *all* versions.
scalacOptions ++= Seq(
  "-deprecation"
  ,"-unchecked"
  ,"-encoding", "UTF-8"
  ,"-target:jvm-1.7"
  ,"-Xlint"
  // "-optimise"   // this option will slow your build
)

scalacOptions ++= Seq(
  "-Yclosure-elim",
  "-Yinline"
)

// These language flags will be used only for 2.10.x.
// Uncomment those you need, or if you hate SIP-18, all of them.
scalacOptions <++= scalaVersion map { sv =>
  if (sv startsWith "2.10") List(
    "-Xverify"
    ,"-Ywarn-all"
    ,"-feature"
    ,"-language:postfixOps"
    // "-language:reflectiveCalls",
    // "-language:implicitConversions"
    // "-language:higherKinds",
    // "-language:existentials",
    // "-language:experimental.macros",
    // "-language:experimental.dynamics"
  )
  else Nil
}

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

val akka = "2.2.3"
val spray = "1.2-RC3"

/* dependencies */
libraryDependencies ++= Seq (
  "com.github.nscala-time" %% "nscala-time" % "0.6.0"
  // -- testing --
  , "org.scalatest" % "scalatest_2.10" % "2.0.RC2" % "test"
  , "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test"
  // -- Logging --
  ,"ch.qos.logback" % "logback-classic" % "1.0.13"
  ,"com.typesafe" %% "scalalogging-slf4j" % "1.0.1"
  // -- Akka --
  ,"com.typesafe.akka" %% "akka-testkit" % akka % "test"
  ,"com.typesafe.akka" %% "akka-actor" % akka
  ,"com.typesafe.akka" %% "akka-slf4j" % akka
  ,"com.typesafe.akka" %% "akka-cluster" % akka
  // -- Spray --
  ,"io.spray" % "spray-routing" % spray
  ,"io.spray" % "spray-client" % spray
  ,"io.spray" % "spray-testkit" % spray % "test"
  ,"io.spray" % "spray-json_2.10" % "1.2.5"
  //-- Json --
  //,"org.json4s" %% "json4s-native" % "3.2.2"
  //-- Cassandra --
  ,"com.datastax.cassandra" % "cassandra-driver-core" % "2.0.0-rc1" exclude("org.slf4j", "slf4j-log4j12")
  ,"org.xerial.snappy" % "snappy-java" % "1.0.5"
)

libraryDependencies += "org.fusesource" % "sigar" % "1.6.4" classifier("native")

/* you may need these repos */
resolvers ++= Seq(
  // Resolver.sonatypeRepo("snapshots")
  // Resolver.typesafeRepo("releases")
  //"spray repo" at "http://nightlies.spray.io",
  "spray" at "http://repo.spray.io/"
)

seq(Revolver.settings: _*)

atmosSettings

testOptions in Test += Tests.Setup(classLoader =>
  classLoader
    .loadClass("org.slf4j.LoggerFactory")
    .getMethod("getLogger", classLoader.loadClass("java.lang.String"))
    .invoke(null, "ROOT")
)

packageArchetype.java_application
