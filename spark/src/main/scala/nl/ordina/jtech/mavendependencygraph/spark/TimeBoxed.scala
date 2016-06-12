package nl.ordina.jtech.mavendependencygraph.spark

import scala.concurrent._
import scala.concurrent.duration._
import scala.util._
import scala.concurrent.ExecutionContext.Implicits._

object TimeBoxed {
  class TimeBoxedOperation[A](f: => A) {
    def get(implicit timeout: Duration) =
      Try(Await.result(Future(f), timeout)).get
    def recover(default: A)(implicit timeout: Duration) =
      Try(Await.result(Future(f), timeout)).getOrElse(default)
  }

  def apply[A](f: => A): TimeBoxedOperation[A] = new TimeBoxedOperation(f)
}
