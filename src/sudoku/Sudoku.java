package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Sudoku avec generateur, solveur de grille 9*9 et évaluateur de difficulté.
 * @author Johan Gousset
 */
public class Sudoku {
    /**
     * Nombre de blancs utilise pour creer une grille de difficulte tres facile, 
     * a noter que la difficulte d'une grille ne depend pas uniquement du nombre de 
     * blancs, une grille de meme difficulte n'aura pas toujours le meme nombre de blancs.
     */
    public static int NB_WHITES_VERYEASY = 42;
    /**
     * Nombre de blancs utilise pour creer une grille de difficulte facile, 
     * a noter que la difficulte d'une grille ne depend pas uniquement du nombre de 
     * blancs, une grille de meme difficulte n'aura pas toujours le meme nombre de blancs.
     */
    public static int NB_WHITES_EASY = 44;
    /**
     * Nombre de blancs utilise pour creer une grille de difficulte moyenne, 
     * a noter que la difficulte d'une grille ne depend pas uniquement du nombre de 
     * blancs, une grille de meme difficulte n'aura pas toujours le meme nombre de blancs.
     */
    public static int NB_WHITES_MEDIUM = 46;
    /**
     * Nombre de blancs utilise pour creer une grille de difficulte difficile, 
     * a noter que la difficulte d'une grille ne depend pas uniquement du nombre de 
     * blancs, une grille de meme difficulte n'aura pas toujours le meme nombre de blancs.
     */
    public static int NB_WHITES_DIFFICULT = 48;
    /**
     * Nombre de blancs utilise pour creer une grille de difficulte extreme, 
     * a noter que la difficulte d'une grille ne depend pas uniquement du nombre de 
     * blancs, une grille de meme difficulte n'aura pas toujours le meme nombre de blancs.
     */
    public static int NB_WHITES_HARDCORE = 50;
    /**
     * Exemple de grille tres facile.
     */
    public static int[][] VERY_EASY_GRID_EXAMPLE = 
                        {{1,0,0,8,3,0,0,0,2},
                        {5,7,0,0,0,1,0,0,0},
                        {0,0,0,5,0,9,0,6,4},
                        {7,0,4,0,0,8,5,9,0},
                        {0,0,3,0,1,0,4,0,0},
                        {0,5,1,4,0,0,3,0,6},
                        {3,6,0,7,0,4,0,0,0},
                        {0,0,0,6,0,0,0,7,9},
                        {8,0,0,0,5,2,0,0,3}};
    /**
     * Exemple de grille facile.
     */
    public static int[][] EASY_GRID_EXAMPLE =
                        {{0,0,6,0,3,0,7,0,8},
                        {0,3,0,0,0,0,0,0,1},
                        {2,0,0,0,0,0,6,0,0},
                        {1,0,0,3,5,0,0,0,6},
                        {0,7,9,0,4,0,1,5,0},
                        {5,0,0,0,1,7,0,0,4},
                        {0,0,2,0,0,0,0,0,7},
                        {6,0,0,0,0,0,0,8,0},
                        {4,0,7,0,6,0,2,0,0}};
    /**
     * Exemple de grille medium.
     */
    public static int[][] MEDIUM_GRID_EXAMPLE =
                        {{0,0,9,0,3,0,6,0,0},
                        {0,3,6,0,1,4,0,8,9},
                        {1,0,0,8,6,9,0,3,5},
                        {0,9,0,0,0,0,8,0,0},
                        {0,1,0,0,0,0,0,9,0},
                        {0,6,8,0,9,0,1,7,0},
                        {6,0,1,9,0,3,0,0,2},
                        {9,7,2,6,4,0,3,0,0},
                        {0,0,3,0,2,0,9,0,0}};
    /**
     * Exemple de grille difficile.
     */
    public static int[][] DIFFICULT_GRID_EXAMPLE =
                        {{0,4,0,1,0,0,0,0,0},
                        {0,0,3,5,0,0,0,1,9},
                        {0,0,0,0,0,6,0,0,3},
                        {0,0,7,0,0,5,0,0,8},
                        {0,8,1,0,0,0,9,6,0},
                        {9,0,0,2,0,0,7,0,0},
                        {6,0,0,9,0,0,0,0,0},
                        {8,1,0,0,0,2,4,0,0},
                        {0,0,0,0,0,4,0,9,0}};
    
