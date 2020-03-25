package sudoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author JOHAN
 */
public class HomeController {
    /**
     * Scene des parametres.
     */
    private Scene parametersScene;
    /**
     * Scene de la selection de difficulte pour creer un sudoku.
     */
    private Scene beforeCreateScene;
    /**
     * Scene de la résolution de sudoku.
     */
    private Scene solveScene;
    /**
     * Parametres de l'application.
     */
    private ParametersHandler parametersHandler;
    @FXML
    private Button createButton;
    @FXML
    private Button solveButton;
    
    @FXML
    private void parametersButton(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(this.parametersScene);
    }
    
    @FXML
    private void createButtonAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(this.beforeCreateScene);
    }
    
    @FXML
    private void solveButtonAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(this.solveScene);
    }
    
    /**
     * Initialise le controleur de la page d'acceuil.
     * @param homeScene Scene de la page d'acceuil
     * @param parametersHandler_ Parametres de l'application
     */
    void initialize(Scene parametersScene_, Scene beforeCreateScene_, Scene solveScene_, ParametersHandler parametersHandler_) {
        this.parametersScene = parametersScene_;
        this.beforeCreateScene = beforeCreateScene_;
        this.solveScene = solveScene_;
        this.parametersHandler = parametersHandler_;
        update();
    }
    
    /**
     * Met à jour la scene d'acceuil.
     */
    public void update() {
        this.createButton.setStyle("-fx-background-image: url('sudoku/buttons/" + this.parametersHandler.getLanguage() + "/Create.png')");
        this.solveButton.setStyle("-fx-background-image: url('sudoku/buttons/" + this.parametersHandler.getLanguage() + "/Solve.png')");
    }
}
