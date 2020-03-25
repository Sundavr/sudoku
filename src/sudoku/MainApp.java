package sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main de l'interface graphique du l'application Sudoku.
 * @author JOHAN
 */
public class MainApp extends Application {
    private GridGenerator gridGenerator;
    private HomeController homeController;
    private ParametersController parametersController;
    private BeforeCreateController beforeCreateController;
    private CreateController createController;
    private SolveController solveController;
        
    @Override
    public void start(Stage stage) throws Exception {
        stage.setOnCloseRequest(event -> {
            System.exit(0); //s'assure de stopper proprement l'application si le thread principal est en wait sur le generateur
        });
        this.gridGenerator = new GridGenerator();
        this.gridGenerator.start();
        
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        
        ParametersHandler parametersHandler = new ParametersHandler();
        
        //acceuil
        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
        Parent home = homeLoader.load();
        Scene homeScene = new Scene(home);
        
        //paramètres
        FXMLLoader parametersLoader = new FXMLLoader(getClass().getResource("fxml/Parameters.fxml"));
        Parent parameters = parametersLoader.load();
        Scene parametersScene = new Scene(parameters);
        
        //sélection de la difficulté avant création d'un sudoku
        FXMLLoader beforeCreateLoader = new FXMLLoader(getClass().getResource("fxml/BeforeCreate.fxml"));
        Parent beforeCreate = beforeCreateLoader.load();
        Scene beforeCreateScene = new Scene(beforeCreate);
        
        //création d'un sudoku
        FXMLLoader createLoader = new FXMLLoader(getClass().getResource("fxml/Create.fxml"));
        Parent create = createLoader.load();
        Scene createScene = new Scene(create);
        
        //résolution d'un sudoku
        FXMLLoader solveLoader = new FXMLLoader(getClass().getResource("fxml/Solve.fxml"));
        Parent solve = solveLoader.load();
        Scene solveScene = new Scene(solve);
        
        //on récupère les controleurs
        this.homeController = (HomeController)homeLoader.getController();
        this.parametersController = (ParametersController)parametersLoader.getController();
        this.beforeCreateController = (BeforeCreateController)beforeCreateLoader.getController();
        this.createController = (CreateController)createLoader.getController();
        this.solveController = (SolveController)solveLoader.getController();
        
        //on initialise les controleurs
        this.homeController.initialize(parametersScene, beforeCreateScene, solveScene, parametersHandler);
        this.parametersController.initialize(this.gridGenerator, this, homeScene, this.beforeCreateController, parametersHandler);
        this.beforeCreateController.initialize(this.gridGenerator, homeScene, beforeCreateScene, createScene, this.createController, parametersHandler);
        this.createController.initialize(this.gridGenerator, homeScene, beforeCreateScene, parametersHandler);
        this.solveController.initialize(homeScene, parametersHandler);
        
        stage.setTitle("Sudoku");
        stage.setScene(homeScene);
        stage.show();
    }
    
    public void update() {
        this.homeController.update();
        this.parametersController.update();
        this.beforeCreateController.update();
        this.createController.update();
        this.solveController.update();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