    /**
     * Resoud une grille de sudoku.
     * @param grid_ la grille a resoudre
     * @return la grille de sudoku resolue
     */
    public static int[][] solver(int[][] grid_) {
        return solver(grid_, 0);
    }
    
    /**
     * Resoud une grille de sudoku a partir d'un indice donne.
     * @param grid_ la grille a resoudre
     * @param indice_ indice de depart pour resoudre la grille
     * @return la grille de sudoku resolue
     */
    public static int[][] solver(int[][] grid_, int indice_) {
        if (!isValidGrid(grid_)) return grid_;
        int x = getCoord(indice_)._1;
        int y = getCoord(indice_)._2;
        if (x==9 && y==0) return grid_;
        if (grid_[x][y] != 0) return solver(grid_, indice_+1);
        HashSet<Integer> aEssayer = getNumbers(grid_.length);
        HashSet<Integer> dejaPris = nombreDejaPris(grid_, x, y);
        aEssayer.removeAll(dejaPris);
        if (aEssayer.isEmpty()) return null; //pas de solution
        for (int nb : aEssayer) {
            int newTab[][] = cloneGrid(grid_);
            newTab[x][y] = nb;
            int result[][];
            if ((result = solver(newTab, indice_+1)) != null) {
                return result;
            }
        }
        //pas de solution
        return null;
    }
    
    /**
     * Clone la grille donnee.
     * @param grid_ grille a cloner
     * @return un clone de la grille
     */
    private static int[][] cloneGrid(int[][] grid_) {
        int newGrid[][] = new int[grid_.length][];
        for(int i = 0; i < grid_.length; i++) newGrid[i] = grid_[i].clone();
        return newGrid;
    }
    
    /**
     * Resoud une grille de sudoku a partir d'un indice donne et retourne toutes les solutions possibles.
     * @param grid_ la grille a resoudre
     * @return les grilles de solution possibles
     */
    public static HashSet<int[][]> findAllSolutions(int[][] grid_) {
        return findAllSolutions(grid_, 0);
    }
    
    /**
     * Resoud une grille de sudoku a partir d'un indice donne et retourne toutes les solutions possibles.
     * @param grid_ la grille a resoudre
     * @param indice_ indice de depart pour resoudre la grille
     * @return les grilles de solution possibles
     */
    public static HashSet<int[][]> findAllSolutions(int[][] grid_, int indice_) {
        HashSet<int[][]> results = new HashSet<>();
        if (!isValidGrid(grid_)) return results;
        int x = getCoord(indice_)._1;
        int y = getCoord(indice_)._2;
        if (x==9 && y==0) {
            results.add(grid_);
            return results;
        }
        if (grid_[x][y] != 0) return findAllSolutions(grid_, indice_+1);
        HashSet<Integer> aEssayer = getNumbers(grid_.length);
        HashSet<Integer> dejaPris = nombreDejaPris(grid_, x, y);
        aEssayer.removeAll(dejaPris);
        for (int nb : aEssayer) {
            int newTab[][] = cloneGrid(grid_);
            newTab[x][y] = nb;
            results.addAll(findAllSolutions(newTab, indice_+1));
        }
        return results;
    }
    
    /**
     * Retourne les coordonnees x et y correspondant a l'indice donnee d'une grille de sudoku.
     * @param indice_ indice de la position courante dans la grille
     * @return les coordonnees x et y correspondant a l'indice donnee
     */
    private static Tuple2<Integer,Integer> getCoord(int indice_) {
        return new Tuple2(indice_ / 9, indice_ % 9);
    }
    
    /**
     * Retourne un set des nombres deja pris vis-a-vis de la case indiquee.
     * @param grid_ grille
     * @param x_ ligne de la case
     * @param y_ colonne de la case
     * @return un set des nombres deja pris vis-a-vis de la case indiquee
     */
    private static HashSet<Integer> nombreDejaPris(int[][] grid_, int x_, int y_) {
        HashSet<Integer> result = new HashSet<>();
        for (int i=0; i<9; i++) {
            result.add(grid_[x_][i]);
            result.add(grid_[i][y_]);
            result.add(grid_[(x_/3)*3 + i/3][(y_ / 3)*3 + i%3]);
        }
        return result;
    }
    
