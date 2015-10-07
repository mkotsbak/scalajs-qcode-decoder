package io.github.mkotsbak.qcodeDecoder

import org.scalajs.dom.MediaStream
import org.scalajs.dom.raw.{HTMLImageElement, HTMLVideoElement}

import scala.concurrent.{ExecutionContext, Promise, Future}
import scala.scalajs.js
import scala.scalajs.js.JavaScriptException
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.|
import scala.util.{Failure, Success, Try}

@JSName("QCodeDecoder")
@js.native
protected[qcodeDecoder] class QCodeDecoderNative extends js.Object {
    type cb = js.Function2[js.JavaScriptException | js.UndefOr[js.Error], js.UndefOr[String], Unit]

    def _captureToCanvas(videoElement: HTMLVideoElement, callback: cb, once: Boolean): Unit = js.native
    def decodeFromCamera(videoElement: HTMLVideoElement, callback: cb, once: Boolean): Unit = js.native
    def decodeFromVideo(videoElement: HTMLVideoElement, callback: cb, once: Boolean): Unit = js.native
    def decodeFromImage(imageElement: HTMLImageElement, callback: cb): Unit = js.native
    def stop(): Unit = js.native

    var stream: MediaStream = js.native
}

class QCodeDecoder {
    val qCodeDecoderNative = new QCodeDecoderNative()

    private def onceCallback(p: Promise[String]): (js.JavaScriptException | js.UndefOr[js.Error], js.UndefOr[String]) => Unit = {
        (err, res) => (err, res.toOption) match {
            case (_, Some(s)) => p.success(s)
            case (ex: js.JavaScriptException, None) => p.failure(ex)
            case _ =>
                if (p.isCompleted) println("Extra error: " + err.toString)
                else println("Error: " + err.toString)
        }
    }

    def decodeFromCameraOnce(videoElement: HTMLVideoElement, keepStream: Boolean = false)(implicit ec: ExecutionContext): Future[String] = {
        val p = Promise[String]

        if (qCodeDecoderNative.stream == null) qCodeDecoderNative.decodeFromCamera(videoElement, onceCallback(p), once = true)
        else qCodeDecoderNative._captureToCanvas(videoElement, onceCallback(p), once = true)

        if (!keepStream) {
            p.future.onComplete( _ => stop() )
        }

        p.future
    }

    def decodeFromVideoOnce(videoElement: HTMLVideoElement, keepStream: Boolean = false): Future[String] = {
        val p = Promise[String]
        qCodeDecoderNative.decodeFromVideo(videoElement, onceCallback(p), once = true)
        p.future
    }

    def decodeFromImage(imageElement: HTMLImageElement): Future[String] = {
        val p = Promise[String]
        qCodeDecoderNative.decodeFromImage(imageElement, onceCallback(p))
        p.future
    }

    private def multipleCallback(callback: Try[String] => Unit): (js.JavaScriptException | js.UndefOr[js.Error], js.UndefOr[String]) => Unit = {
        (err, res) => (err, res.toOption) match {
            case (_, Some(s)) => callback(Success(s))
            case (ex: js.JavaScriptException, None) => callback(Failure(ex))
            case _ => callback(Failure(JavaScriptException(err)))
        }
    }

    def decodeFromCamera(videoElement: HTMLVideoElement, callback: Try[String] => Unit): Unit = {
        qCodeDecoderNative.decodeFromCamera(videoElement, multipleCallback(callback), once = false)
    }

    def decodeFromVideo(videoElement: HTMLVideoElement, callback: Try[String] => Unit): Unit = {
        qCodeDecoderNative.decodeFromVideo(videoElement, multipleCallback(callback), once = false)
    }

    def stop(): Unit = qCodeDecoderNative.stop()
}
