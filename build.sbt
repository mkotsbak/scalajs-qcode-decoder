
val buildScalaVersion = "2.11.7"
val qcode_decoderVersion = "0.1.0"

enablePlugins(ScalaJSPlugin)

organization := "io.github.mkotsbak"
name := "scalajs-qcode-decoder"
version := "0.1.0-SNAPSHOT"
scalaVersion := buildScalaVersion

libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.2"
)

jsDependencies += "org.webjars.bower" % "qcode-decoder" % "0.1.0" / "qcode-decoder.min.js"

publishTo := Some("releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
pomExtra :=
  <url>https://github.com/mkotsbak/scalajs-qcode-decoder</url>
    <licenses>
      <license>
        <name>Apache-2.0</name>
        <url>https://www.apache.org/licenses/LICENSE-2.0.html</url>
      </license>
    </licenses>
    <scm>
      <url>git://github.com/mkotsbak/scalajs-qcode-decoder.git</url>
    </scm>
    <developers>
      <developer>
        <id>mkotsbak</id>
        <name>Marius B. Kotsbak</name>
        <url>https://github.com/mkotsbak</url>
      </developer>
    </developers>
