package nl.ordina.jtech.mavendependencygraph.spark

import org.scalatest._

import scala.concurrent.TimeoutException
import scala.concurrent.duration._

class TimeBoxedSpec extends FunSuite {
  implicit val timeout = 10 milliseconds
  val durationThatTimesOut = 20L

  test("TimeBoxedOperations that do not exceed their duration should just return the result") {
    val result = TimeBoxed {
      true
    }.get(1 seconds)
    assert(result)
  }

  test("TimeBoxedOperations that exceed their duration should fail with a timeout") {
    intercept[TimeoutException](TimeBoxed {
      Thread.sleep(durationThatTimesOut)
    }.get)
  }

  test("TimeBoxedOperations that exceed their duration should be able to fallback to a default value") {
    println(timeout)
    val result = TimeBoxed {
      Thread.sleep(durationThatTimesOut)
      false
    } recover {
      true
    }

    assert(result)
  }
}
