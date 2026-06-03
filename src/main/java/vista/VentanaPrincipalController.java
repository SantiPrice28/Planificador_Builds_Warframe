package vista;

import controlador.ControladorPrincipal;
import java.sql.Connection;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
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
    @FXML
    private Label lblUsuario;

    @FXML
    private VBox panelBuildsContenedor;
    @FXML
    private Button btnBuildsPublicas;
    @FXML
    private Button btnTusBuilds;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Button btnEliminarBuild;

    // Variable para saber qué estamos viendo y no mezclar eventos del ComboBox
    // 0: Warframes, 1: Armas, 2: Builds
    private int modoVista = -1;
    private boolean viendoBuildsPublicas = false; // Por defecto vemos las nuestras
    private Build buildEnEdicion = null;
    private boolean ignorarCombo = false;
    private boolean cambiosSinGuardar = false;
    private Object objetoSeleccionadoAnterior = null;

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
        configurarBotonesBuilds();

        if (ControladorPrincipal.usuarioActual != null) {
            lblUsuario.setText("Usuario: " + ControladorPrincipal.usuarioActual.getNombreUsuario());
        }

        // Detectar doble clic en la lista de builds
        listaBuilds.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Build seleccionada = listaBuilds.getSelectionModel().getSelectedItem();
                if (seleccionada != null) {
                    cargarBuildEnPantalla(seleccionada);
                }
            }
        });

        cmbSelectorPrincipal.setOnAction(e -> {
            if (ignorarCombo) {
                return;
            }
            Object sel = cmbSelectorPrincipal.getSelectionModel().getSelectedItem();
            if (sel == null) {
                return;
            }

            if ((modoVista == 0 || modoVista == 1) && sel != objetoSeleccionadoAnterior) {

                // Si hay mods y el usuario dice CANCELAR
                if (!confirmarPerdidaCambios()) {
                    ignorarCombo = true;
                    cmbSelectorPrincipal.getSelectionModel().select(objetoSeleccionadoAnterior);
                    ignorarCombo = false;
                    return; 
                }
            }
            
            objetoSeleccionadoAnterior = sel;

            if (modoVista == 0 || modoVista == 1) {
                if (sel instanceof String) {
                    return;
                }

                buildEnEdicion = null;
                cambiosSinGuardar = false;
                resetearMods();

                if (sel instanceof Warframe) {
                    mostrarDatosWarframe((Warframe) sel);
                } else if (sel instanceof Arma) {
                    mostrarDatosArma((Arma) sel);
                }
            } else if (modoVista == 2) {
                actualizarListaBuilds();
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

        // Ocultar todo primero
        for (HBox fila : filas) {
            fila.setVisible(false);
            fila.setManaged(false);
        }

        // Rellenar solo lo que tenemos
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
        actualizarPanelStats(s, true);
    }

    private void mostrarDatosArma(Arma a) {
        Map<String, String> s = new LinkedHashMap<>();
        
        if (a.getDañoImpacto() > 0) s.put("Impacto", String.format("%.2f", a.getDañoImpacto()));
        if (a.getDañoPerforante() > 0) s.put("Perforación", String.format("%.2f", a.getDañoPerforante()));
        if (a.getDañoCortante() > 0) s.put("Cortante", String.format("%.2f", a.getDañoCortante()));
        if (a.getDañoFrio() > 0) s.put("Frío", String.format("%.2f", a.getDañoFrio()));
        if (a.getDañoCalor() > 0) s.put("Calor", String.format("%.2f", a.getDañoCalor()));
        if (a.getDañoToxina() > 0) s.put("Toxina", String.format("%.2f", a.getDañoToxina()));

        s.put("Crítico", String.format("%.2f%%", a.getCritico() * 100));
        s.put("Multiplicador", String.format("%.2fx", a.getMultCritico()));
        s.put("Estado", String.format("%.2f%%", a.getEstado() * 100));
        s.put("Cadencia", String.format("%.2f", a.getCadencia()));
        
        if (a.getPrecision() > 0) {
            s.put("Precisión", String.format("%.2f", a.getPrecision()));
        }

        actualizarPanelStats(s, false);
    }

    @FXML
    private void mostrarPanelWarframes() {
        if (modoVista == 0) {
            return;
        }
        if (!confirmarPerdidaCambios()) {
            return;
        }
        cambiosSinGuardar = false;
        modoVista = 0;
        cambiarPestanaActiva(btnWarframes, "ARSENAL: WARFRAMES");
        configurarVista(true);
        cargarCombo(0);
    }

    @FXML
    private void mostrarPanelArmas() {
        if (modoVista == 1) {
            return;
        }
        if (!confirmarPerdidaCambios()) {
            return;
        }

        cambiosSinGuardar = false;
        modoVista = 1;
        cambiarPestanaActiva(btnArmas, "ARSENAL: ARMAS");
        configurarVista(true);
        cargarCombo(1);
    }

    @FXML
    private void mostrarPanelBuilds() {
        if (modoVista == 2) {
            return;
        }
        if (!confirmarPerdidaCambios()) {
            return;
        }

        cambiosSinGuardar = false;
        modoVista = 2;
        cambiarPestanaActiva(btnBuilds, "BUILDS");
        configurarVista(false);
        cargarCombo(2);
        actualizarListaBuilds();
    }

    private void configurarVista(boolean mostrarArsenal) {
        if (panelArsenal != null) {
            panelArsenal.setVisible(mostrarArsenal);
            panelArsenal.setManaged(mostrarArsenal);
        }
        if (panelBuildsContenedor != null) {
            panelBuildsContenedor.setVisible(!mostrarArsenal);
            panelBuildsContenedor.setManaged(!mostrarArsenal);
        }
    }

    private void cargarCombo(int tipo) {
        Connection conn = null;
        try {
            conn = ControladorPrincipal.mySQLFactory.getConnection();
            ObservableList<Object> items = FXCollections.observableArrayList();

            if (tipo == 0) {
                items.addAll(ControladorPrincipal.warDAO.listarWarframes(conn));
            } else if (tipo == 1) {
                items.addAll(ControladorPrincipal.armDAO.listarArmas(conn));
            } else if (tipo == 2) {
                // --- MODO BUILDS ---
                items.add("--- CUALQUIERA ---"); // Muestra TODO

                items.add("--- WARFRAMES ---"); // Muestra todos los Warframes
                items.addAll(ControladorPrincipal.warDAO.listarWarframes(conn));

                items.add("--- ARMAS ---"); // Muestra todas las armas
                items.addAll(ControladorPrincipal.armDAO.listarArmas(conn));
            }

            cmbSelectorPrincipal.setItems(items);

            if (!items.isEmpty()) {
                cmbSelectorPrincipal.getSelectionModel().selectFirst();

                objetoSeleccionadoAnterior = items.get(0);

                // Forzar actualización visual
                Object primero = items.get(0);
                if (modoVista == 0 && primero instanceof Warframe) {
                    mostrarDatosWarframe((Warframe) primero);
                } else if (modoVista == 1 && primero instanceof Arma) {
                    mostrarDatosArma((Arma) primero);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    ControladorPrincipal.mySQLFactory.releaseConnection(conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void configurarSlotsMods() {
        Button[] slots = {slotMod1, slotMod2, slotMod3, slotMod4, slotMod5, slotMod6, slotMod7, slotMod8};
        for (int i = 0; i < slots.length; i++) {
            final int indice = i;

            slots[i].setPrefWidth(160);
            slots[i].setMaxWidth(160);

            slots[i].setPrefHeight(280);
            slots[i].setMaxHeight(280);

            slots[i].setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

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

        Connection conn = null;
        try {
            conn = ControladorPrincipal.mySQLFactory.getConnection();
            Object objetoActual = cmbSelectorPrincipal.getSelectionModel().getSelectedItem();
            List<Mod> modsFiltrados = ControladorPrincipal.modDAO.listarModsFiltrados(conn, objetoActual);

            // Recoger IDs equipados y sus incompatibles
            List<Integer> idsEquipados = new ArrayList<>();
            List<Integer> idsIncompatibles = new ArrayList<>();

            for (Mod m : modsEquipados) {
                if (m != null) {
                    idsEquipados.add(m.getId());
                    // Consultamos a la BD usando tu método existente
                    idsIncompatibles.addAll(ControladorPrincipal.modDAO.obtenerModsIncompatibles(conn, m.getId()));
                }
            }

            // Filtrar la lista que verá el usuario
            ObservableList<Mod> itemsFiltrados = FXCollections.observableArrayList();
            itemsFiltrados.add(new Mod(-1, "+", "", "", 0, "Quitar mod", ""));

            for (Mod m : modsFiltrados) {
                // Solo añadimos si no está equipado Y no es incompatible
                if (!idsEquipados.contains(m.getId()) && !idsIncompatibles.contains(m.getId())) {
                    itemsFiltrados.add(m);
                }
            }
            listaView.setItems(itemsFiltrados);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    ControladorPrincipal.mySQLFactory.releaseConnection(conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 3. Formato visual
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

        // 4. Lógica de selección (Unificada en un solo setOnMouseClicked)
        listaView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Mod seleccionado = listaView.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    if (seleccionado.getId() == -1) {
                        modsEquipados[indice] = null;
                        botonSlot.setText("+");
                        botonSlot.setStyle("");
                    } else {
                        modsEquipados[indice] = seleccionado;
                        // Nombre + Efecto con salto de línea
                        botonSlot.setText(seleccionado.getNombre() + "\n\n" + seleccionado.getEfecto());
                        botonSlot.setWrapText(true);
                        botonSlot.setStyle("-fx-background-color: #333333; -fx-text-fill: #ffcc00; -fx-border-color: #ffcc00; -fx-text-alignment: center;");
                    }
                    recalcularYMostrar();

                    evaluarCambiosSinGuardar();

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
                    String nombreBonito = capitalizar(nombre);

                    // Formateo especial para % (AHORA CON 2 DECIMALES)
                    if (nombreBonito.equals("Crítico") || nombreBonito.equals("Estado")) {
                        statsParaPantalla.put(nombreBonito, String.format("%.2f%%", valor));
                    } else if (nombreBonito.equals("Multiplicador")) {
                        statsParaPantalla.put(nombreBonito, String.format("%.2fx", valor));
                    } else {
                        statsParaPantalla.put(nombreBonito, String.format("%.2f", valor));
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Limpiar Arsenal");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas quitar todos los mods? Empezarás desde cero.");

        java.util.Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            resetearMods();

            cambiosSinGuardar = false;
        }
    }

    private void resetearMods() {
        // Vaciar el array lógico
        Arrays.fill(modsEquipados, null);

        // Resetear los botones visualmente
        Button[] slots = {slotMod1, slotMod2, slotMod3, slotMod4, slotMod5, slotMod6, slotMod7, slotMod8};
        for (Button b : slots) {
            b.setText("+");
            b.setStyle("");
        }

        // Recalcular para volver a los stats base
        recalcularYMostrar();
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        // Pone la primera letra en mayúscula y el resto en minúscula
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    private void actualizarListaBuilds() {
        Object sel = cmbSelectorPrincipal.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        int idUsuarioActual = -1;
        if (ControladorPrincipal.usuarioActual != null) {
            idUsuarioActual = ControladorPrincipal.usuarioActual.getId();
        } else {
            System.out.println("Error: No hay usuario logeado en memoria.");
            return;
        }

        Connection conn = null;

        try {
            conn = ControladorPrincipal.mySQLFactory.getConnection();
            List<Build> resultados = new ArrayList<>();

            // Leemos qué ha seleccionado el usuario y llamamos al DAO
            if (sel instanceof String) {
                String filtro = (String) sel;
                if (filtro.contains("CUALQUIERA")) {
                    resultados = ControladorPrincipal.buildDAO.buscarBuilds(conn, idUsuarioActual, viendoBuildsPublicas, "TODAS", 0);
                } else if (filtro.contains("WARFRAMES")) {
                    resultados = ControladorPrincipal.buildDAO.buscarBuilds(conn, idUsuarioActual, viendoBuildsPublicas, "SOLO_WARFRAMES", 0);
                } else if (filtro.contains("ARMAS")) {
                    resultados = ControladorPrincipal.buildDAO.buscarBuilds(conn, idUsuarioActual, viendoBuildsPublicas, "SOLO_ARMAS", 0);
                }
            } else if (sel instanceof Warframe) {
                resultados = ControladorPrincipal.buildDAO.buscarBuilds(conn, idUsuarioActual, viendoBuildsPublicas, "WARFRAME_ESPECIFICO", ((Warframe) sel).getId());
            } else if (sel instanceof Arma) {
                resultados = ControladorPrincipal.buildDAO.buscarBuilds(conn, idUsuarioActual, viendoBuildsPublicas, "ARMA_ESPECIFICA", ((Arma) sel).getId());
            }

            // Metemos la lista de builds en la pantalla
            listaBuilds.setItems(FXCollections.observableArrayList(resultados));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    ControladorPrincipal.mySQLFactory.releaseConnection(conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void configurarBotonesBuilds() {
        btnBuildsPublicas.setOnAction(e -> {
            viendoBuildsPublicas = true;

            // Visualmente activamos "Públicas"
            btnBuildsPublicas.getStyleClass().remove("btn-pestana");
            if (!btnBuildsPublicas.getStyleClass().contains("btn-pestana-activa")) {
                btnBuildsPublicas.getStyleClass().add("btn-pestana-activa");
            }

            // Visualmente desactivamos "Tus Builds"
            btnTusBuilds.getStyleClass().remove("btn-pestana-activa");
            if (!btnTusBuilds.getStyleClass().contains("btn-pestana")) {
                btnTusBuilds.getStyleClass().add("btn-pestana");
            }

            // Refrescamos la lista de la pantalla
            actualizarListaBuilds();
        });

        btnTusBuilds.setOnAction(e -> {
            viendoBuildsPublicas = false; // Cambiamos la lógica

            // Visualmente activamos "Tus Builds"
            btnTusBuilds.getStyleClass().remove("btn-pestana");
            if (!btnTusBuilds.getStyleClass().contains("btn-pestana-activa")) {
                btnTusBuilds.getStyleClass().add("btn-pestana-activa");
            }

            // Visualmente desactivamos "Públicas"
            btnBuildsPublicas.getStyleClass().remove("btn-pestana-activa");
            if (!btnBuildsPublicas.getStyleClass().contains("btn-pestana")) {
                btnBuildsPublicas.getStyleClass().add("btn-pestana");
            }

            // Refrescamos la lista de la pantalla
            actualizarListaBuilds();
        });
    }

    private void cargarBuildEnPantalla(Build build) {
        if (build == null) {
            return;
        }

        ignorarCombo = true;

        Connection conn = null;
        try {
            conn = ControladorPrincipal.mySQLFactory.getConnection();

            // Cambiamos de pestaña y seleccionamos el equipamiento base
            if ("Warframe".equalsIgnoreCase(build.getTipo())) {
                mostrarPanelWarframes();

                for (Object item : cmbSelectorPrincipal.getItems()) {
                    if (item instanceof Warframe && ((Warframe) item).getId() == build.getIdWarframe()) {
                        cmbSelectorPrincipal.getSelectionModel().select(item);
                        objetoSeleccionadoAnterior = item;
                        break;
                    }
                }
            } else if ("Arma".equalsIgnoreCase(build.getTipo())) {
                mostrarPanelArmas();

                for (Object item : cmbSelectorPrincipal.getItems()) {
                    if (item instanceof Arma && ((Arma) item).getId() == build.getIdArma()) {
                        cmbSelectorPrincipal.getSelectionModel().select(item);
                        objetoSeleccionadoAnterior = item;
                        break;
                    }
                }
            }

            // Limpiamos cualquier mod que hubiera antes y preparamos las variables
            resetearMods();
            Integer[] idsMods = {
                build.getMod1Id(), build.getMod2Id(), build.getMod3Id(), build.getMod4Id(),
                build.getMod5Id(), build.getMod6Id(), build.getMod7Id(), build.getMod8Id()
            };
            Button[] slots = {slotMod1, slotMod2, slotMod3, slotMod4, slotMod5, slotMod6, slotMod7, slotMod8};

            // Buscamos cada mod en la BD y lo equipamos en el botón visual y en el array lógico
            for (int i = 0; i < idsMods.length; i++) {
                if (idsMods[i] != null && idsMods[i] > 0) {
                    Mod mod = ControladorPrincipal.modDAO.obtenerModPorId(conn, idsMods[i]);
                    if (mod != null) {
                        modsEquipados[i] = mod;

                        // Ponemos nombre + salto de línea + efecto
                        slots[i].setText(mod.getNombre() + "\n\n" + mod.getEfecto());
                        slots[i].setWrapText(true); // Permite varias líneas

                        slots[i].setStyle("-fx-background-color: #333333; -fx-text-fill: #ffcc00; -fx-border-color: #ffcc00; -fx-text-alignment: center;");
                    }
                }
            }

            // Recalculamos los stats finales
            recalcularYMostrar();

            buildEnEdicion = build;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar la build en pantalla.");
        } finally {
            ignorarCombo = false;
            cambiosSinGuardar = false;
        }
        if (conn != null) {
            try {
                ControladorPrincipal.mySQLFactory.releaseConnection(conn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void guardarBuild() {
        System.out.println("¡Botón guardar pulsado!");
        Object sel = cmbSelectorPrincipal.getSelectionModel().getSelectedItem();
        if (sel == null || modoVista == 2) {
            mostrarAlerta("Atención", "Debes seleccionar un Arma o Warframe en tu Arsenal para poder guardar.");
            return;
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(buildEnEdicion != null ? "Modificar Build" : "Guardar nueva Build");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1a1a1a;");

        Label lblTitulo = new Label("NOMBRE DE LA BUILD:");
        lblTitulo.setStyle("-fx-text-fill: #c5a45f; -fx-font-weight: bold;");
        TextField txtNombre = new TextField(buildEnEdicion != null ? buildEnEdicion.getNombre() : "");
        txtNombre.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-border-color: #555;");

        Label lblDesc = new Label("DESCRIPCIÓN:");
        lblDesc.setStyle("-fx-text-fill: #c5a45f; -fx-font-weight: bold;");
        TextArea txtDesc = new TextArea(buildEnEdicion != null ? buildEnEdicion.getDescripcion() : "");
        txtDesc.setPrefRowCount(3);
        txtDesc.setStyle("-fx-control-inner-background: #333; -fx-text-fill: white; -fx-border-color: #555;");

        CheckBox chkPublica = new CheckBox("Hacer pública (visible para todos)");
        chkPublica.setStyle("-fx-text-fill: white;");
        if (buildEnEdicion != null) {
            chkPublica.setSelected(buildEnEdicion.isEsPublica());
        }

        Button btnGuardarNuevo = new Button(buildEnEdicion != null ? "GUARDAR COMO COPIA NUEVA" : "GUARDAR EN EL ARSENAL");
        btnGuardarNuevo.setStyle("-fx-background-color: #333; -fx-text-fill: #c5a45f; -fx-border-color: #c5a45f; -fx-cursor: hand; -fx-font-weight: bold;");
        btnGuardarNuevo.setMaxWidth(Double.MAX_VALUE);

        Button btnSobrescribir = new Button("SOBRESCRIBIR BUILD ACTUAL");
        btnSobrescribir.setStyle("-fx-background-color: #5a2e2e; -fx-text-fill: #ff6666; -fx-border-color: #ff6666; -fx-cursor: hand; -fx-font-weight: bold;");
        btnSobrescribir.setMaxWidth(Double.MAX_VALUE);

        // Función compartida para recolectar datos
        java.util.function.Consumer<Build> recolectarDatos = (nuevaBuild) -> {
            nuevaBuild.setNombre(txtNombre.getText().trim());
            nuevaBuild.setDescripcion(txtDesc.getText().trim());
            nuevaBuild.setIdUsuario(ControladorPrincipal.usuarioActual.getId());
            nuevaBuild.setEsPublica(chkPublica.isSelected());

            if (modoVista == 0 && sel instanceof Warframe) {
                nuevaBuild.setTipo("Warframe");
                nuevaBuild.setIdWarframe(((Warframe) sel).getId());
            } else if (modoVista == 1 && sel instanceof Arma) {
                nuevaBuild.setTipo("Arma");
                nuevaBuild.setIdArma(((Arma) sel).getId());
            }

            nuevaBuild.setMod1Id(modsEquipados[0] != null ? modsEquipados[0].getId() : null);
            nuevaBuild.setMod2Id(modsEquipados[1] != null ? modsEquipados[1].getId() : null);
            nuevaBuild.setMod3Id(modsEquipados[2] != null ? modsEquipados[2].getId() : null);
            nuevaBuild.setMod4Id(modsEquipados[3] != null ? modsEquipados[3].getId() : null);
            nuevaBuild.setMod5Id(modsEquipados[4] != null ? modsEquipados[4].getId() : null);
            nuevaBuild.setMod6Id(modsEquipados[5] != null ? modsEquipados[5].getId() : null);
            nuevaBuild.setMod7Id(modsEquipados[6] != null ? modsEquipados[6].getId() : null);
            nuevaBuild.setMod8Id(modsEquipados[7] != null ? modsEquipados[7].getId() : null);
        };

        // Evento Insertar
        btnGuardarNuevo.setOnAction(e -> {
            if (txtNombre.getText().trim().isEmpty()) {
                mostrarAlerta("Error", "El nombre es obligatorio.");
                return;
            }

            Connection conn = null;
            try {
                conn = ControladorPrincipal.mySQLFactory.getConnection();

                Build nuevaBuild = new Build();
                recolectarDatos.accept(nuevaBuild);

                int id = ControladorPrincipal.buildDAO.guardarBuild(conn, nuevaBuild);
                if (id > 0) {
                    mostrarAlerta("Éxito", "Build nueva guardada.");
                    cambiosSinGuardar = false;
                    buildEnEdicion = nuevaBuild;
                    buildEnEdicion.setId(id);

                    actualizarListaBuilds();

                    stage.close();
                } else {
                    mostrarAlerta("Error", "No se pudo guardar.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlerta("Error", "Fallo de BD.");
            } finally {
                if (conn != null) {
                    try {
                        ControladorPrincipal.mySQLFactory.releaseConnection(conn);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Evento Actualizar (Solo se configura si estamos editando)
        btnSobrescribir.setOnAction(e -> {
            if (txtNombre.getText().trim().isEmpty()) {
                mostrarAlerta("Error", "El nombre es obligatorio.");
                return;
            }

            Connection conn = null;
            try {
                conn = ControladorPrincipal.mySQLFactory.getConnection();

                recolectarDatos.accept(buildEnEdicion);

                if (ControladorPrincipal.buildDAO.modificarBuild(conn, buildEnEdicion)) {
                    mostrarAlerta("Éxito", "Build actualizada correctamente.");
                    cambiosSinGuardar = false;
                    actualizarListaBuilds();

                    stage.close();
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mostrarAlerta("Error", "Fallo de BD.");
            } finally {
                if (conn != null) {
                    try {
                        ControladorPrincipal.mySQLFactory.releaseConnection(conn);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        layout.getChildren().addAll(lblTitulo, txtNombre, lblDesc, txtDesc, chkPublica, btnGuardarNuevo);
        if (buildEnEdicion != null) {
            layout.getChildren().add(btnSobrescribir);
        }

        Scene scene = new Scene(layout, 400, buildEnEdicion != null ? 450 : 380);
        stage.setScene(scene);
        stage.show();

        actualizarListaBuilds();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void eliminarBuildSeleccionada() {
        Build seleccionada = listaBuilds.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Atención", "Debes seleccionar una build de la lista primero.");
            return;
        }

        if (seleccionada.getIdUsuario() != ControladorPrincipal.usuarioActual.getId()) {
            mostrarAlerta("Error", "No puedes eliminar una build pública que no te pertenece.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Build");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que deseas eliminar la build '" + seleccionada.getNombre() + "'?");

        java.util.Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Connection conn = null;
            try {
                conn = ControladorPrincipal.mySQLFactory.getConnection();

                if (ControladorPrincipal.buildDAO.eliminarBuild(conn, seleccionada.getId())) {

                    if (buildEnEdicion != null && buildEnEdicion.getId() == seleccionada.getId()) {
                        buildEnEdicion = null;
                        resetearMods();
                    }

                    mostrarAlerta("Éxito", "Build eliminada.");
                    actualizarListaBuilds();
                } else {
                    mostrarAlerta("Error", "No se pudo eliminar la build.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // AQUÍ ESTÁ LA MAGIA: Devolvemos la conexión a la piscina, NO la destruimos
                if (conn != null) {
                    try {
                        ControladorPrincipal.mySQLFactory.releaseConnection(conn);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    private void evaluarCambiosSinGuardar() {
        if (buildEnEdicion == null) {
            // Si es una build nueva, solo hay cambios reales si hay al menos 1 mod puesto.
            boolean hayMods = false;
            for (Mod m : modsEquipados) {
                if (m != null) {
                    hayMods = true;
                    break;
                }
            }
            cambiosSinGuardar = hayMods;
        } else {
            // Si hemos cargado una build de la base de datos, comprobamos si los 8 slots 
            // están EXACTAMENTE igual que como los cargamos.
            Integer[] idsOriginales = {
                buildEnEdicion.getMod1Id(), buildEnEdicion.getMod2Id(), buildEnEdicion.getMod3Id(), buildEnEdicion.getMod4Id(),
                buildEnEdicion.getMod5Id(), buildEnEdicion.getMod6Id(), buildEnEdicion.getMod7Id(), buildEnEdicion.getMod8Id()
            };
            
            boolean diferentes = false;
            for (int i = 0; i < 8; i++) {
                Integer idOrig = (idsOriginales[i] == null || idsOriginales[i] == 0) ? null : idsOriginales[i];
                Integer idAct = (modsEquipados[i] == null) ? null : modsEquipados[i].getId();
                
                if (idOrig == null && idAct != null) diferentes = true;
                if (idOrig != null && idAct == null) diferentes = true;
                if (idOrig != null && idAct != null && !idOrig.equals(idAct)) diferentes = true;
            }
            cambiosSinGuardar = diferentes;
        }
    }
    
    private boolean confirmarPerdidaCambios() {
        // Si no hay cambios, no molestamos al usuario y le dejamos pasar
        if (!cambiosSinGuardar) {
            return true;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cambios sin guardar");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas continuar? Perderás los mods que has modificado si no guardas la build.");

        // Mostrar el cuadro de diálogo y esperar respuesta
        java.util.Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        if (!confirmarPerdidaCambios()) {
            return;
        }
        try {
            ControladorPrincipal.usuarioActual = null;
            buildEnEdicion = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Ajustes de tamaño para el Login
            stage.setMinWidth(600);
            stage.setMinHeight(500);
            stage.setWidth(600);
            stage.setHeight(500);
            stage.centerOnScreen();

            stage.setScene(new Scene(root, 600, 500));
            stage.setTitle("Login - Warframe Builder");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cerrar sesión: " + e.getMessage());
        }
    }

}
