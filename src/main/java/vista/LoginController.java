package vista;

import controlador.ControladorPrincipal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;

    @FXML
    public void initialize() {
        System.out.println("Pantalla de Login cargada.");
        ControladorPrincipal.iniciaFactory(); 
    }

    @FXML
    private void handleConectar() {
        String usuario = txtUsuario.getText();
        String pass = txtContrasena.getText();
        
        if (usuario.isEmpty() || pass.isEmpty()) {
            mostrarError("Por favor, rellena todos los campos.");
            return;
        }

        System.out.println("Intentando conectar con usuario: " + usuario);
        ControladorPrincipal.loginJavaFX(usuario, pass, this);
    }

    @FXML
    private void handleRegistrar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Registro.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Registrar Nueva Cuenta");
            stage.setScene(new Scene(root, 400, 500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al abrir la ventana de registro.");
        }
    }

    public void abrirVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/VentanaPrincipal.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root, 1300, 768));
            
            //Establecemos unos minimos de ancho y alto para que la aplicacion no se deforme
            stage.setMinWidth(1024);
            stage.setMinHeight(550);

            stage.setTitle("Arsenal Warframe");
            stage.centerOnScreen();
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al cargar la interfaz principal.");
        }
    }
    
    public void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}