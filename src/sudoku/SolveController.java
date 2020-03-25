package sudoku;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author JOHAN
 */
public class SolveController {
    /**
     * Scene d'acceuil.
     */
    private Scene homeScene;
    /**
     * Parametres de l'application.
     */
    private ParametersHandler parametersHandler;
    /**
     * Scene initialisee ou non.
     */
    private boolean initialized = false;
    /**
     * TextField de la grille.
     */
    private TextField gridCells[][];
    @FXML
    private Button homeButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button solveButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label difficultyLabel;
    
    @FXML
    private void homeButtonAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(this.homeScene);
    }
    
    @FXML
    private void resetButtonAction(ActionEvent event) {
        resetGrid();
    }
    
    @FXML
    private void solveButtonAction(ActionEvent event) {
        int grid[][] = this.getGrid();
        int result[][] = Sudoku.solver(grid);
        for (int i=0; i<9; i++) {
           for (int j=0; j<9; j++) {
                this.gridCells[i][j].setText((result[i][j] == 0 ? "" : ""+result[i][j]));
            }
        }
    }
    
    /**
     * Initialise le controleur de la page d'acceuil.
     * @param homeScene Scene de la page d'acceuil
     * @param parametersHandler_ Parametres de l'application
     */
    void initialize(Scene homeScene_, ParametersHandler parametersHandler_) {
        this.homeScene = homeScene_;
        this.parametersHandler = parametersHandler_;
        this.initialized = true;
        this.gridCells = new TextField[9][9];
        update();
        initGrid();
    }
    
    /**
     * Met à jour la scene de selection des parametres de creation d'un sudoku.
     */
    public void update() {
        if (this.initialized) {
            //mise à jour de l'affichage via javafx
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    homeButton.setStyle("-fx-background-image: url('sudoku/buttons/" + parametersHandler.getLanguage() + "/Home.png')");
                    resetButton.setStyle("-fx-background-image: url('sudoku/buttons/" + parametersHandler.getLanguage() + "/Reset.png')");
                    solveButton.setStyle("-fx-background-image: url('sudoku/buttons/" + parametersHandler.getLanguage() + "/Solve.png')");
                }
            });
        }
    }
    
    /**
     * Cree les TextField de la grille.
     */
    private void initGrid() {
        for (int row=0; row<9; row++) {
            for (int column=0; column<9; column++) {
                TextField cell = new TextField();
                cell.setAlignment(Pos.CENTER);
                int x = row;
                int y = column;
                cell.setOnKeyTyped(event -> {
                    //focus next node after key typed
                    if((cell.getSkin() instanceof BehaviorSkinBase) && (x!=8 || y!=8)) {
                        ((BehaviorSkinBase)cell.getSkin()).getBehavior().traverseNext();  
                    }
                });
                cell.setOnKeyReleased(event ->{
                    System.out.println(Sudoku.gridAsString(getGrid()));
                    difficultyLabel.setText(Sudoku.getDifficulty(getGrid()).toString());
                });
                this.gridCells[row][column] = cell;
                this.gridPane.add(cell, column, row);
            }
        }
    }
    
    /**
     * Vide la grille.
     */
    private void resetGrid() {
        for (TextField line[] : this.gridCells) {
            for (TextField cell : line) {
                cell.setText("");
            }
        }
        this.difficultyLabel.setText("");
    }
    
    /**
     * Retourne la grille actuelle.
     */
    private int[][] getGrid() {
        int grid[][] = new int[9][9];
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                try {
                    grid[i][j] = Integer.parseInt(this.gridCells[i][j].getText());
                } catch (NumberFormatException nfe) {
                    grid[i][j] = 0;
                }
            }
        }
        return grid;
    }
}
