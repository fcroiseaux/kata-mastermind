/**
 * Created by IntelliJ IDEA.
 * User: antoine
 * Date: 12/03/2012
 * Time: 22:13
 */

package lu.ade.kata

// Le mastermind, avec le code secret et la liste des couleurs
class MasterMind(private val secret:List[String], val values:List[String]) {

  def secretLen=secret.size      // La longueur du code secret (nb de pions)
  def valuesLen=values.size      // Le nombre de couleurs possibles

  // Vérifie une proposition, en retournant le nombre bien placé et le nombre mal placé
  def check(proposition:List[String])={
    assert(proposition.size == secret.size)
    val (placed,incorrect)=proposition.zip(secret).partition(v=>v._1.equals(v._2))
    val rest=incorrect.map(_._2)
    (placed.size,incorrect.count(v=>rest.exists(_.equals(v._1))))
  }

  // Retourne le nombre total de possibilités pour ce mastermind
  def max=math.pow(valuesLen,secretLen).toInt

}

// Génère des combinaisons candidates pour la resolution du mastermind
class Generator(val mastermind:MasterMind){

  // Retourne une possibilité à un index donné (de 0 à mastermind.max-1)
  def propositionAt(index:Int):List[String]={
    for(i <- (0 until mastermind.secretLen).toList)
      yield mastermind.values(
        index%
          math.pow(mastermind.valuesLen,mastermind.secretLen-i).toInt/
            (math.pow(mastermind.valuesLen,mastermind.secretLen-i).toInt/mastermind.valuesLen)
      )
  }

  // Stream une à une chacune des propositions
  private def streamPropositions(index:Int):Stream[List[String]]=index match {
    case s:Int if s>=mastermind.max => Stream.empty
    case _ => Stream.cons(propositionAt(index),streamPropositions(index+1))
  }

  def streamAll=streamPropositions(0)
}

trait Solver{
  def mastermind:MasterMind
  def solve:Option[List[String]]
}

class BruteSolver(val mastermind:MasterMind) extends Solver {

  // Fin à la première qui match le code secret
  def solve=new Generator(mastermind).streamAll.find({c=>mastermind.check(c)==(mastermind.secretLen,0)})

}
