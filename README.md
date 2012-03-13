## Kata MasterMind en Scala

### Build & test
Une fois le projet cloné, lancer sbt
```sbt```

Dans la console SBT, pour lancer les test unitaires :
```test```

La commande va lancer une série de test sur l'application.

### Run

Pour tester l'algorithme de resolution, dans la console sbt :

* Lance la résolution avec l'agorithme "BruteForce" qui essaye toutes les combinaisons une à une :
 ```run brute```
	


* Lance la résolution avec le même algorithme, mais executé par des Actors Akka en parallèle. Le nombre d'actors (`4`) est paramètrable : ```run akka 4```
