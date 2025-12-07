/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.factory.DAOFactory;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import modelo.dao.*;
import modelo.util.PasswordUtil;
import modelo.vo.*;
import vista.*;

/**
 *
 * @author aizpu
 */
public class ControladorPrincipal {

    public static DAOFactory mySQLFactory;
    
    public static ArmaDAO armDAO;
    public static BuildDAO buildDAO;
    public static ModDAO modDAO;
    public static TipoArmaDAO tipoArmaDAO;
    public static WarframeDAO warDAO;

    public static Login vista_login = new Login();
    public static Ventana vista_principal = new Ventana();

    public static Usuario usuarioActual = null;

    public static void iniciar() {
        vista_login.setVisible(true);
        vista_login.setLocationRelativeTo(null);
    }

    public static void iniciaFactory() {
        mySQLFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        
        armDAO = mySQLFactory.getArmaDAO();
        buildDAO = mySQLFactory.getBuildDAO();
        modDAO = mySQLFactory.getModDAO();
        tipoArmaDAO = mySQLFactory.getTipoArmaDAO();
        warDAO = mySQLFactory.getWarframeDAO();
    }

    public static void cerrarFactory() {
        try {
            mySQLFactory.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void inicarPrograma() {
        vista_principal.setVisible(true);
        vista_principal.setLocationRelativeTo(null);
        vista_login.setVisible(false);
    }

    public static void login() {
        String usuario = vista_login.getTxt_usuario().getText().trim();
        char[] contra = vista_login.getTxt_contrasena().getPassword();
        String contrasena = new String(contra).trim();

        Connection conn = null;
        try {
            conn = mySQLFactory.getConnection();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario user = usuarioDAO.obtenerPorNombreUsuario(conn, usuario);

            //USAR BCRYPT PARA VERIFICAR
            if (user != null && PasswordUtil.verificarPassword(contrasena, user.getContraseña())) {
                usuarioActual = user;
                inicarPrograma();
            } else {
                JOptionPane.showMessageDialog(vista_login,
                        "El usuario o la contraseña que has introducido no son correctos",
                        "Warning usuario",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(vista_login,
                    "Error al consultar la base de datos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            if (conn != null) {
                try {
                    mySQLFactory.releaseConnection(conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        vista_principal.setVisible(false);
        vista_login.setVisible(true);
        vista_login.getTxt_usuario().setText("");
        vista_login.getTxt_contrasena().setText("");
    }

    public static void registrarUsuario(String nombreUsuario, String contrasena) {
        Connection conn = null;
        try {
            conn = mySQLFactory.getConnection();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            // Validaciones
            if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            if (contrasena == null || contrasena.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "La contraseña no puede estar vacía");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            if (contrasena.length() < 6) {
                JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            // Verificar si ya existe
            if (usuarioDAO.existeNombreUsuario(conn, nombreUsuario)) {
                JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            //ENCRIPTAR CONTRASEÑA
            String hashContraseña = PasswordUtil.encriptarPassword(contrasena);

            // Crear usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreUsuario(nombreUsuario);
            nuevoUsuario.setContraseña(hashContraseña);

            int idGenerado = usuarioDAO.crearUsuario(conn, nuevoUsuario);

            // Commit
            if (!conn.getAutoCommit()) {
                conn.commit();
            }

            mySQLFactory.releaseConnection(conn);

            if (idGenerado > 0) {
                JOptionPane.showMessageDialog(null, "Usuario creado correctamente. Ya puedes iniciar sesión.");
            } else {
                JOptionPane.showMessageDialog(null, "Error al crear el usuario");
            }

        } catch (Exception ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + ex.getMessage());
        } finally {
            if (conn != null) {
                try {
                    mySQLFactory.releaseConnection(conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void listarArmas() {
//        try {
//            Connection conn;
//            conn = mySQLFactory.getConnection();
//
////            armDAO.listar(conn, vista_principal.getTxt_area());
//            mySQLFactory.releaseConnection(conn);
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(null, "Entrada de datos incorrecta");
//        } catch (Exception ex) {
//            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    public static void cargarArmas() {
        try {
            Connection conn;
            conn = mySQLFactory.getConnection();

            armDAO.cargarArmasCombo(conn, vista_principal.getCmb_armas());

            mySQLFactory.releaseConnection(conn);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Entrada de datos incorrecta");
        } catch (Exception ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarWarframes() {
        try {
            Connection conn;
            conn = mySQLFactory.getConnection();

            warDAO.cargarWarframesCombo(conn, vista_principal.getCmb_warframes());

            mySQLFactory.releaseConnection(conn);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Entrada de datos incorrecta");
        } catch (Exception ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cargarMods() {
        try {
            Connection conn = mySQLFactory.getConnection();

            //Obtener arma seleccionada
            Arma arma = (Arma) vista_principal.getCmb_armas().getSelectedItem();
            if (arma == null) {
                return;
            }

            //Obtener su tipo_arma completo
            TipoArma tipoArma = tipoArmaDAO.obtenerTipoArmaPorId(conn, arma.getIdTipoArma());
            if (tipoArma == null) {
                return;
            }

            String categoriaArma = tipoArma.getCategoria(); // Primaria / Secundaria / Melee
            int idTipoArma = tipoArma.getId(); // Rifle / Escopeta / etc

            //Pedir los mods filtrados al DAO
            List<Mod> modsFiltrados = modDAO.obtenerModsParaArma(conn, categoriaArma, idTipoArma);

            //Cargar los mods en los combos
            JComboBox[] combos = vista_principal.getCombosMods();
            for (JComboBox combo : combos) {

                combo.removeAllItems();

                // Mod vacío para "Selecciona un mod"
                Mod vacio = new Mod();
                vacio.setId(0);
                vacio.setNombre("Selecciona un mod");
                vacio.setEfecto("");
                combo.addItem(vacio);

                // Añadir mods válidos
                for (Mod m : modsFiltrados) {
                    combo.addItem(m);
                }

                combo.setSelectedIndex(0);
            }

            mySQLFactory.releaseConnection(conn);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error cargando los mods.");
        }
    }

    public static void cargarStatsBaseArma() {

        try {
            Connection conn;
            conn = mySQLFactory.getConnection();

            armDAO.cargarStatsBaseArmas(conn, vista_principal.getCmb_armas(), vista_principal.getLabelsArray(), vista_principal);

            mySQLFactory.releaseConnection(conn);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Entrada de datos incorrecta");
        } catch (Exception ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void resetearCombosMods() {
        //TODO
    }

    public static void actualizarStatsArma() {
        try {
            Connection conn = mySQLFactory.getConnection();

            Arma arma = (Arma) vista_principal.getCmb_armas().getSelectedItem();
            if (arma == null) {
                mySQLFactory.releaseConnection(conn);
                return;
            }

            JLabel[] labels = vista_principal.getLabelsArray();
            JComboBox[] combosMods = vista_principal.getCombosMods();

            // Acumulamos los mods
            Map<String, Double> acumulados = new HashMap<>();
            String[] posibles = {"impacto", "perforante", "cortante", "critico", "mult_critico", "precision", "estado", "cadencia"};
            for (String k : posibles) {
                acumulados.put(k, 0.0);
            }

            // Variables especiales
            double dañoGenerico = 0.0;
            double multishot = 0.0;

            // Lista para almacenar elementos en orden
            List<String> elementosEnOrden = new ArrayList<>();

            for (JComboBox combo : combosMods) {
                Mod mod = (Mod) combo.getSelectedItem();
                if (mod == null || mod.getId() == 0) {
                    continue;
                }

                List<ModEfecto> efectos = modDAO.obtenerEfectosDeMod(conn, mod.getId());
                if (efectos == null) {
                    continue;
                }

                for (ModEfecto e : efectos) {
                    String campo = e.getCampo() != null ? e.getCampo().toLowerCase() : "";
                    String atributo = e.getAtributo() != null ? e.getAtributo().toLowerCase() : null;
                    double valor = e.getValor();

                    if ("daño".equals(campo)) {
                        if (atributo == null || "daño".equals(atributo)) {
                            // Daño genérico
                            dañoGenerico += valor;
                        } else {
                            switch (atributo) {
                                case "impacto":
                                case "perforante":
                                case "cortante":
                                    acumulados.put(atributo, acumulados.get(atributo) + valor);
                                    break;
                                case "frio":
                                case "electrico":
                                case "calor":
                                case "toxina":
                                    elementosEnOrden.add(atributo + ":" + valor);
                                    break;
                            }
                        }
                    } else if ("multishot".equals(campo)) {
                        multishot += valor;
                    } else {
                        // Otros stats
                        String stat = mapearAtributo(atributo != null ? atributo : campo);
                        if (stat != null && acumulados.containsKey(stat)) {
                            acumulados.put(stat, acumulados.get(stat) + valor);
                        }
                    }
                }
            }

            // Combinar elementos
            Map<String, Double> elementosCombinados = combinarElementos(elementosEnOrden);

            // Calcular daño fisico total
            double dañoFisicoBase = vista_principal.getValorBase("impacto")
                    + vista_principal.getValorBase("perforante")
                    + vista_principal.getValorBase("cortante");

            double dañoFisicoTotal = dañoFisicoBase * (1 + dañoGenerico);

            // Multiplicamos todo el daño en base al multidisparo
            double multiplicadorMultishot = (1 + multishot);

            // Calcular los resultados finales
            List<String> textosVisibles = new ArrayList<>();

            // Daños físicos
            for (String stat : new String[]{"impacto", "perforante", "cortante"}) {
                double base = vista_principal.getValorBase(stat);
                double pctEspecifico = acumulados.get(stat);
                double resultado = base * (1 + dañoGenerico + pctEspecifico);
                // Aplicar multidisparo
                resultado *= multiplicadorMultishot;
                if (resultado > 0) {
                    textosVisibles.add(getTextoStat(stat, resultado));
                }
            }

            // Elementos base del arma
            for (String elem : new String[]{"frio", "electrico", "calor", "toxina"}) {
                double base = vista_principal.getValorBase(elem);
                if (base > 0) {
                    double porcentajeExtra = 0.0;
                    for (String elemMod : elementosEnOrden) {
                        String[] partes = elemMod.split(":");
                        if (partes[0].equals(elem)) {
                            porcentajeExtra += Double.parseDouble(partes[1]);
                        }
                    }
                    double resultado = base * (1 + dañoGenerico + porcentajeExtra);
                    // Aplicar multidisparo
                    resultado *= multiplicadorMultishot;
                    textosVisibles.add(getTextoStat(elem, resultado));
                }
            }

            // Elementos combinados
            for (Map.Entry<String, Double> entry : elementosCombinados.entrySet()) {
                String elemento = entry.getKey();
                double porcentaje = entry.getValue();
                double resultado = dañoFisicoTotal * porcentaje;
                // Aplicar multidisparo
                resultado *= multiplicadorMultishot;
                if (resultado > 0) {
                    textosVisibles.add(getTextoStat(elemento, resultado));
                }
            }

            // Stats no-daño (estos NO se ven afectados por multishot)
            for (String stat : new String[]{"critico", "mult_critico", "estado", "precision", "cadencia"}) {
                double base = vista_principal.getValorBase(stat);
                double pct = acumulados.get(stat);
                double resultado = base * (1 + pct);
                if (resultado > 0) {
                    textosVisibles.add(getTextoStat(stat, resultado));
                }
            }

            // Mostrar el multidisparo al final como info adicional (opcional)
            if (multishot > 0) {
                textosVisibles.add(String.format("Multishot: %.0f%%", multishot * 100));
            }

            // Mostrar stats en los labels
            int i = 0;
            for (; i < textosVisibles.size() && i < labels.length; i++) {
                labels[i].setText(textosVisibles.get(i));
                labels[i].setVisible(true);
            }
            for (; i < labels.length; i++) {
                labels[i].setVisible(false);
            }

            vista_principal.panel_armas.revalidate();
            vista_principal.panel_armas.repaint();

            mySQLFactory.releaseConnection(conn);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar las stats del arma");
        }
    }

    private static String mapearAtributo(String atributoMod) {
        switch (atributoMod.toLowerCase()) {
            case "daño":
                return "impacto";
            case "frio":
                return "frio";
            case "electrico":
                return "electrico";
            case "calor":
                return "calor";
            case "toxina":
                return "toxina";
            case "critico":
                return "critico";
            case "mult_critico":
                return "mult_critico";
            case "precision":
                return "precision";
            case "cadencia":
                return "cadencia";
            default:
                return atributoMod;
        }
    }

    private static String getTextoStat(String stat, double valor) {
        switch (stat) {
            case "impacto":
                return "Impacto: " + String.format("%.2f", valor);
            case "perforante":
                return "Perforante: " + String.format("%.2f", valor);
            case "cortante":
                return "Cortante: " + String.format("%.2f", valor);
            case "frio":
                return "Frío: " + String.format("%.2f", valor);
            case "electrico":
                return "Eléctrico: " + String.format("%.2f", valor);
            case "calor":
                return "Calor: " + String.format("%.2f", valor);
            case "toxina":
                return "Toxina: " + String.format("%.2f", valor);
            // Elementos combinados
            case "viral":
                return "Viral: " + String.format("%.2f", valor);
            case "corrosivo":
                return "Corrosivo: " + String.format("%.2f", valor);
            case "gas":
                return "Gas: " + String.format("%.2f", valor);
            case "radiacion":
                return "Radiación: " + String.format("%.2f", valor);
            case "magnetico":
                return "Magnético: " + String.format("%.2f", valor);
            case "explosion":
                return "Explosión: " + String.format("%.2f", valor);
            // Stats
            case "critico":
                return "Crítico: " + String.format("%.2f", valor);
            case "mult_critico":
                return "Mult. Crítico: " + String.format("%.2f", valor);
            case "estado":
                return "Estado: " + String.format("%.2f", valor);
            case "precision":
                return "Precisión: " + String.format("%.2f", valor);
            case "cadencia":
                return "Cadencia: " + String.format("%.2f", valor);
            default:
                return stat + ": " + String.format("%.2f", valor);
        }
    }

    /**
     * Combina elementos según el orden y las reglas de Warframe Primero agrupa
     * elementos iguales, luego intenta combinarlos en orden
     */
    private static Map<String, Double> combinarElementos(List<String> elementosEnOrden) {
        Map<String, Double> resultado = new HashMap<>();

        if (elementosEnOrden.isEmpty()) {
            return resultado;
        }

        // Agrupar elementos iguales en orden de aparición
        List<String> elementosAgrupados = new ArrayList<>();
        Map<String, Double> acumuladorTemporal = new HashMap<>();
        List<String> ordenAparicion = new ArrayList<>();

        for (String elemConValor : elementosEnOrden) {
            String[] partes = elemConValor.split(":");
            String elemento = partes[0];
            double valor = Double.parseDouble(partes[1]);

            if (!acumuladorTemporal.containsKey(elemento)) {
                ordenAparicion.add(elemento);
            }
            acumuladorTemporal.put(elemento, acumuladorTemporal.getOrDefault(elemento, 0.0) + valor);
        }

        // Reconstruir lista agrupada en orden de primera aparición
        for (String elem : ordenAparicion) {
            elementosAgrupados.add(elem + ":" + acumuladorTemporal.get(elem));
        }

        // Intentar combinar elementos en orden
        List<String> pool = new ArrayList<>(elementosAgrupados);

        while (!pool.isEmpty()) {
            String primero = pool.remove(0);
            String[] partes1 = primero.split(":");
            String elem1 = partes1[0];
            double valor1 = Double.parseDouble(partes1[1]);

            boolean combinado = false;

            // Buscar si hay otro elemento para combinar
            for (int i = 0; i < pool.size(); i++) {
                String segundo = pool.get(i);
                String[] partes2 = segundo.split(":");
                String elem2 = partes2[0];
                double valor2 = Double.parseDouble(partes2[1]);

                String combinacion = obtenerCombinacion(elem1, elem2);

                if (combinacion != null) {
                    // Se pueden combinar
                    pool.remove(i);
                    resultado.put(combinacion, resultado.getOrDefault(combinacion, 0.0) + valor1 + valor2);
                    combinado = true;
                    break;
                }
            }

            if (!combinado) {
                // No se pudo combinar, añadir elemento base
                resultado.put(elem1, resultado.getOrDefault(elem1, 0.0) + valor1);
            }
        }

        return resultado;
    }

    /**
     * Retorna el nombre del elemento combinado, o null si no se pueden combinar
     */
    private static String obtenerCombinacion(String elem1, String elem2) {
        // Normalizar orden (alfabético para evitar duplicados)
        String a = elem1.compareTo(elem2) < 0 ? elem1 : elem2;
        String b = elem1.compareTo(elem2) < 0 ? elem2 : elem1;

        String clave = a + "+" + b;

        switch (clave) {
            case "calor+frio":
                return "explosion";
            case "electrico+frio":
                return "magnetico";
            case "frio+toxina":
                return "viral";
            case "calor+electrico":
                return "radiacion";
            case "calor+toxina":
                return "gas";
            case "electrico+toxina":
                return "corrosivo";
            default:
                return null;
        }
    }

    public static void actualizarDisponibilidadMods() {
        try {
            Connection conn = mySQLFactory.getConnection();

            Arma arma = (Arma) vista_principal.getCmb_armas().getSelectedItem();
            if (arma == null) {
                mySQLFactory.releaseConnection(conn);
                return;
            }

            TipoArma tipoArma = tipoArmaDAO.obtenerTipoArmaPorId(conn, arma.getIdTipoArma());
            if (tipoArma == null) {
                mySQLFactory.releaseConnection(conn);
                return;
            }

            String categoriaArma = tipoArma.getCategoria();
            int idTipoArma = tipoArma.getId();

            List<Mod> modsFiltrados = modDAO.obtenerModsParaArma(conn, categoriaArma, idTipoArma);

            // Obtener mods seleccionados
            JComboBox[] combos = vista_principal.getCombosMods();
            List<Integer> modsSeleccionados = new ArrayList<>();

            for (JComboBox combo : combos) {
                Mod modSeleccionado = (Mod) combo.getSelectedItem();
                if (modSeleccionado != null && modSeleccionado.getId() != 0) {
                    modsSeleccionados.add(modSeleccionado.getId());
                }
            }

            // Obtener mods incompatibles con los seleccionados
            List<Integer> modsIncompatibles = new ArrayList<>();
            for (Integer idModSeleccionado : modsSeleccionados) {
                List<Integer> incompatibles = modDAO.obtenerModsIncompatibles(conn, idModSeleccionado);
                modsIncompatibles.addAll(incompatibles);
            }

            // Recargar cada combo
            for (int i = 0; i < combos.length; i++) {
                JComboBox<Mod> combo = combos[i];
                Mod seleccionadoActual = (Mod) combo.getSelectedItem();

                java.awt.event.ActionListener[] listeners = combo.getActionListeners();
                for (java.awt.event.ActionListener listener : listeners) {
                    combo.removeActionListener(listener);
                }

                combo.removeAllItems();

                Mod vacio = new Mod();
                vacio.setId(0);
                vacio.setNombre("Selecciona un mod");
                vacio.setEfecto("");
                combo.addItem(vacio);

                for (Mod m : modsFiltrados) {
                    boolean estaEnOtroCombo = modsSeleccionados.contains(m.getId());
                    boolean esElActual = seleccionadoActual != null && m.getId() == seleccionadoActual.getId();
                    boolean esIncompatible = modsIncompatibles.contains(m.getId()); // ⭐ NUEVO

                    // No añadir si está en otro combo, es incompatible, SALVO que sea el actual
                    if ((!estaEnOtroCombo && !esIncompatible) || esElActual) {
                        combo.addItem(m);
                    }
                }

                if (seleccionadoActual != null) {
                    for (int j = 0; j < combo.getItemCount(); j++) {
                        Mod item = (Mod) combo.getItemAt(j);
                        if (item.getId() == seleccionadoActual.getId()) {
                            combo.setSelectedIndex(j);
                            break;
                        }
                    }
                }

                for (java.awt.event.ActionListener listener : listeners) {
                    combo.addActionListener(listener);
                }
            }

            mySQLFactory.releaseConnection(conn);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar disponibilidad de mods");
        }
    }

    public static void cargarModsWarframe() {
        try {
            Connection conn = mySQLFactory.getConnection();

            // Obtener warframe seleccionado
            Warframe warframe = (Warframe) vista_principal.getCmb_warframes().getSelectedItem();
            if (warframe == null) {
                mySQLFactory.releaseConnection(conn);
                return;
            }

            // Obtener mods de Warframe
            List<Mod> modsFiltrados = modDAO.obtenerModsParaWarframe(conn);

            // Cargar los mods en los combos
            JComboBox[] combos = vista_principal.getCombosModsWarframe();
            for (JComboBox combo : combos) {
                combo.removeAllItems();

                // Mod vacío
                Mod vacio = new Mod();
                vacio.setId(0);
                vacio.setNombre("Selecciona un mod");
                vacio.setEfecto("");
                combo.addItem(vacio);

                // Añadir mods válidos
                for (Mod m : modsFiltrados) {
                    combo.addItem(m);
                }

                combo.setSelectedIndex(0);
            }

            mySQLFactory.releaseConnection(conn);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error cargando los mods de Warframe.");
        }
    }

    public static void cargarStatsBaseWarframe() {
        try {
            Connection conn = mySQLFactory.getConnection();

            warDAO.cargarStatsBaseWarframe(conn, vista_principal.getCmb_warframes(),
                    vista_principal.getLabelsWarframe(), vista_principal);

            mySQLFactory.releaseConnection(conn);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Entrada de datos incorrecta");
        } catch (Exception ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void actualizarDisponibilidadModsWarframe() {
        try {
            Connection conn = mySQLFactory.getConnection();

            Warframe warframe = (Warframe) vista_principal.getCmb_warframes().getSelectedItem();
            if (warframe == null) {
                mySQLFactory.releaseConnection(conn);
                return;
            }

            // Obtener lista de mods de Warframe
            List<Mod> modsFiltrados = modDAO.obtenerModsParaWarframe(conn);

            // Obtener los mods actualmente seleccionados
            JComboBox[] combos = vista_principal.getCombosModsWarframe();
            List<Integer> modsSeleccionados = new ArrayList<>();

            for (JComboBox combo : combos) {
                Mod modSeleccionado = (Mod) combo.getSelectedItem();
                if (modSeleccionado != null && modSeleccionado.getId() != 0) {
                    modsSeleccionados.add(modSeleccionado.getId());
                }
            }

            // Obtener mods incompatibles con los seleccionados
            List<Integer> modsIncompatibles = new ArrayList<>();
            for (Integer idModSeleccionado : modsSeleccionados) {
                List<Integer> incompatibles = modDAO.obtenerModsIncompatibles(conn, idModSeleccionado);
                modsIncompatibles.addAll(incompatibles);
            }

            // Recargar cada combo manteniendo su selección
            for (int i = 0; i < combos.length; i++) {
                JComboBox<Mod> combo = combos[i];
                Mod seleccionadoActual = (Mod) combo.getSelectedItem();

                // Remover listener temporalmente
                java.awt.event.ActionListener[] listeners = combo.getActionListeners();
                for (java.awt.event.ActionListener listener : listeners) {
                    combo.removeActionListener(listener);
                }

                // Limpiar y recargar
                combo.removeAllItems();

                // Añadir opción vacía
                Mod vacio = new Mod();
                vacio.setId(0);
                vacio.setNombre("Selecciona un mod");
                vacio.setEfecto("");
                combo.addItem(vacio);

                // Añadir mods disponibles
                for (Mod m : modsFiltrados) {
                    boolean estaEnOtroCombo = modsSeleccionados.contains(m.getId());
                    boolean esElActual = seleccionadoActual != null && m.getId() == seleccionadoActual.getId();
                    boolean esIncompatible = modsIncompatibles.contains(m.getId()); // ⭐ NUEVO

                    // No añadir si está en otro combo, es incompatible, SALVO que sea el actual
                    if ((!estaEnOtroCombo && !esIncompatible) || esElActual) {
                        combo.addItem(m);
                    }
                }

                // Restaurar selección
                if (seleccionadoActual != null) {
                    for (int j = 0; j < combo.getItemCount(); j++) {
                        Mod item = (Mod) combo.getItemAt(j);
                        if (item.getId() == seleccionadoActual.getId()) {
                            combo.setSelectedIndex(j);
                            break;
                        }
                    }
                }

                // Reañadir listeners
                for (java.awt.event.ActionListener listener : listeners) {
                    combo.addActionListener(listener);
                }
            }

            mySQLFactory.releaseConnection(conn);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar disponibilidad de mods");
        }
    }

    public static void actualizarStatsWarframe() {
        try {
            Connection conn = mySQLFactory.getConnection();

            Warframe warframe = (Warframe) vista_principal.getCmb_warframes().getSelectedItem();
            if (warframe == null) {
                mySQLFactory.releaseConnection(conn);
                return;
            }

            JLabel[] labels = vista_principal.getLabelsWarframe();
            JComboBox[] combosMods = vista_principal.getCombosModsWarframe();

            // Acumular mods
            Map<String, Double> acumulados = new HashMap<>();
            String[] posibles = {"salud", "escudo", "armadura", "energia", "duracion", "eficiencia", "fuerza", "rango"};
            for (String k : posibles) {
                acumulados.put(k, 0.0);
            }

            for (JComboBox combo : combosMods) {
                Mod mod = (Mod) combo.getSelectedItem();
                if (mod == null || mod.getId() == 0) {
                    continue;
                }

                List<ModEfecto> efectos = modDAO.obtenerEfectosDeMod(conn, mod.getId());
                if (efectos == null) {
                    continue;
                }

                for (ModEfecto e : efectos) {
                    String campo = e.getCampo() != null ? e.getCampo().toLowerCase() : "";
                    String atributo = e.getAtributo() != null ? e.getAtributo().toLowerCase() : null;
                    double valor = e.getValor();

                    // Mapear el campo/atributo a la stat correspondiente
                    String stat = mapearAtributoWarframe(atributo != null ? atributo : campo);
                    if (stat != null && acumulados.containsKey(stat)) {
                        acumulados.put(stat, acumulados.get(stat) + valor);
                    }
                }
            }

            // Calcular resultados finales
            List<String> textosVisibles = new ArrayList<>();

            // Stats BASE (valores enteros)
            for (String stat : new String[]{"salud", "escudo", "armadura", "energia"}) {
                double base = vista_principal.getValorBaseWarframe(stat);
                double pct = acumulados.get(stat);
                double resultado = base * (1 + pct);
                if (resultado > 0 || base > 0) {
                    textosVisibles.add(getTextoStatWarframe(stat, resultado));
                }
            }

            // Stats de HABILIDADES (porcentajes)
            for (String stat : new String[]{"duracion", "eficiencia", "fuerza", "rango"}) {
                double base = vista_principal.getValorBaseWarframe(stat);
                double pct = acumulados.get(stat);
                double resultado = base * (1 + pct);

                // Límites de Warframe
                resultado = aplicarLimitesWarframe(stat, resultado);

                textosVisibles.add(getTextoStatWarframe(stat, resultado));
            }

            // Mostrar en los labels
            int i = 0;
            for (; i < textosVisibles.size() && i < labels.length; i++) {
                labels[i].setText(textosVisibles.get(i));
                labels[i].setVisible(true);
            }
            for (; i < labels.length; i++) {
                labels[i].setVisible(false);
            }

            vista_principal.panel_warframes.revalidate();
            vista_principal.panel_warframes.repaint();

            mySQLFactory.releaseConnection(conn);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Mapea el atributo del mod de Warframe a la stat correspondiente
     */
    private static String mapearAtributoWarframe(String atributoMod) {
        if (atributoMod == null) {
            return null;
        }

        switch (atributoMod.toLowerCase()) {
            case "salud":
                return "salud";
            case "escudo":
                return "escudo";
            case "armadura":
                return "armadura";
            case "energia":
            case "energia_maxima":
                return "energia";
            case "duracion":
                return "duracion";
            case "eficiencia":
                return "eficiencia";
            case "fuerza":
                return "fuerza";
            case "rango":
                return "rango";
            default:
                return atributoMod;
        }
    }

    /**
     * Aplica los límites de stats de Warframe (Eficiencia max 175%, min 25%)
     */
    private static double aplicarLimitesWarframe(String stat, double valor) {
        switch (stat) {
            case "eficiencia":
                if (valor < 0.10) {
                    return 0.10;
                }
                return valor;
            case "duracion":
                if (valor < 0.10) {
                    return 0.10;
                }
                return valor;
            case "fuerza":
                if (valor < 0.10) {
                    return 0.10;
                }
                return valor;
            case "rango":
                if (valor < 0.10) {
                    return 0.10;
                }
                return valor;
            default:
                return valor;
        }
    }

    /**
     * Convierte una stat de Warframe en texto formateado
     */
    private static String getTextoStatWarframe(String stat, double valor) {
        switch (stat) {
            case "salud":
                return "Salud: " + String.format("%.0f", valor);
            case "escudo":
                return "Escudo: " + String.format("%.0f", valor);
            case "armadura":
                return "Armadura: " + String.format("%.0f", valor);
            case "energia":
                return "Energía: " + String.format("%.0f", valor);
            case "duracion":
                return "Duración: " + String.format("%.0f%%", valor * 100);
            case "eficiencia":
                return "Eficiencia: " + String.format("%.0f%%", valor * 100);
            case "fuerza":
                return "Fuerza: " + String.format("%.0f%%", valor * 100);
            case "rango":
                return "Rango: " + String.format("%.0f%%", valor * 100);
            default:
                return stat + ": " + String.format("%.2f", valor);
        }
    }

    /**
     * Guardar la build actual de arma
     */
    public static void guardarBuildArma(String nombreBuild, String descripcion) {
        Connection conn = null;
        try {
            conn = mySQLFactory.getConnection();

            Arma arma = (Arma) vista_principal.getCmb_armas().getSelectedItem();
            if (arma == null) {
                JOptionPane.showMessageDialog(null, "Selecciona un arma primero");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            JComboBox[] combos = vista_principal.getCombosMods();

            // Validar que tenga al menos un mod
            boolean tieneAlgunMod = false;
            for (JComboBox combo : combos) {
                Mod mod = (Mod) combo.getSelectedItem();
                if (mod != null && mod.getId() != 0) {
                    tieneAlgunMod = true;
                    break;
                }
            }

            if (!tieneAlgunMod) {
                int respuesta = JOptionPane.showConfirmDialog(null,
                        "La build no tiene ningún mod equipado. ¿Quieres guardarla de todas formas?",
                        "Build vacía",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (respuesta != JOptionPane.YES_OPTION) {
                    mySQLFactory.releaseConnection(conn);
                    return;
                }
            }

            Build build = new Build();
            build.setIdUsuario(usuarioActual.getId());
            build.setNombre(nombreBuild);
            build.setTipo("Arma");
            build.setIdArma(arma.getId());
            build.setIdWarframe(null);
            build.setDescripcion(descripcion);

            build.setMod1Id(getModIdFromCombo(combos[0]));
            build.setMod2Id(getModIdFromCombo(combos[1]));
            build.setMod3Id(getModIdFromCombo(combos[2]));
            build.setMod4Id(getModIdFromCombo(combos[3]));
            build.setMod5Id(getModIdFromCombo(combos[4]));
            build.setMod6Id(getModIdFromCombo(combos[5]));
            build.setMod7Id(getModIdFromCombo(combos[6]));
            build.setMod8Id(getModIdFromCombo(combos[7]));

            int idBuildGenerado = buildDAO.guardarBuild(conn, build);

            if (!conn.getAutoCommit()) {
                conn.commit();
            }

            mySQLFactory.releaseConnection(conn);

            if (idBuildGenerado > 0) {
                JOptionPane.showMessageDialog(null, "Build guardada correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar la build");
            }

        } catch (Exception ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar la build: " + ex.getMessage());
        } finally {
            if (conn != null) {
                mySQLFactory.releaseConnection(conn);
            }
        }
    }

    /**
     * Guardar la build actual de warframe
     */
    public static void guardarBuildWarframe(String nombreBuild, String descripcion) {
        try {
            Connection conn = mySQLFactory.getConnection();

            // Obtener warframe seleccionado
            Warframe warframe = (Warframe) vista_principal.getCmb_warframes().getSelectedItem();
            if (warframe == null) {
                JOptionPane.showMessageDialog(null, "Selecciona un warframe primero");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            // Obtener mods seleccionados
            JComboBox[] combos = vista_principal.getCombosModsWarframe();

            boolean tieneAlgunMod = false;
            for (JComboBox combo : combos) {
                Mod mod = (Mod) combo.getSelectedItem();
                if (mod != null && mod.getId() != 0) {
                    tieneAlgunMod = true;
                    break;
                }
            }

            if (!tieneAlgunMod) {
                int respuesta = JOptionPane.showConfirmDialog(null,
                        "La build no tiene ningún mod equipado. ¿Quieres guardarla de todas formas?",
                        "Build vacía",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (respuesta != JOptionPane.YES_OPTION) {
                    mySQLFactory.releaseConnection(conn);
                    return;
                }
            }

            // Crear objeto Build
            Build build = new Build();
            build.setIdUsuario(usuarioActual.getId());
            build.setNombre(nombreBuild);
            build.setTipo("Warframe");
            build.setIdWarframe(warframe.getId());
            build.setIdArma(null);
            build.setDescripcion(descripcion);

            // Asignar mods
            build.setMod1Id(getModIdFromCombo(combos[0]));
            build.setMod2Id(getModIdFromCombo(combos[1]));
            build.setMod3Id(getModIdFromCombo(combos[2]));
            build.setMod4Id(getModIdFromCombo(combos[3]));
            build.setMod5Id(getModIdFromCombo(combos[4]));
            build.setMod6Id(getModIdFromCombo(combos[5]));
            build.setMod7Id(getModIdFromCombo(combos[6]));
            build.setMod8Id(getModIdFromCombo(combos[7]));

            // Guardar en BD
            int idBuildGenerado = buildDAO.guardarBuild(conn, build);

            mySQLFactory.releaseConnection(conn);

            if (idBuildGenerado > 0) {
                JOptionPane.showMessageDialog(null, "Build guardada correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar la build");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar la build: " + ex.getMessage());
        }
    }

    /**
     * Listar builds del usuario actual por tipo
     */
    public static void cargarListaBuilds(String tipo, javax.swing.JList<Build> listBuilds) {
        try {
            Connection conn = mySQLFactory.getConnection();

            List<Build> builds = buildDAO.listarBuildsPorTipo(conn, usuarioActual.getId(), tipo);

            javax.swing.DefaultListModel<Build> modelo = new javax.swing.DefaultListModel<>();
            for (Build b : builds) {
                modelo.addElement(b);
            }
            listBuilds.setModel(modelo);

            mySQLFactory.releaseConnection(conn);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar builds: " + ex.getMessage());
        }
    }

    /**
     * Cargar una build completa (aplicar arma/warframe y mods)
     */
    public static boolean cargarBuild(int idBuild) {
        try {
            Connection conn = mySQLFactory.getConnection();

            Build build = buildDAO.obtenerBuild(conn, idBuild);
            if (build == null) {
                JOptionPane.showMessageDialog(null, "Build no encontrada");
                mySQLFactory.releaseConnection(conn);
                return false;
            }

            if ("Arma".equals(build.getTipo())) {
                // Cargar build de arma
                cargarBuildArma(build);
            } else if ("Warframe".equals(build.getTipo())) {
                // Cargar build de warframe
                cargarBuildWarframe(build);
            }

            mySQLFactory.releaseConnection(conn);
            //JOptionPane.showMessageDialog(null, "Build '" + build.getNombre() + "' cargada correctamente");
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar la build: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Cargar build de arma
     */
    private static void cargarBuildArma(Build build) {
        // Cambiar al panel de armas si no está visible
        vista_principal.btn_armas.doClick();

        // Seleccionar el arma
        JComboBox<Arma> cmbArmas = vista_principal.getCmb_armas();
        for (int i = 0; i < cmbArmas.getItemCount(); i++) {
            Arma arma = cmbArmas.getItemAt(i);
            if (arma != null && arma.getId() == build.getIdArma()) {
                cmbArmas.setSelectedIndex(i);
                break;
            }
        }

        // Aplicar los mods
        JComboBox[] combos = vista_principal.getCombosMods();

        aplicarModACombo(combos[0], build.getMod1Id());
        aplicarModACombo(combos[1], build.getMod2Id());
        aplicarModACombo(combos[2], build.getMod3Id());
        aplicarModACombo(combos[3], build.getMod4Id());
        aplicarModACombo(combos[4], build.getMod5Id());
        aplicarModACombo(combos[5], build.getMod6Id());
        aplicarModACombo(combos[6], build.getMod7Id());
        aplicarModACombo(combos[7], build.getMod8Id());
    }

    /**
     * Cargar build de warframe
     */
    private static void cargarBuildWarframe(Build build) {

        try {
            // Cambiar al panel de warframes
            vista_principal.btn_warframes.doClick();

            // Seleccionar el warframe
            JComboBox<Warframe> cmbWarframes = vista_principal.getCmb_warframes();

            boolean encontrado = false;
            for (int i = 0; i < cmbWarframes.getItemCount(); i++) {
                Warframe wf = cmbWarframes.getItemAt(i);
                if (wf != null) {
                    if (wf.getId() == build.getIdWarframe()) {
                        cmbWarframes.setSelectedIndex(i);
                        encontrado = true;
                        break;
                    }
                }
            }

            if (!encontrado) {
                return;
            }

            // Aplicar los mods
            JComboBox[] combos = vista_principal.getCombosModsWarframe();

            aplicarModACombo(combos[0], build.getMod1Id());
            aplicarModACombo(combos[1], build.getMod2Id());
            aplicarModACombo(combos[2], build.getMod3Id());
            aplicarModACombo(combos[3], build.getMod4Id());
            aplicarModACombo(combos[4], build.getMod5Id());
            aplicarModACombo(combos[5], build.getMod6Id());
            aplicarModACombo(combos[6], build.getMod7Id());
            aplicarModACombo(combos[7], build.getMod8Id());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Eliminar una build
     */
    public static void eliminarBuild(int idBuild) {
        try {
            int confirm = JOptionPane.showConfirmDialog(null,
                    "¿Estás seguro de que quieres eliminar esta build?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Connection conn = mySQLFactory.getConnection();
                buildDAO.eliminarBuild(conn, idBuild);
                mySQLFactory.releaseConnection(conn);

                JOptionPane.showMessageDialog(null, "Build eliminada correctamente");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar la build: " + ex.getMessage());
        }
    }

    /**
     * Obtiene el ID del mod seleccionado en un combo (null si no hay mod)
     */
    private static Integer getModIdFromCombo(JComboBox combo) {
        Mod mod = (Mod) combo.getSelectedItem();
        if (mod == null || mod.getId() == 0) {
            return null;
        }
        return mod.getId();
    }

    /**
     * Aplica un mod a un combo por su ID
     */
    private static void aplicarModACombo(JComboBox combo, Integer idMod) {
        try {
            if (idMod == null) {
                combo.setSelectedIndex(0);
                return;
            }


            // Buscar el mod en el combo
            for (int i = 0; i < combo.getItemCount(); i++) {
                Mod mod = (Mod) combo.getItemAt(i);
                if (mod != null && mod.getId() == idMod) {
                    combo.setSelectedIndex(i);
                    return;
                }
            }

            // Si no se encuentra, dejar vacío
            combo.setSelectedIndex(0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Resetear todos los mods de armas
     */
    public static void resetearModsArma() {
        JComboBox[] combos = vista_principal.getCombosMods();
        Object[] opciones = {"Sí", "No"};

        int respuesta = JOptionPane.showOptionDialog(
                null,
                "¿Estas seguro de que quieres limpiar la lista de mods?",
                "Confirmar limpiar la lista de mods",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[1]);

        if (respuesta == 0) {
            for (JComboBox combo : combos) {
                combo.setSelectedIndex(0);
            }
        }
    }

    /**
     * Resetear todos los mods de warframe
     */
    public static void resetearModsWarframe() {
        JComboBox[] combos = vista_principal.getCombosModsWarframe();
        Object[] opciones = {"Sí", "No"};

        int respuesta = JOptionPane.showOptionDialog(
                null,
                "¿Estas seguro de que quieres limpiar la lista de mods?",
                "Confirmar limpiar la lista de mods",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[1]);

        if (respuesta == 0) {
            for (JComboBox combo : combos) {
                combo.setSelectedIndex(0);
            }
        }
    }
    
/**
 * Actualizar stats de arma en segundo plano (con SwingWorker)
 */
public static void actualizarStatsArmaAsync() {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            actualizarStatsArma();
            return null;
        }
    };
    worker.execute();
}

/**
 * Actualizar stats de warframe en segundo plano (con SwingWorker)
 */
public static void actualizarStatsWarframeAsync() {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            actualizarStatsWarframe();
            return null;
        }
    };
    worker.execute();
}
}
