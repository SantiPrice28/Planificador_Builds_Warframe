package vista;

import controlador.ControladorPrincipal;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroController {

    @FXML private TextField txtNombreUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private PasswordField txtContrasenaRepetir;

    @FXML
    private void handleRegistrar() {
        String usuario = txtNombreUsuario.getText().trim();
        String pass1 = txtContrasena.getText();
        String pass2 = txtContrasenaRepetir.getText();

        if (usuario.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            mostrarError("Por favor, rellena todos los campos.");
            return;
        }

        if (!pass1.equals(pass2)) {
            mostrarError("Las contraseñas no coinciden, Tenno.");
            return;
        }

        System.out.println("Registrando usuario: " + usuario);
        ControladorPrincipal.registrarUsuarioFX(usuario, pass1, this);
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    public void cerrarVentana() {
        Stage stage = (Stage) txtNombreUsuario.getScene().getWindow();
        stage.close();
    }

    public void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error en el Registro");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operación Exitosa");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}