    /**
     * Retourne un set des nombres deja pris au sein du bloc donne.
     * @param grid_ grille
     * @param x_ ligne du bloc
     * @param y_ colonne du bloc
     * @return un set des nombres deja pris au sein du bloc donne
     */
    private static HashSet<Integer> nombreDejaPrisBloc(int[][] grid_, int x_, int y_) {
        HashSet<Integer> result = new HashSet<>();
        for (int i=0; i<9; i++) {
            result.add(grid_[(x_*3) + i/3][(y_*3) + i%3]);
        }
        return result;
    }
    
    /**
     * Retourne un set des nombres deja pris au sein de la ligne donnee.
     * @param grid_ grille
     * @param line_ numero de la ligne
     * @return un set des nombres deja pris au sein de la ligne donnee
     */
    private static HashSet<Integer> nombreDejaPrisLigne(int[][] grid_, int line_) {
        HashSet<Integer> result = new HashSet<>();
        for (int i=0; i<9; i++) {
            result.add(grid_[line_][i]);
        }
        return result;
    }
    
    /**
     * Retourne un set des nombres deja pris au sein de la colonne donnee.
     * @param grid_ grille
     * @param column_ numero de la colonne
     * @return un set des nombres deja pris au sein de la colonne donnee
     */
    private static HashSet<Integer> nombreDejaPrisColonne(int[][] grid_, int column_) {
        HashSet<Integer> result = new HashSet<>();
        for (int i=0; i<9; i++) {
            result.add(grid_[i][column_]);
        }
        return result;
    }
    
    /**
     * Genere une grille de sudoku et taille 9*9 avec le nombre de blancs correspondant a la difficulte medium.
     * @return une grille de sudoku et taille 9*9 avec  avec le nombre de blancs correspondant a la difficulte medium.
     */
    public static int[][] generateGrid() {
        return generateGrid(Difficulty.MEDIUM, 9);
    }
    
    /**
     * Genere une grille de sudoku et taille 9*9 avec un nombre de blancs 
     * correspondant a la difficulte choisie.
     * @param difficulty_ difficulté du sudoku (en nombre de blancs seulement)
     * @return une grille de sudoku et taille 9*9 avec un nombre de blancs correspondant a la difficulte choisie
     */
    public static int[][] generateGrid(Difficulty difficulty_) {
        return generateGrid(difficulty_, 9);
    }
    
    /**
     * Genere une grille de sudoku de taille size_*size_ avec un nombre de blancs 
     * correspondant a la difficulte medium.
     * @param size_ taille de la grille (côté)
     * @return une grille de sudoku et taille 9*9 avec un nombre de blancs correspondant a la difficulte choisie
     * @deprecated le sudoku ne supporte pour l'instant que des grilles de 9*9,
     * preferer l'utilisation des méthodes avec taille par defaut
     */
    @Deprecated
    public static int[][] generateGrid(int size_) {
        return generateGrid(Difficulty.MEDIUM, size_);
    }
    
    /**
     * Genere une grille de sudoku et taille size_*size_ avec un nombre de blancs 
     * correspondant a la difficulte choisie.
     * @param difficulty_ difficulté du sudoku (en nombre de blancs seulement)
     * @param size_ taille de la grille (côté)
     * @return une grille de sudoku et taille size_*size_ avec le nombre de blancs correspondant a la difficulté choisie
     */
    public static int[][] generateGrid(Difficulty difficulty_, int size_) {
        if (size_ != 9) {
              System.out.println(size_ + " not implemented yet !");
              return null;
        }
        ArrayList<Integer> lineNumbers = new ArrayList<>();
        for (int i=1; i<=9; i++) {
            lineNumbers.add(i);
        }
        Collections.shuffle(lineNumbers);
        int[][] result = emptyTheGrid(fillXY(new int[size_][size_], 0, lineNumbers), difficulty_);
        if (findAllSolutions(result).size() == 1) return result;
        return generateGrid(difficulty_, size_); //tant qu'il y a plus d'une solution crée d'autres grilles
    }
    
