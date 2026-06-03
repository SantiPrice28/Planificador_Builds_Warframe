import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author aizpu
 */
public class Planificador_Builds_Warframe extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carga la vista del Login al arrancar.
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Login.fxml"));
        primaryStage.setTitle("Planificador Builds Warframe - Login");
        primaryStage.setScene(new Scene(root, 600, 500));
        
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Cerrando Tenno OS y limpiando conexiones...");
            Platform.exit();
            System.exit(0);
        });
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}