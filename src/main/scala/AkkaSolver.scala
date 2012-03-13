/**
 * Created by IntelliJ IDEA.
 * User: antoine
 * Date: 13/03/12
 * Time: 21:45
 */

package lu.ade.kata

import akka.actor._
import akka.dispatch.Await
import akka.routing.RoundRobinRouter
import akka.util.Timeout
import akka.util.duration._
import akka.pattern.ask

case class Work(start:Int, nb:Int, replyTo:ActorRef)
case class Found(code:List[String])
case class Solve()

class Worker(val mastermind:MasterMind) extends Actor {

  val generator=new Generator(mastermind)

  def receive = {
    case Work(start, nb, replyTo) =>
      for(index <- (0 until nb)){
        val prop=generator.propositionAt(index+start)
        if(mastermind.check(prop)==(mastermind.secretLen,0))
          replyTo ! Found(prop)
      }
  }
}

class AkkaMaster(val mastermind:MasterMind, val nbWorkers:Int) extends Actor{

  val workerRouter = context.actorOf(Props(new Worker(mastermind)).withRouter(RoundRobinRouter(nbWorkers)), name = "workerRouter")
  val nbEltsForEach=mastermind.max/nbWorkers

  def receive = {
    case Solve =>
      for (workerNum <- 0 until nbWorkers) workerRouter ! Work(workerNum*nbEltsForEach,nbEltsForEach,sender)
  }

}

class AkkaSolver(val mastermind:MasterMind, val nbWorkers:Int) extends Solver {
  val system = ActorSystem("MasterMindSystem")
  def solve={
    val master=system.actorOf(Props(new AkkaMaster(mastermind,nbWorkers)),name = "master")
    implicit val timeout = Timeout(300 seconds)
    val future=Await.result(master ? Solve,timeout.duration).asInstanceOf[Found]
    system.shutdown()
    Some(future.code)
  }
}
