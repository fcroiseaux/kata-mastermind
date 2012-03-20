/**
 * Created by IntelliJ IDEA.
 * User: antoine
 * Date: 12/03/2012
 * Time: 22:12
 */

import org.specs2.mutable._
import lu.ade.kata._

class MastermindTest extends Specification {

  val masterMind=new MasterMind(List("R","B","O","N"),List("R","B","O","N","V"))

  "MasterMind app " should {

    "reject invalid proposition length" in {
      masterMind.check(List("G","J","V")) must throwA[AssertionError]
    }

    "reject invalid combinaison" in {
      masterMind.check(List("G","J","V","M")) must equalTo(0,0)
    }
    
    "count placed items" in {
      masterMind.check(List("R","J","V","M")) must equalTo(1,0)
      masterMind.check(List("R","J","O","M")) must equalTo(2,0)
    }

    "count good items but not in place" in {
      masterMind.check(List("G","J","R","M")) must equalTo(0,1)
      masterMind.check(List("B","J","R","M")) must equalTo(0,2)
    }
    
    "count good and placed items" in {
      masterMind.check(List("R","O","T","T")) must equalTo(1,1)
      masterMind.check(List("R","O","O","O")) must equalTo(2,0)
    }
  }

  "Solver app " should {

    "brute solve mastermind" in {
      val secret = List("A","C","B", "A")
      val masterMind=new MasterMind(secret ,List("A","B","C","D","E","F","G"))
      val solution=new BruteSolver(masterMind).solve
      solution must beSome
      solution.get must equalTo(secret)
    }

    "wise solve mastermind" in {
      val secret = List("A","C","B", "A")
      val masterMind=new MasterMind(secret,List("A","B","C","D","E","F","G"))
      val solution=new WiseSolver(masterMind).solve
      solution must beSome
      solution.get must equalTo(secret)
    }
  }

}
