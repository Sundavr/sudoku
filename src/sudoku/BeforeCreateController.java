package sudoku;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Scene de selection des parametres de creation d'un sudoku.
 * @author Johan Gousset
 */
public class BeforeCreateController implements PropertyChangeListener {
    /**
     * Generateur de grilles.
     */
    GridGenerator gridGenerator;
    /**
     * Scene d'acceuil.
     */
    private Scene homeScene;
    /**
     * Scene de la selection de difficulte pour creer un sudoku.
     */
    private Scene beforeCreateScene;
    /**
     * Scene de création de sudoku.
     */
    private Scene createScene;
    /**
     * Controleur de la creation d'un sudoku.
     */
    private CreateController createController;
    /**
     * Parametres de l'application.
     */
    private ParametersHandler parametersHandler;
    /**
     * Scene initialisee ou non.
     */
    private boolean initialized;
    /**
     * Creation de grille annulee ou non.
     */
    private boolean cancel;
    /**
     * Properties pour les textes a afficher.
     */
    private Properties properties;
    @FXML
    private Label difficultyLabel;
    @FXML
    private ChoiceBox<Difficulty> difficultyChoiceBox;
    @FXML
    private Button homeButton;
    @FXML
    private Button createButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ImageView preloaderImageView;
    @FXML
    private Label nbGridsLabel;
    
    @FXML
    private void homeButtonAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(this.homeScene);
    }
    
    @FXML
    private void createButtonAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.cancel = false;
        this.difficultyChoiceBox.setDisable(true);
        this.preloaderImageView.setVisible(true);
        this.cancelButton.setVisible(true);
        new Thread() {
            @Override
            public void run() {
                createController.generateGrid(difficultyChoiceBox.getValue());
                if (!cancel) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.setScene(createScene);
                        }
                    });
                }
                cancelButton.setVisible(false);
                preloaderImageView.setVisible(false);
                difficultyChoiceBox.setDisable(false);
            }
        }.start();
    }
    
    @FXML
    private void cancelButtonAction(ActionEvent event) {
        cancel();
    }
    
    /**
     * Annule la demande de grille au generateur.
     */
    void cancel() {
        this.cancel = true;
        this.gridGenerator.cancel(this.difficultyChoiceBox.getValue());
    }
    
    @FXML
    private void difficultyChoiceBoxAction(ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nbGridsLabel.setText(Integer.toString(gridGenerator.getNumberOfGrids(difficultyChoiceBox.getValue())));
            }
        });
    }
    
    /**
     * Initialise le controleur de la page d'acceuil.
     * @param gridGenerator_ generateur de grilles
     * @param homeScene Scene de la page d'acceuil
     * @param beforeCreateScene_ Scene de la selection de difficulte pour creer un sudoku
     * @param createScene Scene de la creation de sudoku
     * @param createController_ Controleur de la creation de sudoku
     * @param parametersHandler_ Parametres de l'application
     */
    void initialize(GridGenerator gridGenerator_, Scene homeScene_, Scene beforeCreateScene_, Scene createScene_, CreateController createController_, ParametersHandler parametersHandler_) {
        this.gridGenerator = gridGenerator_;
        this.homeScene = homeScene_;
        this.beforeCreateScene = beforeCreateScene_;
        this.createScene = createScene_;
        this.createController = createController_;
        this.parametersHandler = parametersHandler_;
        this.properties = new Properties();
        this.difficultyChoiceBox.setItems(FXCollections.observableList(new ArrayList(Arrays.asList(Difficulty.values()))));
        this.difficultyChoiceBox.setValue(Difficulty.EASY); //par défaut
        if (this.parametersHandler.isDebug()) {
            this.nbGridsLabel.setVisible(true);
        } else {
            this.nbGridsLabel.setVisible(false);
        }
        this.cancelButton.setVisible(false);
        this.gridGenerator.addPropertyChangeListener(this);
        this.initialized = true;
        this.cancel = false;
        update();
    }
    
    /**
     * Met à jour la scene de selection des parametres de creation d'un sudoku.
     */
    public void update() {
        if (this.initialized) {
            Difficulty.setLanguage(parametersHandler.getLanguage()); //màj énum
            String difficultyText = "Difficulty"; //par défaut
            String propertiesFileName = "texts/BeforeCreate_" + parametersHandler.getLanguage() + ".properties";
            try {
                InputStream inputStream = getClass().getResourceAsStream(propertiesFileName);
                if (inputStream != null) {
                    this.properties.load(inputStream);
                    difficultyText = this.properties.getProperty("difficultyLabel");
                }
            } catch (IOException ioe) {
                //impossible de lire le fichier property, laisse la valeur par défaut
            }
            String finalDifficultyText = difficultyText; //pour la classe interne, besoin d'être final
            //mise à jour de l'affichage via javafx
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //update la ChoiceBox
                    Difficulty selectedDifficulty = difficultyChoiceBox.getValue();
                    difficultyChoiceBox.getItems().clear();
                    difficultyChoiceBox.setItems(FXCollections.observableList(new ArrayList(Arrays.asList(Difficulty.values()))));
                    difficultyChoiceBox.setValue(selectedDifficulty);
                    difficultyLabel.setText(finalDifficultyText);
                    homeButton.setStyle("-fx-background-image: url('sudoku/buttons/" + parametersHandler.getLanguage() + "/Home.png')");
                    createButton.setStyle("-fx-background-image: url('sudoku/buttons/" + parametersHandler.getLanguage() + "/Create.png')");
                    if (parametersHandler.isDebug()) {
                        nbGridsLabel.setText(Integer.toString(gridGenerator.getNumberOfGrids(selectedDifficulty)));
                        nbGridsLabel.setVisible(true);
                    } else {
                        nbGridsLabel.setVisible(false);
                    }
                }
            });
            
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (this.parametersHandler.isDebug() &&  event.getPropertyName().equals(this.gridGenerator.getPropertyFromDifficulty(this.difficultyChoiceBox.getValue()))) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    nbGridsLabel.setText(Integer.toString(gridGenerator.getNumberOfGrids(difficultyChoiceBox.getValue())));
                }
            });
        }
    }
}