    private static int[][] fillXY(int[][] grid_, int indice_, ArrayList<Integer> lineNumbers_) {
        int x = getCoord(indice_)._1;
        int y = getCoord(indice_)._2;
        if (x==9 && y==0) return grid_; //terminé
        HashSet<Integer> dejaPris = new HashSet(nombreDejaPris(grid_, x, y));
        for (int number : lineNumbers_) {
          if (!dejaPris.contains(number)) {
            grid_[x][y] = number;
            int result[][];
            if (y == grid_.length-1) { //si on passe à la ligne suivante
                ArrayList<Integer> newLineNumbers = new ArrayList<>();
                for (int i=1; i<=9; i++) {
                    newLineNumbers.add(i);
                }
                Collections.shuffle(lineNumbers_);
               if ((result = fillXY(grid_, indice_+1, newLineNumbers)) != null) {
                   return result;
               }
            } else {
                ArrayList<Integer> newLineNumbers = (ArrayList<Integer>)lineNumbers_.clone();
                newLineNumbers.remove(new Integer(number));
                if ((result = fillXY(grid_, indice_+1, newLineNumbers)) != null) {
                   return result;
               }
            }
            grid_[x][y] = 0;
          }
        }
        //impossible de générer la grille (ne doit pas arriver)
        return null;
    }
    
    /**
     * Retourne la liste des nombres possibles dans un sudoku de la taille donnee.
     * @param size_ taille du sudoku
     * @return la liste des nombres possibles dans un sudoku de la taille donnee
     */
    private static HashSet<Integer> getNumbers(int size_) {
        HashSet<Integer> numbers = new HashSet<>();
        for (int i=1; i<=size_; i++) numbers.add(i);
        return numbers;
    }
    
    /**
     * Vide partiellement la grille en remplaçant un nombre de case qui depend 
     * de la difficulte choisie par des blancs, la grille doit etre pleine.
     * @param grid_ la grille a vider
     * @param difficulty_ la difficulte correspondant au nombre de blancs/trous souhaites
     * @return la grille partiellement videe
     */
    private static int[][] emptyTheGrid(int[][] grid_, Difficulty difficulty_) {
        if (!isFull(grid_)) return grid_; //si la grille n'est pas pleine ne fait rien
        int[][] result = cloneGrid(grid_);
        int nbWhites;
        switch (difficulty_) {
            case VERYEASY:
                nbWhites = NB_WHITES_VERYEASY;
                break;
            case EASY:
                nbWhites = NB_WHITES_EASY;
                break;
            case DIFFICULT:
                nbWhites = NB_WHITES_DIFFICULT;
                break;
            /**TODOHARDCORE
            case HARDCORE:
                nbWhites = NB_WHITES_HARDCORE;
                break;*/
            default:
                nbWhites = NB_WHITES_MEDIUM;
        }
        Random random = new Random();
        for (int i=0; i<nbWhites; i++) {
            int x = random.nextInt(9);
            int y = random.nextInt(9);
            int iterMax = 300;
            while ((result[x][y] == 0) && (iterMax-->0)) {
                x = random.nextInt(9);
                y = random.nextInt(9);
            }
            result[x][y] = 0;
        }
        return result;
    }
    
    public static boolean isValidGrid(int[][] grid_) {
        for (int i=0; i<grid_.length; i++) {
            for (int j=0; j<grid_[0].length; j++) {
                int num = grid_[i][j];
                if (num != 0) { //si 0 valide
                    grid_[i][j] = 0; //vide la case
                    if ((num<0) || (num>9)) return false;
                    if(nombreDejaPris(grid_, i, j).contains(num)) return false;
                    grid_[i][j] = num; //remet le chiffre dans la case s'il est correct
                }
            }
        }
        return true;
    }
    
    /**
     * Retourne le nombre de trous (blancs/0) dans la grille donnee.
     * @param grid_ grille a  tester
     * @return le nombre de trous (blancs/0) dans la grille
     */
    public static int getNbWhites(int[][] grid_) {
        int nbWhites = 0;
        for (int i=0; i<grid_.length; i++) {
            for (int j=0; j<grid_[0].length; j++) {
                if (grid_[i][j] == 0) nbWhites++;
            }
        }
        return nbWhites;
    }
    
    /**
     * Retourne true si la grille est pleine (pas de trou/0), false sinon.
     * @param grid_ grille a tester
     * @return true si la grille est pleine (pas de trou/0), false sinon
     */
    private static boolean isFull(int[][] grid_) {
        return getNbWhites(grid_)==0;
    }
    
