name := "akkaPractice"

version := "0.1"

scalaVersion := "2.13.1"

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-http
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.11"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.26" // or whatever the latest version is

libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11"