/**
 * Created by IntelliJ IDEA.
 * User: antoine
 * Date: 13/03/12
 * Time: 21:27
 */

import lu.ade.kata._

object Runner {
  
  def main(args:Array[String])={
    val masterMind=new MasterMind(List("T","F","A","B","C"),List("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T"))
    val solver=args(0) match {
      case "akka" =>
        println("Use AkkaSolver with "+args(1)+" actor(s)...")
        new AkkaSolver(masterMind,args(1).toInt)
      case _ =>
        println("Use BruteSolver...")
        new BruteSolver(masterMind)
    }
    val start=System.currentTimeMillis()
    val result=solver.solve
    val duration=System.currentTimeMillis()-start
    println("Solved in "+duration/1000+" s.")
    println("Solution is "+result.get)
  }

}
