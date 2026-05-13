package vista;

import controlador.ControladorPrincipal;
import java.sql.Connection;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.vo.*;

public class VentanaPrincipalController {

    @FXML
    private Button btnWarframes, btnArmas, btnBuilds;
    @FXML
    private Label lblTituloSeccion;
    @FXML
    private ComboBox<Object> cmbSelectorPrincipal;
    @FXML
    private HBox panelArsenal; 
    @FXML
    private ListView<Build> listaBuilds;

    // Nueva referencia para la línea
    @FXML
    private javafx.scene.shape.Line lineaDivisoria;

    // --- SLOTS DE MODS ---
    @FXML
    private Button slotMod1;
    @FXML
    private Button slotMod2;
    @FXML
    private Button slotMod3;
    @FXML
    private Button slotMod4;
    @FXML
    private Button slotMod5;
    @FXML
    private Button slotMod6;
    @FXML
    private Button slotMod7;
    @FXML
    private Button slotMod8;

    // Los 12 labels de la derecha
    @FXML
    private HBox filaStat1, filaStat2, filaStat3, filaStat4, filaStat5, filaStat6, filaStat7, filaStat8, filaStat9, filaStat10, filaStat11, filaStat12;
    @FXML
    private Label lblDesc1, lblDesc2, lblDesc3, lblDesc4, lblDesc5, lblDesc6, lblDesc7, lblDesc8, lblDesc9, lblDesc10, lblDesc11, lblDesc12;
    @FXML
    private Label lblVal1, lblVal2, lblVal3, lblVal4, lblVal5, lblVal6, lblVal7, lblVal8, lblVal9, lblVal10, lblVal11, lblVal12;

    private HBox[] filas;
    private Label[] descs;
    private Label[] vals;
    private Mod[] modsEquipados = new Mod[8]; // Array para guardar los 8 mods del arma o warframe

    @FXML
    public void initialize() {
        filas = new HBox[]{filaStat1, filaStat2, filaStat3, filaStat4, filaStat5, filaStat6, filaStat7, filaStat8, filaStat9, filaStat10, filaStat11, filaStat12};
        descs = new Label[]{lblDesc1, lblDesc2, lblDesc3, lblDesc4, lblDesc5, lblDesc6, lblDesc7, lblDesc8, lblDesc9, lblDesc10, lblDesc11, lblDesc12};
        vals = new Label[]{lblVal1, lblVal2, lblVal3, lblVal4, lblVal5, lblVal6, lblVal7, lblVal8, lblVal9, lblVal10, lblVal11, lblVal12};

        mostrarPanelWarframes();
        configurarSlotsMods();

        cmbSelectorPrincipal.setOnAction(e -> {
            Object sel = cmbSelectorPrincipal.getSelectionModel().getSelectedItem();

            limpiarMods();

            if (sel instanceof Warframe) {
                mostrarDatosWarframe((Warframe) sel);
            } else if (sel instanceof Arma) {
                mostrarDatosArma((Arma) sel);
            }
        });
    }

    private void cambiarPestanaActiva(Button botonActivo, String titulo) {
        btnWarframes.getStyleClass().remove("active");
        btnArmas.getStyleClass().remove("active");
        btnBuilds.getStyleClass().remove("active");

        botonActivo.getStyleClass().add("active");
        lblTituloSeccion.setText(titulo);
    }

    private void actualizarPanelStats(Map<String, String> stats, boolean mostrarLinea) {
        // Control de la línea divisoria
        if (lineaDivisoria != null) {
            lineaDivisoria.setVisible(mostrarLinea);
            lineaDivisoria.setManaged(mostrarLinea);
        }

        // 1. Ocultar todo primero
        for (HBox fila : filas) {
            fila.setVisible(false);
            fila.setManaged(false);
        }

        // 2. Rellenar solo lo que tenemos
        int i = 0;
        for (Map.Entry<String, String> entry : stats.entrySet()) {
            if (i < filas.length) {
                descs[i].setText(entry.getKey());
                vals[i].setText(entry.getValue());
                filas[i].setVisible(true);
                filas[i].setManaged(true);
                i++;
            }
        }
    }

