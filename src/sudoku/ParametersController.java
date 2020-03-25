package sudoku;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Scene des parametres.
 * @author Johan Gousset
 */
public class ParametersController implements PropertyChangeListener {
    /**
     * Generateur de grilles.
     */
    GridGenerator gridGenerator;
    /**
     * Main de l'application.
     */
    private MainApp mainApp;
    /**
     * Scene d'acceuil.
     */
    private Scene homeScene;
    /**
     * Controleur de la selection de difficulte pour creer un sudoku.
     */
    private BeforeCreateController beforeCreateController;
    /**
     * Parametres de l'application.
     */
    private ParametersHandler parametersHandler;
    /**
     * Properties pour les textes a afficher.
     */
    private Properties properties;
    @FXML
    private Button homeButton;
    @FXML
    private Label languageLabel;
    @FXML
    private Label generatorLabel;
    @FXML
    private ChoiceBox<Language> languageChoiceBox;
    @FXML
    private CheckBox generatorCheckBox;
    @FXML
    private CheckBox debugCheckBox;
    
    @FXML
    private void homeButtonAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(this.homeScene);
    }
    
    @FXML
    private void languageChoiceBoxAction(ActionEvent event) {
        this.parametersHandler.setLanguage(this.languageChoiceBox.getValue());
        this.mainApp.update();
    }
    
    @FXML
    private void generatorCheckBoxAction(ActionEvent event) {
        this.gridGenerator.pause(!this.generatorCheckBox.isSelected());
        if (!this.generatorCheckBox.isSelected()) {
            this.beforeCreateController.cancel();
        }
    }
    
    @FXML
    private void debugCheckBoxAction(ActionEvent event) {
        this.parametersHandler.setDebug(this.debugCheckBox.isSelected());
        this.mainApp.update();
    }
    
    /**
     * Initialise le controleur de la page d'acceuil.
     * @param gridGenerator_ generateur de grilles
     * @param mainApp_ main de l'application
     * @param homeScene Scene de la page d'acceuil
     * @param beforeCreateController_ Controleur de la selection de difficulte pour creer un sudoku
     * @param parametersHandler_ conteneur des parametres
     */
    void initialize(GridGenerator gridGenerator_, MainApp mainApp_, Scene homeScene_, BeforeCreateController beforeCreateController_, ParametersHandler parametersHandler_) {
        this.gridGenerator = gridGenerator_;
        this.mainApp = mainApp_;
        this.homeScene = homeScene_;
        this.beforeCreateController = beforeCreateController_;
        this.parametersHandler = parametersHandler_;
        this.properties = new Properties();
        this.languageChoiceBox.setItems(FXCollections.observableList(Arrays.asList(Language.values())));
        this.languageChoiceBox.setValue(Language.FR); //par défaut
        this.gridGenerator.addPropertyChangeListener(this);
        update();
    }
    
    /**
     * Met à jour la scene de selection des parametres de creation d'un sudoku.
     */
    public void update() {
        String languageText = "Language"; //par défaut
        String generatorText = "Generator"; //par défaut
        //String propertiesFileName = "sudoku/texts/Parameters_" + parametersHandler.getLanguage() + ".properties"; //avec ClassLoader
        String propertiesFileName = "texts/Parameters_" + parametersHandler.getLanguage() + ".properties";
        try {
            //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
            InputStream inputStream = getClass().getResourceAsStream(propertiesFileName);
            if (inputStream != null) {
                this.properties.load(inputStream);
                languageText = this.properties.getProperty("languageLabel");
                generatorText = this.properties.getProperty("generatorLabel");
            }
        } catch (IOException ioe) {
            //impossible de lire le fichier property, laisse la valeur par défaut
        }
        String finalLanguageText = languageText; //pour la classe interne, besoin d'être final
        String finalGeneratorText = generatorText;
        //mise à jour de l'affichage via javafx
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                homeButton.setStyle("-fx-background-image: url('sudoku/buttons/" + parametersHandler.getLanguage() + "/Home.png')");
                languageLabel.setText(finalLanguageText);
                generatorLabel.setText(finalGeneratorText);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(GridGenerator.IS_RUNNING_PROPERTY)) {
            this.generatorCheckBox.setSelected((boolean)event.getNewValue());
        }
    }
}
