import akka.actor._
import scalax.io._

case class WriteMessage(output: String)

class WriterActor(fileName: String, delay: Int) extends Actor {

  val outFile = Resource.fromFile(fileName)

  def receive = {
    case WriteMessage(out) =>
      (1 to 10) foreach { i =>
        outFile.write(s"$out $i\n")
        println(s"$out $i")
        val sleep = (scala.util.Random.nextFloat * delay).toLong
        Thread.sleep(sleep)
      }
    case _ =>
      outFile.write("WHATTHEFUCK\n")
  }

  override def postStop { println(s"Actor for file $fileName has stopped")}

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println(s"$fileName preRestart called")
  }
}

/**
 * Created by abomm on 10/30/14.
 */
object ActorWriter {
  def main(args: Array[String]) {
    val system = ActorSystem("ActorWriterSystem")
    val first = system.actorOf(Props(new WriterActor("first.out",500)))
    val second = system.actorOf(Props(new WriterActor("second.out", 1000)))
    val third = system.actorOf(Props(new WriterActor("third.out", 750)))

    first ! WriteMessage("FIRST ONE")
    second ! WriteMessage("SECOND WOW")
    third ! WriteMessage("THIRD TIME!")

    Thread.sleep(2*1000)

    List(first,second,third) foreach(_ ! Kill)

    Thread.sleep(8 * 1000)

    system.shutdown()

  }
}