    private void mostrarDatosWarframe(Warframe w) {
        Map<String, String> s = new LinkedHashMap<>();
        s.put("Salud", String.valueOf(w.getSalud()));
        s.put("Escudo", String.valueOf(w.getEscudo()));
        s.put("Armadura", String.valueOf(w.getArmadura()));
        s.put("Energía", String.valueOf(w.getEnergia()));
        s.put("Duración", (int) (w.getDuracion() * 100) + "%");
        s.put("Eficiencia", (int) (w.getEficiencia() * 100) + "%");
        s.put("Rango", (int) (w.getRango() * 100) + "%");
        s.put("Fuerza", (int) (w.getFuerza() * 100) + "%");
        actualizarPanelStats(s, true); // Warframe lleva línea
    }

    private void mostrarDatosArma(Arma a) {
        Map<String, String> s = new LinkedHashMap<>();
        if (a.getDañoImpacto() > 0) {
            s.put("Impacto", String.valueOf(a.getDañoImpacto()));
        }
        if (a.getDañoPerforante() > 0) {
            s.put("Perforación", String.valueOf(a.getDañoPerforante()));
        }
        if (a.getDañoCortante() > 0) {
            s.put("Cortante", String.valueOf(a.getDañoCortante()));
        }
        if (a.getDañoFrio() > 0) {
            s.put("Frío", String.valueOf(a.getDañoFrio()));
        }
        if (a.getDañoCalor() > 0) {
            s.put("Calor", String.valueOf(a.getDañoCalor()));
        }
        if (a.getDañoToxina() > 0) {
            s.put("Toxina", String.valueOf(a.getDañoToxina()));
        }

        s.put("Crítico", (int) (a.getCritico() * 100) + "%");
        s.put("Multiplicador", a.getMultCritico() + "x");
        s.put("Estado", (int) (a.getEstado() * 100) + "%");
        s.put("Cadencia", String.valueOf(a.getCadencia()));

        actualizarPanelStats(s, false); // Armas NO llevan línea
    }

    // --- GESTIÓN DE PANELES ---
    @FXML
    private void mostrarPanelWarframes() {
        cambiarPestanaActiva(btnWarframes, "ARSENAL: WARFRAMES");
        configurarVista(true);
        cargarCombo(0);
    }

    @FXML
    private void mostrarPanelArmas() {
        cambiarPestanaActiva(btnArmas, "ARSENAL: ARMAS");
        configurarVista(true);
        cargarCombo(1);
    }

