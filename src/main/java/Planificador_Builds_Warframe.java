import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal adaptada para iniciar con JavaFX.
 * @author aizpu
 */
public class Planificador_Builds_Warframe extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carga la vista del Login al arrancar.
        // La ruta "/vista/Login.fxml" asume que el archivo está en src/main/resources/vista/
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Login.fxml"));
        primaryStage.setTitle("Planificador Builds Warframe - Login");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // En lugar de ControladorPrincipal.iniciar(), lanzamos la aplicación JavaFX
        launch(args);
    }
}