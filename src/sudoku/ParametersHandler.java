package sudoku;

/**
 * Conteneur des parametres de l'application.
 * @author JOHAN
 */
public class ParametersHandler {
    /**
     * Langue de l'application.
     */
    private Language language;
    /**
     * Mode debug.
     */
    private boolean debug;
    
    /**
     * Conteneur des parametres de l'application.
     */
    public ParametersHandler() {
        this(Language.FR); //par défaut
        this.debug = false;
    }
    
    /**
     * Conteneur des parametres de l'application.
     * @param language_ langage par defaut
     */
    public ParametersHandler(Language language_) {
        this.language = language_;
        this.debug = false;
    }
    
    /**
     * Conteneur des parametres de l'application.
     * @param language_ langage par defaut
     * @param debug_ mode debug ou non
     */
    public ParametersHandler(Language language_, boolean debug_) {
        this.language = language_;
        this.debug = debug_;
    }
    
    /**
     * Retourne le langage de l'application.
     * @return le langage de l'application
     */
    public Language getLanguage() {
        return this.language;
    }
    
    /**
     * Change le langage de l'application.
     * @param newLanguage_ la nouvelle langue a utiliser
     */
    public void setLanguage(Language newLanguage_) {
        this.language = newLanguage_;
    }
    
    /**
     * Retourne true si le mode debug est active, false sinon.
     * @return true si le mode debug est active, false sinon
     */
    public boolean isDebug() {
        return this.debug;
    }
    
    /**
     * Change le mode de l'application (debug ou non).
     * @param debug_ mode debug ou non
     */
    public void setDebug(boolean debug_) {
        this.debug = debug_;
    }
}
