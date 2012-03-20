/**
 * Created by IntelliJ IDEA.
 * User: antoine
 * Date: 12/03/2012
 * Time: 22:13
 */

package lu.ade.kata

import collection.parallel.mutable

// Le mastermind, avec le code secret et la liste des couleurs
class MasterMind(private val secret: List[String], val values: List[String]) {

  def secretLen = secret.size // La longueur du code secret (nb de pions)
  def valuesLen = values.size // Le nombre de couleurs possibles

  // Copare deux propositions, en retournant le nombre bien placé et le nombre mal placé
  def compare(prop1: List[String], prop2: List[String]) = {
    assert(prop1.size == prop2.size)
    val (placed, incorrect) = prop1.zip(prop2).partition(v => v._1.equals(v._2))
    val rest = incorrect.map(_._2)
    (placed.size, incorrect.count(v => rest.contains(v._1)))
  }

  def check(proposition: List[String]) = compare(proposition, secret)

  // Conversion d'un entier en une liste le représentant en base n
  def baseN(index: Int, base: Int): List[Int] = index match {
    case zero: Int if zero == 0 => List.fill(secretLen) {0}
    case _ => index % base :: baseN(index / base, base)
  }

  // Retourne une possibilité à un index donné (de 0 à mastermind.max-1)
  def propositionAt(index: Int): List[String] =
    baseN(index, valuesLen).take(secretLen).reverse.map { values }


  // Retourne le nombre total de possibilités pour ce mastermind
  def max = math.pow(valuesLen, secretLen).toInt

}

// Génère des combinaisons candidates pour la resolution du mastermind
class Generator(val mastermind: MasterMind) {


  // Stream une à une chacune des propositions
  private def streamPropositions(index: Int): Stream[List[String]] = index match {
    case s: Int if s >= mastermind.max => Stream.empty
    case _ => Stream.cons(mastermind.propositionAt(index), streamPropositions(index + 1))
  }

  def streamAll = streamPropositions(1)
}

trait Solver {
  def mastermind: MasterMind

  //retourne vrai si on fait la proposition, faux sinon
  def propose(combi:List[String]) : Boolean

  // Fin à la première qui match le code secret
  def solve = new Generator(mastermind).streamAll.find({
    c => propose(c) && mastermind.check(c) ==(mastermind.secretLen, 0)
  })


}

class BruteSolver(val mastermind: MasterMind) extends Solver {

  // On propose toutes les combinaisons
  def propose(combi : List[String]) =  {
    println("BRUTE : " + combi)
    true
  }

}
  
class WiseSolver(val mastermind: MasterMind) extends Solver {
  
  val previousProp = scala.collection.mutable.Map(mastermind.propositionAt(0) -> mastermind.check(mastermind.propositionAt(0)))


  //Vérifie si une combinaison est elligible comme proposition
  def propose(combi : List[String]) = {
    val found = previousProp.find( prop => mastermind.compare(combi, prop._1) != prop._2)
    if (found.isEmpty) {
      previousProp.put(combi, mastermind.check(combi))
      println("WISE : " + combi)
    }
    found.isEmpty
  }

}