    /**
     * Retourne la difficulté d'une grille, la difficulte se base principalement sur 
     * le nombre de techniques a utiliser pour resoudre la grille.
     * @see <a href="https://web.imt-atlantique.fr/x-info/jussien/publications/laburthe-JFPC06.pdf">Evaluer la difficulte d’une grille de sudoku a l’aide d’un modele contraintes</a>
     * @param grid_ la grille a tester
     * @return la difficulté de la grille
     */
    public static Difficulty getDifficulty(int[][] grid_) {
        int[][] grid = cloneGrid(grid_);
        if (isVeryEasy(grid)) return Difficulty.VERYEASY;
        else if (isEasy(grid)) return Difficulty.EASY;
        else if (isMedium(grid)) return Difficulty.MEDIUM;
        return Difficulty.DIFFICULT;
        //TODOreturn Difficulty.HARDCORE;*/
    }
    /**
     * Applique la regle de Single Candidate uniquement pour resoudre la grille, 
     * si la grille est solvable retourne true, false sinon, MODIFIE LA GRILLE DONNEE !
     * @param grid grille a tester
     * @return true si la grille est tres facile, false sinon, applique la regle Single Candidate
     */
    @SuppressWarnings("empty-statement")
    private static boolean isVeryEasy(int[][] grid) {
        //grid = VERY_EASY_GRID_EXAMPLE;
        while(applySingleCandidate(grid));
        return isFull(grid);
    }
    
    /**
     * Applique la regle de SingleCandidate uniquement pour resoudre la grille, 
     * si la grille est solvable retourne true, false sinon, MODIFIE LA GRILLE DONNEE !
     * @param grid grille a tester
     * @return true si la grille est facile, false sinon, applique la regle SingleCandidate
     */
    @SuppressWarnings("empty-statement")
    private static boolean isEasy(int[][] grid) {
        //grid = EASY_GRID_EXAMPLE;
        while(applySinglePosition(grid) || applySingleCandidate(grid));
        return isFull(grid);
    }
    
    /**
     * Applique les regle Candidate Lines, Multiple Lines et Single Candidate pour resoudre 
     * la grille, si la grille est solvable retourne true, false sinon, MODIFIE LA GRILLE DONNEE !
     * @param grid grille a tester
     * @return true si la grille est medium, false sinon, applique les regles 
     * Candidate Lines, Multiple Lines et Single Candidate
     */
    @SuppressWarnings("empty-statement")
    private static boolean isMedium(int[][] grid) {
        //grid = MEDIUM_GRID_EXAMPLE;
        HashSet<Integer>[][] excludeNumbersGrid = new HashSet[grid.length][grid[0].length];
        for (int i=0; i<excludeNumbersGrid.length; i++) {
            for (int j=0; j<excludeNumbersGrid[0].length; j++) {
                excludeNumbersGrid[i][j] = new HashSet<>();
            }
        }
        while (applySinglePosition(grid, excludeNumbersGrid)
            || applySingleCandidate(grid, excludeNumbersGrid)
            || applyCandidateAndMultipleLines(grid, excludeNumbersGrid));
        return isFull(grid);
    }
    
    /**
     * Applique la technique Single Position sur la grille donnee.
     * @param grid_ la grille
     * @return true si la grille a ete mise a jour, false sinon
     */
    private static boolean applySinglePosition(int[][] grid_) {
        HashSet<Integer>[][] excludeNumbersGrid = new HashSet[grid_.length][grid_[0].length];
        for (int i=0; i<excludeNumbersGrid.length; i++) {
            for (int j=0; j<excludeNumbersGrid[0].length; j++) {
                excludeNumbersGrid[i][j] = new HashSet<>();
            }
        }
        return applySinglePosition(grid_, excludeNumbersGrid);
    }
    
