package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class VentanaPrincipalController {

    @FXML private Button btnWarframes;
    @FXML private Button btnArmas;
    @FXML private Button btnBuilds;

    @FXML private Label lblTituloSeccion;
    @FXML private ComboBox<String> cmbSelectorPrincipal;

    @FXML private Button slotMod1, slotMod2, slotMod3, slotMod4;
    @FXML private Button slotMod5, slotMod6, slotMod7, slotMod8;

    @FXML private Label lblStatSalud;
    @FXML private Label lblStatEscudo;
    @FXML private Label lblStatArmadura;
    @FXML private Label lblStatEnergia;
    @FXML private Label lblStatDuracion;
    @FXML private Label lblStatEficiencia;
    @FXML private Label lblStatRango;
    @FXML private Label lblStatFuerza;

    private int modoActual = 0; 

    @FXML
    public void initialize() {
        System.out.println("Ventana principal cargada");
    }

    @FXML
    private void mostrarPanelWarframes() {
        cambiarPestanaActiva(btnWarframes, "ARSENAL: WARFRAMES");
        modoActual = 0;
    }

    @FXML
    private void mostrarPanelArmas() {
        cambiarPestanaActiva(btnArmas, "ARSENAL: ARMAS");
        modoActual = 1;
    }

    @FXML
    private void mostrarPanelBuilds() {
        cambiarPestanaActiva(btnBuilds, "MIS BUILDS GUARDADAS");
        modoActual = 2;
    }

    private void cambiarPestanaActiva(Button botonActivo, String titulo) {
        btnWarframes.getStyleClass().remove("active");
        btnArmas.getStyleClass().remove("active");
        btnBuilds.getStyleClass().remove("active");
        
        botonActivo.getStyleClass().add("active");
        lblTituloSeccion.setText(titulo);
    }

    @FXML
    private void limpiarMods() {
        System.out.println("Limpiando mods...");
    }

    @FXML
    private void guardarBuild() {
        System.out.println("Guardando Build...");
    }

    @FXML
    private void cerrarSesion() {
        System.out.println("Cerrando sesión...");
    }
}