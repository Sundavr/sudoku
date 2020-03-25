package sudoku;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generateur de grille.
 * @author JOHAN
 */
public class GridGenerator extends Thread {
    /**
     * Support des ecoutes.
     */
    private final PropertyChangeSupport changeSupport;
    /**
     * Nom de la propriete isRunning.
     */
    public static final String IS_RUNNING_PROPERTY = "isRunning";
    /**
     * Nom de la propriete veryEasyGrids.
     */
    public static final String VERY_EASY_GRIDS_PROPERTY = "veryEasyGrids";
    /**
     * Nom de la propriete easyGrids.
     */
    public static final String EASY_GRIDS_PROPERTY = "easyGrids";
    /**
     * Nom de la propriete mediumGrids.
     */
    public static final String MEDIUM_GRIDS_PROPERTY = "mediumGrids";
    /**
     * Nom de la propriete difficultGrids.
     */
    public static final String DIFFICULT_GRIDS_PROPERTY = "difficultGrids";
    /**
     * Nom de la propriete hardcoreGrids.
     */
    public static final String HARDCORE_GRIDS_PROPERTY = "hardcoreGrids";
    /**
     * Map qui contient une liste de grille associee a chaque difficulte.
     */
    private final HashMap<Difficulty, ArrayList<int[][]>> grids;
    /**
     * Map qui contient les boolean d'annulation de recherche d'une difficulte de grille donnee.
     */
    private final HashMap<Difficulty, Boolean> cancelMap;
    /**
     * Nombre maximal de grilles stockees.
     */
    private final int maxGrids;
    /**
     * Generateur actif ou non.
     */
    private boolean isRunning;
    /**
     * Generateur plein ou non.
     */
    private boolean isFull;
    
    public GridGenerator() {
        this.changeSupport = new PropertyChangeSupport(this);
        this.grids = new HashMap<>();
        this.cancelMap = new HashMap<>();
        for (Difficulty difficulty : Difficulty.values()) {
            this.grids.put(difficulty, new ArrayList<>());
            this.cancelMap.put(difficulty, false);
        }
        this.maxGrids = 10;
        this.setDaemon(true);
        this.setPriority(MIN_PRIORITY);
        this.isRunning = true;
    }
    
    @Override
    public void run() {
        while(true) {
            //TODO un thread par difficulté ? mais stock dans la map si besoin ?
            for (Difficulty initDifficulty : Difficulty.values()) {
                int[][] grid = Sudoku.generateGrid(initDifficulty);
                Difficulty difficulty = Sudoku.getDifficulty(grid);
                if (this.grids.get(difficulty).size() < this.maxGrids) {
                    synchronized(this.grids.get(difficulty)) {
                        this.grids.get(difficulty).add(grid);
                        this.changeSupport.firePropertyChange(getPropertyFromDifficulty(difficulty), this.grids.get(difficulty).size()-1, this.grids.get(difficulty).size());
                        this.grids.get(difficulty).notify();
                    }
                }
            }
            if (!this.isRunning || this.isFull()) {
                this.isRunning = false;
                this.changeSupport.firePropertyChange(IS_RUNNING_PROPERTY, true, false);
                synchronized(this) {
                    try { this.wait(); } catch (InterruptedException ie) {}
                }
                this.isRunning = true;
                this.changeSupport.firePropertyChange(IS_RUNNING_PROPERTY, false, true);
            }
        }
    }
    
    /**
     * Retourne true si le generateur est en marche, false, sinon.
     * @return true si le generateur est en marche, false, sinon
     */
    public boolean isRunning() {
        return this.isRunning;
    }
    
    /**
     * Change l'etat du generateur, le met en pause s'il etait actif, 
     * sinon le relance.
     */
    public void pause() {
        this.pause(!this.isRunning);
    }
    
    /**
     * Change l'etat du generateur : en pause ou non.
     * @param newState nouvel etat du generateur
     */
    public void pause(boolean newState) {
        this.isRunning = !newState;
        if (newState == false) {
            //relance generateur s'il etait en pause
            synchronized(this) {
                this.notify();
            }
        } else {
            //debloque les thread en attente de reponse si le generateur est mit en pause
            for (Difficulty difficulty : Difficulty.values()) {
                cancel(difficulty);
            }
        }
    }
    
