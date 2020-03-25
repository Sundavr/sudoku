package sudoku;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author JOHAN
 */
public class CreateController {
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
    /**
     * Grille avant modification.
     */
    private int originalGrid[][];
    @FXML
    private Button homeButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button printButton;
    @FXML
    private Button solveButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane paneToPrint;
    @FXML
    private Label difficultyLabel;
    
    @FXML
    private void homeButtonAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(this.homeScene);
    }
    
    @FXML
    private void printButtonAction(ActionEvent event) {
        // Création du job d'impression.
        final PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob.showPrintDialog(this.printButton.getScene().getWindow())) {
            // Mise en page, si nécessaire.
            // Lancement de l'impression.
            if (printerJob.printPage(this.paneToPrint)) {
                // Fin de l'impression.
                printerJob.endJob();
            }
      }
    }
    
    @FXML
    private void solveButtonAction(ActionEvent event) {
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
        int result[][] = Sudoku.solver(grid);
        //si pas de solution pour la grille affichée, résoud la grille de base
        if (result == null) result = Sudoku.solver(this.originalGrid);
        //affiche le résultat
        if (result != null) {
            for (int i=0; i<9; i++) {
                for (int j=0; j<9; j++) {
                    this.gridCells[i][j].setText(""+result[i][j]);
                }
            }
        } else {
            System.out.println("BUG : pas de solution trouvé");
        }
    }
    
    @FXML
    private void resetButtonAction(ActionEvent event) {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                this.gridCells[i][j].setText((this.originalGrid[i][j] == 0 ? "" : ""+this.originalGrid[i][j]));
            }
        }
    }
    
    /**
     * Initialise le controleur de la page d'acceuil.
     * @param gridGenerator_ generateur de grilles
     * @param homeScene Scene de la page d'acceuil
     * @param beforeCreateScene_ Scene de la selection de difficulte pour creer un sudoku
     * @param parametersHandler_ Parametres de l'application
     */
    void initialize(GridGenerator gridGenerator_, Scene homeScene_, Scene beforeCreateScene_, ParametersHandler parametersHandler_) {
        this.gridGenerator = gridGenerator_;
        this.homeScene = homeScene_;
        this.beforeCreateScene = beforeCreateScene_;
        this.parametersHandler = parametersHandler_;
        this.initialized = true;
        this.gridCells = new TextField[9][9];
        this.originalGrid = new int[9][9];
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
                    printButton.setStyle("-fx-background-image: url('sudoku/buttons/" + parametersHandler.getLanguage() + "/Print.png')");
                    solveButton.setStyle("-fx-background-image: url('sudoku/buttons/" + parametersHandler.getLanguage() + "/Solve.png')");
                    resetButton.setStyle("-fx-background-image: url('sudoku/buttons/" + parametersHandler.getLanguage() + "/Reset.png')");
                }
            });
        }
    }
    
    public void generateGrid(Difficulty difficulty_) {
        int result[][] = this.gridGenerator.getGrid(difficulty_);
        if (result == null) return;
        this.originalGrid = result;
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                this.gridCells[i][j].setText((result[i][j] == 0 ? "" : ""+result[i][j]));
            }
        }
        this.difficultyLabel.setText(difficulty_.toString());
    }
    
    /**
     * Cree les TextField de la grille.
     */
    private void initGrid() {
        for (int row=0; row<9; row++) {
            for (int column=0; column<9; column++) {
                TextField cell = new TextField();
                cell.setAlignment(Pos.CENTER);
                this.gridCells[row][column] = cell;
                this.gridPane.add(cell, column, row);
            }
        }
    }
}
