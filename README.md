Projet Netbeans de génération de grilles de sudoku.
L'exécutable se trouve dans le dossier dist.

L'application propose une génération de grille de sudoku à imprimer,
ainsi qu'un solver de grille qui permet également d'évaluer la difficulté 
d'une grille.

Le calcul de la difficulté est effectué en se basant sur le nombre de 
techniques à employer pour résoudre la grille.
Plus d'information sur :   GNU nano 4.6                       README.md                        Modified
Projet Netbeans de génération de grilles de sudoku.
L'exécutable se trouve dans le dossier dist.

L'application propose une génération de grille de sudoku à imprimer,
ainsi qu'un solver de grille qui permet également d'évaluer la difficulté
d'une grille.

Le calcul de la difficulté est effectué en se basant sur le nombre de
techniques à employer pour résoudre la grille, plus d'information sur :
https://web.imt-atlantique.fr/x-info/jussien/publications/laburthe-JFPC06.pdf
Pour l'instant la difficulté maximal évaluée se limite à difficile.

L'interface graphique du projet est basée sur javafx.

Lors du lancement de l'application, afin d'optimiser les temps de générations 
de grilles, un générateur de grille est lancée (désactivable dans les 
paramètres). Cela est dû à la complexité de générer une grille d'une 
difficulté donnée. Pour cela, il faut générer des grilles jusqu'à n'avoir 
qu'une solution pour que cette dernière soit valide. Puis, évaluer la 
difficulté et si cela ne correspond pas, recommencer.
Le générateur stock ainsi les grilles associée à leur difficulté en 
permanence afin d'avoir une grille disponible pour chaque difficulté plus 
rapidement.

L'I18N est également mis en place avec 2 langues disponibles pour l'instant:
le français et l'anglais.