    /**
     * Retourne le nom de la propriete associee a la difficulte donnee.
     * @param difficulty difficulte dont on cherche le nom de la propriete
     * @return le nom de la propriete associee a la difficulte donnee
     */
    public String getPropertyFromDifficulty(Difficulty difficulty) {
        switch(difficulty) {
            case VERYEASY:
                return VERY_EASY_GRIDS_PROPERTY;
            case EASY:
                return EASY_GRIDS_PROPERTY;
            case DIFFICULT:
                return DIFFICULT_GRIDS_PROPERTY;
            /*TODOcase HARDCORE:
                return HARDCORE_GRIDS_PROPERTY;*/
            default:
                return MEDIUM_GRIDS_PROPERTY;
        }
    }
    
    /**
     * Retourne une grille de la difficulte souhaitee, 
     * relance le générateur s'il etait en pause et qu'il n'y a pas de grille.
     * @param difficulty_ difficulte de la grille
     * @return une grille de la difficulte souhaitee
     */
    public int[][] getGrid(Difficulty difficulty_) {
        this.cancelMap.put(difficulty_, false);
        int[][] grid;
        long maxTime = 120000; //2min max
        synchronized(this.grids.get(difficulty_)) {
            long startTime = System.currentTimeMillis();
            this.setPriority(MAX_PRIORITY);
            while(this.grids.get(difficulty_).isEmpty() && ((System.currentTimeMillis()-startTime)<maxTime)) {
                if (this.isRunning == false) {
                    this.isRunning = true;
                    synchronized(this) {
                        this.notify();
                    }
                }
                try {
                    this.grids.get(difficulty_).wait(maxTime);
                    if (this.cancelMap.get(difficulty_)) return null; //si recherche cancel
                } catch (InterruptedException ie) {
                    return getDefaultGrid(difficulty_);
                }
            }
            this.setPriority(MIN_PRIORITY);
            if (this.grids.get(difficulty_).isEmpty()) {
                return getDefaultGrid(difficulty_);
            } else {
                this.changeSupport.firePropertyChange(getPropertyFromDifficulty(difficulty_), this.grids.get(difficulty_).size(), this.grids.get(difficulty_).size()-1);
                grid = this.grids.get(difficulty_).remove(0);
            }
        }
        if (this.isFull) {
            this.isFull = false;
            synchronized(this) {
                this.notify();
            }
        }
        return grid;
    }
    
    /**
     * Annule la requete de grille de la difficulte donnee.
     * @param difficulty_ difficulte de la grille demandee
     */
    public void cancel(Difficulty difficulty_) {
        this.cancelMap.put(difficulty_, true);
        synchronized(this.grids.get(difficulty_)) {
            this.grids.get(difficulty_).notifyAll();
        }
    }
    
    /**
     * Retourne le nombre de grille actuellement disponible dans la difficulte donnee.
     * @param difficulty difficulte
     * @return le nombre de grille actuellement disponible dans la difficulte donnee
     */
    public int getNumberOfGrids(Difficulty difficulty) {
        return this.grids.get(difficulty).size();
    }
    
    /**
     * Retourne true si le generateur est plein, c'est a dire que le nombre maximum 
     * de grilles de chaque difficulte est disponible, false sinon.
     * @return true si le genrateur est plein
     */
    public boolean isFull() {
        for (Difficulty difficulty :Difficulty.values()) {
            if (getNumberOfGrids(difficulty) < this.maxGrids) return false;
        }
        this.isFull = true;
        return true;
    }
    
    /**
     * Retourne la grille par defaut pour la difficulte donnee.
     * @param difficulty_ la difficulte de la grille
     * @return la grille par defaut pour la difficulte donnee
     */
    public int[][] getDefaultGrid(Difficulty difficulty_) {
        switch(difficulty_) {
            case VERYEASY:
                return Sudoku.VERY_EASY_GRID_EXAMPLE;
            case EASY:
                return Sudoku.EASY_GRID_EXAMPLE;
            case DIFFICULT:
                return Sudoku.DIFFICULT_GRID_EXAMPLE;
            default:
                return Sudoku.MEDIUM_GRID_EXAMPLE;
        }
    }
    
    /**
     * Ajoute un ecouteur.
     * @param listener le nouvel ecouteur
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        this.changeSupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Retire un ecouteur.
     * @param listener l'ecouteur a supprimer
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        this.changeSupport.removePropertyChangeListener(listener);
    }
    
    /**
     * Ajoute un ecouteur sur la propriete donnee.
     * @param propertyName la propriete a ecouter
     * @param listener le nouvel ecouteur
     */
    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.changeSupport.addPropertyChangeListener(propertyName, listener);
    }
    
    /**
     * Retire l'ecoute de la propriete donnee pour l'écouteur donne.
     * @param propertyName propriete dont on arrete l'ecoute
     * @param listener l'ecouteur a supprimer
     */
    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.changeSupport.removePropertyChangeListener(propertyName, listener);
    }
}
