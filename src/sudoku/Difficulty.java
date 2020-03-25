package sudoku;

/**
 * Enumération des difficultés de sudoku.
 * @author Johan Gousset
 */
public enum Difficulty {
    VERYEASY("Très facile", "Very easy"), EASY("Facile", "Easy"), MEDIUM("Moyen", "Medium"), DIFFICULT("Difficile", "Difficult"), /*TODOHARDCORE("Extrême", "Hardcore")*/;
    
    //nom de la difficulté
    private final String frenchName;
    private final String englishName;
    //langue utilisée
    private static Language language = Language.FR; //par défaut
    
    private Difficulty(String frenchName_, String englishName_) {
        this.frenchName = frenchName_;
        this.englishName = englishName_;
    }
    
    public static void setLanguage(Language language_) {
        language = language_;
    }
    
    public static Language getLanguage() {
        return language;
    }
    
    public String getName() {
        switch(language) {
            case ENG:
                return this.englishName;
            default:
                return this.frenchName;
        }
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
}
