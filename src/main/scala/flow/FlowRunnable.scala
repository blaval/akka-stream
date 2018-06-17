package flow

import akka.NotUsed
import akka.stream.scaladsl._
import flow.FlowRunnable.{example1, example2, example3, example4}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global._

object FlowRunnable extends App {

  import shared.Implicits._

  def example1(): Unit = {
    val source = Source(10 to 15).map { elem ⇒ println(elem); elem }
    source.runWith(Sink.ignore)
  }

  def example2(): Unit = {
    val source = Source.repeat(10).limit(6).map { elem ⇒ println(elem); elem }
    source.runWith(Sink.ignore)
  }

  def example3(): Unit = {
    def f: Future[List[Int]] = Future.successful((1 to 5).toList)

    def g(l: List[Int]): List[String] = l.map(_.toString * 2)

    Source(List(List(1, 2), List(1, 2)))
      //    .fromFuture(f)
      .mapConcat(g) // emits 5 elements of type Int
      .runForeach(println)

    Source(List(List(1, 2), List(1, 2)))
      //    .fromFuture(f)
      .map(g) // emits one element of type List[Int]
      .runForeach(println)
  }

  def example4(): Unit = {
    val MaximumDistinctWords = 3
    Source("Couper cette phrase".split(" ").toList)
      // split the words into separate streams first
      .groupBy(MaximumDistinctWords, identity)
      //transform each element to pair with number of words in it
      .map(_ -> 1)
      // add counting logic to the streams
      .reduce((l, r) ⇒ (l._1, l._2 + r._2))
      // get a stream of word counts
      .mergeSubstreams
      .runForeach(println)
  }

  val p: (Int, => Unit) => Future[Unit] = (idx, f) => Future {
    println(s"\nExample ${idx.toString}:"); f
  }

  //  p(1, example1())
  //  p(2, example2())
  //  p(3, example3())
  p(4, example4())
}