    /**
     * Applique la technique Single Position sur la grille donnee.
     * @param grid_ la grille
     * @param excludeNumbersGrid_ les sets de tous les nombres exclus pour chaque case de la grille
     * @return true si la grille a ete mise a jour, false sinon
     */
    private static boolean applySinglePosition(int[][] grid_, HashSet<Integer>[][] excludeNumbersGrid_) {
        boolean updated = false;
        //parcours chaque bloc/ligne/colonne
        for (int i=0; i<grid_.length; i++) {
            //bloc
            HashSet<Integer> aEssayerBloc = getNumbers(grid_.length);
            HashSet<Integer> dejaPrisBloc = nombreDejaPrisBloc(grid_, i/3, i%3);
            aEssayerBloc.removeAll(dejaPrisBloc);
            for (int number : aEssayerBloc) {
                Tuple2<Integer,Integer> possibleCell = null;
                for (int j=0; j<grid_[0].length; j++) {
                    //si on peut mettre le nombre dans cette case
                    if ((grid_[(i/3)*3 + j/3][(i%3)*3 + j%3] == 0) && !nombreDejaPris(grid_, (i/3)*3 + j/3, (i%3)*3 + j%3).contains(number) && !excludeNumbersGrid_[(i/3)*3 + j/3][(i%3)*3 + j%3].contains(number)) {
                        if (possibleCell == null) { //s'il n'y a pas déjà de case possible dans ce bloc
                            possibleCell = new Tuple2((i/3)*3 + j/3, (i%3)*3 + j%3);
                        } else { //sinon, plusieurs cases possibles, on passe au nombre suivant
                            possibleCell = null;
                            break;
                        }
                    }
                }
                if (possibleCell != null) { //s'il n'y a qu'une case possible pour ce nombre
                    grid_[possibleCell._1][possibleCell._2] = number;
                    updated = true;
                }
            }
            //ligne
            HashSet<Integer> aEssayerLigne = getNumbers(grid_.length);
            HashSet<Integer> dejaPrisLigne = nombreDejaPrisLigne(grid_, i);
            aEssayerLigne.removeAll(dejaPrisLigne);
            for (int number : aEssayerLigne) {
                Tuple2<Integer,Integer> possibleCell = null;
                for (int j=0; j<grid_[0].length; j++) {
                    //si on peut mettre le nombre dans cette case
                    if ((grid_[i][j] == 0) && !nombreDejaPris(grid_, i, j).contains(number) && !excludeNumbersGrid_[i][j].contains(number)) {
                        if (possibleCell == null) { //s'il n'y a pas déjà de case possible dans cette ligne
                            possibleCell = new Tuple2(i, j);
                        } else { //sinon, plusieurs cases possibles, on passe au nombre suivant
                            possibleCell = null;
                            break;
                        }
                    }
                }
                if (possibleCell != null) { //s'il n'y a qu'une case possible pour ce nombre
                    grid_[possibleCell._1][possibleCell._2] = number;
                    updated = true;
                }
            }
            //colonne
            HashSet<Integer> aEssayerColonne = getNumbers(grid_.length);
            HashSet<Integer> dejaPrisColonne = nombreDejaPrisColonne(grid_, i);
            aEssayerColonne.removeAll(dejaPrisColonne);
            for (int number : aEssayerColonne) {
                Tuple2<Integer,Integer> possibleCell = null;
                for (int j=0; j<grid_.length; j++) {
                    //si on peut mettre le nombre dans cette case
                    if ((grid_[j][i] == 0) && !nombreDejaPris(grid_, j, i).contains(number) && !excludeNumbersGrid_[j][i].contains(number)) {
                        if (possibleCell == null) { //s'il n'y a pas déjà de case possible dans cette colonne
                            possibleCell = new Tuple2(j, i);
                        } else { //sinon, plusieurs cases possibles, on passe au nombre suivant
                            possibleCell = null;
                            break;
                        }
                    }
                }
                if (possibleCell != null) { //s'il n'y a qu'une case possible pour ce nombre
                    grid_[possibleCell._1][possibleCell._2] = number;
                    updated = true;
                }
            }
        }
        return updated;
    }
    
    /**
     * Applique la technique SingleCandidate sur la grille donnee.
     * @param grid_ la grille
     * @return true si la grille a ete mise a jour, false sinon
     */
    private static boolean applySingleCandidate(int[][] grid_) {
        HashSet<Integer>[][] excludeNumbersGrid = new HashSet[grid_.length][grid_[0].length];
        for (int i=0; i<excludeNumbersGrid.length; i++) {
            for (int j=0; j<excludeNumbersGrid[0].length; j++) {
                excludeNumbersGrid[i][j] = new HashSet<>();
            }
        }
        return applySingleCandidate(grid_, excludeNumbersGrid);
    }
    
