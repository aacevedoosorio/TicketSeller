enablePlugins(JavaServerAppPackaging)

name := "TickerSeller"

version := "0.1"

scalaVersion := "2.12.6"

organization := "com.aacevedo"

libraryDependencies := Seq(
  "com.typesafe.akka" %% "akka-actor" % Versions.akka,
  "com.typesafe.akka" %% "akka-stream" % Versions.akka,
  "com.typesafe.akka" %% "akka-testkit" % Versions.akka % Test,
  "com.typesafe.akka" %% "akka-http"   % Versions.akkaHttp,
  "com.typesafe.akka" %% "akka-http-spray-json" % Versions.akkaHttp,
"org.scalatest" %% "scalatest" % "3.0.5" % Test
)