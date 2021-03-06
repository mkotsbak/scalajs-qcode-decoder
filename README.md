# scalajs-qcode-decoder
Scala.Js wrapper for [QRCodeDecoder JS library](http://cirocosta.github.io/qcode-decoder) for decoding QR codes.
  
#### How to use it

Add this library to project dependencies (until it is published somewhere, you need to download this project and run "sbt publish-local" first):

```sbt
libraryDependencies += "io.github.mkotsbak" %%% "scalajs-qcode-decoder" % "0.1.0-SNAPSHOT" withSources() withJavadoc()
```

Example usage:

```scala

val qrDecoder = new QCodeDecoder()
        
def decodeQr(): Unit = {
    val res = qrDecoder.decodeFromCameraOnce(cameraElement, keepStream = true)
    res.onFailure {
        case ex: JavaScriptException => println(s"Err ${ex.toString()}")
    }
    res.foreach { text =>
        println(text)
    }
}
```