    @FXML
    private void mostrarPanelBuilds() {
        cambiarPestanaActiva(btnBuilds, "MIS BUILDS");
        configurarVista(false);
        try (Connection conn = ControladorPrincipal.mySQLFactory.getConnection()) {
            List<Build> builds = ControladorPrincipal.buildDAO.listarBuilds(conn, ControladorPrincipal.usuarioActual.getId());
            listaBuilds.setItems(FXCollections.observableArrayList(builds));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarVista(boolean mostrarArsenal) {
        panelArsenal.setVisible(mostrarArsenal);
        panelArsenal.setManaged(mostrarArsenal);
        listaBuilds.setVisible(!mostrarArsenal);
        listaBuilds.setManaged(!mostrarArsenal);
    }

    private void cargarCombo(int tipo) {
        try (Connection conn = ControladorPrincipal.mySQLFactory.getConnection()) {
            if (tipo == 0) {
                List<Warframe> lista = ControladorPrincipal.warDAO.listarWarframes(conn);
                cmbSelectorPrincipal.setItems(FXCollections.observableArrayList(lista));
            } else {
                List<Arma> lista = ControladorPrincipal.armDAO.listarArmas(conn);
                cmbSelectorPrincipal.setItems(FXCollections.observableArrayList(lista));
            }

            if (!cmbSelectorPrincipal.getItems().isEmpty()) {
                cmbSelectorPrincipal.getSelectionModel().selectFirst();

                // Forzamos a que se muestren los datos del primer elemento de la lista
                Object primero = cmbSelectorPrincipal.getSelectionModel().getSelectedItem();
                if (primero instanceof Warframe) {
                    mostrarDatosWarframe((Warframe) primero);
                } else if (primero instanceof Arma) {
                    mostrarDatosArma((Arma) primero);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarSlotsMods() {
        Button[] slots = {slotMod1, slotMod2, slotMod3, slotMod4, slotMod5, slotMod6, slotMod7, slotMod8};
        for (int i = 0; i < slots.length; i++) {
            final int indice = i;
            slots[i].setOnAction(e -> abrirSelectorMod(indice, slots[indice]));
        }
    }

    private void abrirSelectorMod(int indice, Button botonSlot) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Seleccionar Mod");

        VBox layout = new VBox(10);
        layout.getStyleClass().add("modal-container");
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #1a1a1a;");

        ListView<Mod> listaView = new ListView<>();
        try (Connection conn = ControladorPrincipal.mySQLFactory.getConnection()) {
            Object objetoActual = cmbSelectorPrincipal.getSelectionModel().getSelectedItem();
            List<Mod> modsFiltrados = ControladorPrincipal.modDAO.listarModsFiltrados(conn, objetoActual);

            List<Integer> idsEquipados = new ArrayList<>();
            for (Mod m : modsEquipados) {
                if (m != null) {
                    idsEquipados.add(m.getId());
                }
            }

            ObservableList<Mod> itemsFiltrados = FXCollections.observableArrayList();

            itemsFiltrados.add(new Mod(-1, "+", "", "", 0, "Quitar mod", ""));
            for (Mod m : modsFiltrados) {
                if (!idsEquipados.contains(m.getId())) {
                    itemsFiltrados.add(m);
                }
            }

            listaView.setItems(itemsFiltrados);
        } catch (Exception e) {
            e.printStackTrace();
        }

        listaView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Mod seleccionado = listaView.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    if (seleccionado.getId() == -1) {
                        // Lógica de quitar
                        modsEquipados[indice] = null;
                        botonSlot.setText("+");
                        botonSlot.setStyle("");
                    } else {
                        // Lógica de equipar
                        modsEquipados[indice] = seleccionado;
                        botonSlot.setText(seleccionado.getNombre());
                        botonSlot.setStyle("-fx-background-color: #333333; -fx-text-fill: #ffcc00; -fx-border-color: #ffcc00;");
                    }
                    recalcularYMostrar();
                    stage.close();
                }
            }
        });

        // Formato User-Friendly
        listaView.setCellFactory(lv -> new ListCell<Mod>() {
            @Override
            protected void updateItem(Mod item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNombre() + " | " + item.getEfecto());
                }
            }
        });

        listaView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Mod seleccionado = listaView.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    // 1. Guardar en el array
                    modsEquipados[indice] = seleccionado;

                    // 2. Visual en el botón
                    botonSlot.setText(seleccionado.getNombre());
                    botonSlot.setStyle("-fx-background-color: #333333; -fx-text-fill: #ffcc00; -fx-border-color: #ffcc00;");

                    // 3. Recalcular TODO
                    recalcularYMostrar();