    /**
     * Applique la technique SingleCandidate sur la grille donnee.
     * @param grid_ la grille
     * @param excludeNumbersGrid_ les sets de tous les nombres exclus pour chaque case de la grille
     * @return true si la grille a ete mise a jour, false sinon
     */
    private static boolean applySingleCandidate(int[][] grid_, HashSet<Integer>[][] excludeNumbersGrid_) {
        boolean updated = false;
        for (int i=0; i<grid_.length; i++) {
                for (int j=0; j<grid_[0].length; j++) {
                    if (grid_[i][j] == 0) {
                        //pour chaque case
                        HashSet<Integer> aEssayer = getNumbers(grid_.length);
                        HashSet<Integer> dejaPris = nombreDejaPris(grid_, i, j);
                        aEssayer.removeAll(dejaPris);
                        aEssayer.removeAll(excludeNumbersGrid_[i][j]);
                        if (aEssayer.size() == 1) {
                            grid_[i][j] = aEssayer.iterator().next();
                            updated = true;
                        }
                    }
                }
            }
        return updated;
    }
    
    /**
     * Applique les techniques Candidate Lines et Multiple Lines sur la grille donnee.
     * @param grid_ la grille
     * @param excludeNumbersGrid_ les sets de tous les nombres exclus pour chaque case de la grille
     * @return true si des nombres ont etes exclus, false sinon
     */
    private static boolean applyCandidateAndMultipleLines(int[][] grid_, HashSet<Integer>[][] excludeNumbersGrid_) {
        int nbBlocs = (int)Math.sqrt(grid_.length);
        boolean updated = false;
        ArrayList<HashSet<Integer>>[][] possiblesLinesForNumbersInBloc = new ArrayList[nbBlocs][nbBlocs];
        ArrayList<HashSet<Integer>>[][] possiblesColumnsForNumbersInBloc = new ArrayList[nbBlocs][nbBlocs];
        for (int i=0; i<nbBlocs; i++) {
            for (int j=0; j<nbBlocs; j++) {
                possiblesLinesForNumbersInBloc[i][j] = new ArrayList<>();
                possiblesColumnsForNumbersInBloc[i][j] = new ArrayList<>();
                for (int nb=0; nb<nbBlocs*nbBlocs; nb++) {
                    possiblesLinesForNumbersInBloc[i][j].add(new HashSet<>());
                    possiblesColumnsForNumbersInBloc[i][j].add(new HashSet<>());
                }
            }
        }
        
        //----- Candidate Lines -----//
        for (int x=0; x<nbBlocs; x++) {
            for (int y=0; y<nbBlocs ;y++) {
                HashSet<Integer> aEssayer = getNumbers(grid_.length);
                HashSet<Integer> dejaPris = nombreDejaPrisBloc(grid_, x, y);
                aEssayer.removeAll(dejaPris);
                for (int nombre : aEssayer) {
                    //toutes les positions possibles au sein de ce bloc pour nombre sauf celles interdites
                    HashSet<Tuple2<Integer, Integer>> cells = (HashSet<Tuple2<Integer, Integer>>)possibleCellsOfValueInBloc(grid_, x, y, nombre)
                                                                .stream()
                                                                .filter(cell -> !excludeNumbersGrid_[cell._1][cell._2].contains(nombre))
                                                                .collect(Collectors.toSet());
                    if (cells.size() > 0) {
                        //si les cases partagent toutes la même ligne ou la même colonne
                        boolean memeLignes = true;
                        boolean memeColonnes = true;
                        Iterator<Tuple2<Integer,Integer>> iterator = cells.iterator();
                        Tuple2<Integer,Integer> firstCell = iterator.next();
                        int ligne = firstCell._1;
                        int colonne = firstCell._2;
                        //pour Multiple Lines
                        possiblesLinesForNumbersInBloc[x][y].get(nombre-1).add(firstCell._1);
                        possiblesColumnsForNumbersInBloc[x][y].get(nombre-1).add(firstCell._2);
                        while(iterator.hasNext()) {
                            Tuple2<Integer,Integer> cell = iterator.next();
                            //pour Multiple Lines
                            possiblesLinesForNumbersInBloc[x][y].get(nombre-1).add(cell._1);
                            possiblesColumnsForNumbersInBloc[x][y].get(nombre-1).add(cell._2);
                            //même ligne
                            if (ligne != cell._1) {
                                memeLignes = false;
                            }
                            //même colonne
                            if (colonne != cell._2) {
                                memeColonnes = false;
                            }
                        }
                        if (memeLignes) {
                            for (int i=0; i<grid_.length; i++) {
                                if (i/3 != y) { //autre bloc
                                    if (!excludeNumbersGrid_[ligne][i].contains(nombre)) updated = true;
                                    excludeNumbersGrid_[ligne][i].add(nombre);
                                }
                            }
                        }
                        if (memeColonnes) {
                            for (int i=0; i<grid_[0].length; i++) {
                                if (i/3 != x) { //autre bloc
                                    if (!excludeNumbersGrid_[i][colonne].contains(nombre)) updated = true;
                                    excludeNumbersGrid_[i][colonne].add(nombre);
                                }
                            }
                        }
                    }
                }
            }
        }
        //----- Multiple Lines -----//
        for (int number : getNumbers(grid_.length)) {
            //lignes
            for (int line=0; line<nbBlocs; line++) {
                for (int x=0; x<nbBlocs; x++) {
                    for (int y=x+1; y<nbBlocs; y++) {
                        //si même lignes possibles pour ce nombre pour les 2 blocs et 2 colonnes possibles
                        if (possiblesLinesForNumbersInBloc[line][x].get(number-1).size() == 2
                         && possiblesLinesForNumbersInBloc[line][y].get(number-1).size() == 2
                         && possiblesLinesForNumbersInBloc[line][x].get(number-1).equals(possiblesLinesForNumbersInBloc[line][y].get(number-1))) {
                            int otherBlocColumn = 3-(x+y);
                            for (int i=0; i<nbBlocs; i++) {
                                Iterator<Integer> iterator = possiblesLinesForNumbersInBloc[line][x].get(number-1).iterator();
                                if (excludeNumbersGrid_[iterator.next()][otherBlocColumn*3+i].add(number)) updated = true;
                                if (excludeNumbersGrid_[iterator.next()][otherBlocColumn*3+i].add(number)) updated = true;
                            }
                        }
                    }
                }
            }
            //colonnes
            for (int col=0; col<nbBlocs; col++) {
                for (int x=0; x<nbBlocs; x++) {
                    for (int y=x+1; y<nbBlocs; y++) {
                        //si même colonnes possibles pour ce nombre pour les 2 blocs et 2 colonnes possibles
                        if (possiblesColumnsForNumbersInBloc[x][col].get(number-1).size() == 2
                         && possiblesColumnsForNumbersInBloc[y][col].get(number-1).size() == 2
                         && possiblesColumnsForNumbersInBloc[x][col].get(number-1).equals(possiblesColumnsForNumbersInBloc[y][col].get(number-1))) {
                            int otherBlocLine = 3-(x+y);
                            for (int i=0; i<nbBlocs; i++) {
                                Iterator<Integer> iterator = possiblesColumnsForNumbersInBloc[x][col].get(number-1).iterator();
                                if (excludeNumbersGrid_[otherBlocLine*3+i][iterator.next()].add(number)) updated = true;
                                if (excludeNumbersGrid_[otherBlocLine*3+i][iterator.next()].add(number)) updated = true;
                            }
                        }
                    }
                }
            }
        }
        return updated;
    }
    
    /**
     * Retourne un set des positions possibles pour la valeur value dans le bloc donne.
     * @param grid la grille
     * @param x ligne du bloc
     * @param y colonne du bloc
     * @return un set des positions possibles pour la valeur value dans le bloc donne
     */
    private static HashSet<Tuple2<Integer, Integer>> possibleCellsOfValueInBloc(int[][] grid_, int x_, int y_, int value_) {
        HashSet<Tuple2<Integer, Integer>> result = new HashSet<>();
        for (int i=0; i<9; i++) {
            if ((grid_[x_*3 + i/3][y_*3 + i%3] == 0) && !nombreDejaPris(grid_, x_*3 + i/3, y_*3 + i%3).contains(value_)) {
                result.add(new Tuple2<>(x_*3 + i/3, y_*3 + i%3));
            }
        }
        return result;
    }
    
    /**
     * Retourne la grille sous forme de string.
     * @param grid_ la grille a changer en string
     * @return la grille sous forme de string
     */
    public static String gridAsString(int[][] grid_) {
        String result = "";
        for (int i=0; i<grid_.length; i++) {
            for (int j=0; j<grid_[0].length; j++) {
                result += grid_[i][j];
            }
            result += "\n";
        }
        return result + "\n";
    }
}
