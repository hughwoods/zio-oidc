ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "zio-oidc",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio-http" % "3.0.0-RC4",
      "dev.zio" %% "zio" % "2.1-RC1",
      "dev.zio" %% "zio-streams" % "2.1-RC1",
      "dev.zio" %% "zio-test" % "2.1-RC1" % Test,
      "dev.zio" %% "zio-test-sbt" % "2.1-RC1" % Test,
      "dev.zio" %% "zio-test-magnolia" % "2.1-RC1" % Test
    )
  )