                    stage.close();
                }
            }
        });
        Scene scene = new Scene(layout, 400, 500);
        scene.getStylesheets().add(getClass().getResource("/vista/warframe-theme.css").toExternalForm());

        layout.getChildren().addAll(new Label("Doble clic para equipar:"), listaView);
        stage.setScene(scene);
        stage.show();
    }

    private void recalcularYMostrar() {
        Object sel = cmbSelectorPrincipal.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        Map<String, String> statsParaPantalla = new LinkedHashMap<>();

        if (sel instanceof Arma) {
            Map<String, Double> resultados = ControladorPrincipal.calcularDanoFinal((Arma) sel, modsEquipados);
            resultados.forEach((nombre, valor) -> {
                if (valor > 0) {
                    // Formateo especial para %
                    if (nombre.equals("Crítico") || nombre.equals("Estado")) {
                        statsParaPantalla.put(nombre, String.format("%.1f%%", valor));
                    } else if (nombre.equals("Multiplicador")) {
                        statsParaPantalla.put(nombre, String.format("%.1fx", valor));
                    } else {
                        statsParaPantalla.put(nombre, String.format("%.1f", valor));
                    }
                }
            });
            actualizarPanelStats(statsParaPantalla, false);
        } else if (sel instanceof Warframe) {
            Warframe w = (Warframe) sel;
            // Valores base
            double salud = w.getSalud();
            double escudo = w.getEscudo();
            double armadura = w.getArmadura();
            double energia = w.getEnergia();
            double dur = 1.0, ef = 1.0, ran = 1.0, fue = 1.0;

            for (Mod m : modsEquipados) {
                if (m == null) {
                    continue;
                }

                // Separamos el efecto por si tiene varias partes (ej: "+99% Fuerza / -55% Eficiencia")
                String[] partes = m.getEfecto().toLowerCase().split("/");

                for (String parte : partes) {
                    double valor = ControladorPrincipal.extraerPorcentaje(parte);
                    // Si la parte contiene un '-' o un 'sub', el valor debe ser negativo
                    if (parte.contains("-")) {
                        valor = -Math.abs(valor);
                    }

                    if (parte.contains("salud")) {
                        salud += w.getSalud() * valor;
                    }
                    if (parte.contains("escudo")) {
                        escudo += w.getEscudo() * valor;
                    }
                    if (parte.contains("armadura")) {
                        armadura += w.getArmadura() * valor;
                    }
                    if (parte.contains("energía") || parte.contains("energia")) {
                        energia += w.getEnergia() * valor;
                    }
                    if (parte.contains("duración") || parte.contains("duracion")) {
                        dur += valor;
                    }
                    if (parte.contains("eficiencia")) {
                        ef += valor;
                    }
                    if (parte.contains("rango")) {
                        ran += valor;
                    }
                    if (parte.contains("fuerza")) {
                        fue += valor;
                    }
                }
            }

            // Mostrar resultados
            statsParaPantalla.put("Salud", String.valueOf((int) Math.round(salud)));
            statsParaPantalla.put("Escudo", String.valueOf((int) Math.round(escudo)));
            statsParaPantalla.put("Armadura", String.valueOf((int) Math.round(armadura)));
            statsParaPantalla.put("Energía", String.valueOf((int) Math.round(energia)));

            statsParaPantalla.put("Duración", (int) Math.round(Math.max(0.1, dur) * 100) + "%");
            statsParaPantalla.put("Eficiencia", (int) Math.round(Math.max(0.1, ef) * 100) + "%");
            statsParaPantalla.put("Rango", (int) Math.round(Math.max(0.1, ran) * 100) + "%");
            statsParaPantalla.put("Fuerza", (int) Math.round(Math.max(0.1, fue) * 100) + "%");

            actualizarPanelStats(statsParaPantalla, true);
        }
    }

    @FXML
    private void limpiarMods() {
        // 1. Vaciar el array lógico
        Arrays.fill(modsEquipados, null);

        // 2. Resetear los botones visualmente
        Button[] slots = {slotMod1, slotMod2, slotMod3, slotMod4, slotMod5, slotMod6, slotMod7, slotMod8};
        for (Button b : slots) {
            b.setText("+");
            b.setStyle(""); 
        }

        // 3. Recalcular para volver a los stats base
        recalcularYMostrar();
    }

    @FXML
    private void guardarBuild() {
        //Falta la logica
        System.out.println("Guardando build...");
    }

    @FXML
    private void cerrarSesion() {
        System.exit(0);
    }
}
