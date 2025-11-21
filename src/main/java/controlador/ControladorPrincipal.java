/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.factory.DAOFactory;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import modelo.dao.*;
import modelo.vo.*;
import vista.*;

/**
 *
 * @author aizpu
 */
public class ControladorPrincipal {

    public static DAOFactory mySQLFactory;
    //declara los objetos DAO
    public static ArmaDAO armDAO;
    public static WarframeDAO warDAO;
    public static ModDAO modDAO;

    public static Login vista_login = new Login();
    public static Ventana vista_principal = new Ventana();
//    
//    public static Ejercicio3 ventana = new Ejercicio3();

    public static void iniciar() {
        vista_login.setVisible(true);
        vista_login.setLocationRelativeTo(null);
    }

    public static void iniciaFactory() {
        mySQLFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        //inicializa los objetos DAO
        armDAO = mySQLFactory.getArmaDAO();
        warDAO = mySQLFactory.getWarframeDAO();
        modDAO = mySQLFactory.getModDAO();
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

            if (user != null && user.getContraseña().equals(contrasena)) {
                // Si quieres seguridad, aquí podrías comparar hashes
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

    public static void listarArmas() {
        try {
            Connection conn;
            conn = mySQLFactory.getConnection();

//            armDAO.listar(conn, vista_principal.getTxt_area());
            mySQLFactory.releaseConnection(conn);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Entrada de datos incorrecta");
        } catch (Exception ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
            Connection conn;
            conn = mySQLFactory.getConnection();

            modDAO.cargarModsCombo(conn, vista_principal.getCombosMods());

            mySQLFactory.releaseConnection(conn);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Entrada de datos incorrecta");
        } catch (Exception ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
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
                return;
            }

            JLabel[] labels = vista_principal.getLabelsArray();
            JComboBox[] combosMods = vista_principal.getCombosMods();
            Map<String, Double> acumulados = new HashMap<>();

            // 1️⃣ Recorrer mods y acumular efectos
            for (JComboBox combo : combosMods) {
                Mod mod = (Mod) combo.getSelectedItem();
                if (mod == null || mod.getId() == 0) {
                    continue;
                }

                List<ModEfecto> efectos = modDAO.obtenerEfectosDeMod(conn, mod.getId());

                for (ModEfecto e : efectos) {
                    String atributoMod = e.getAtributo().toLowerCase();
                    double cantidad = e.getValor();

                    if ("daño".equals(atributoMod)) {
                        // aplica a todos los daños (físicos y elementales)
                        String[] todosDaños = {"impacto", "perforante", "cortante", "frio", "electrico", "calor", "toxina"};
                        for (String stat : todosDaños) {
                            acumulados.put(stat, acumulados.getOrDefault(stat, 0.0) + cantidad);
                        }
                    } else {
                        // para todos los demás atributos
                        String stat = mapearAtributo(atributoMod);
                        acumulados.put(stat, acumulados.getOrDefault(stat, 0.0) + cantidad);
                    }
                }

            }

            // 2️⃣ Aplicar los efectos y actualizar labels con texto
            String[] ordenStats = vista_principal.getOrdenStats();
            for (int i = 0; i < ordenStats.length; i++) {
                String stat = ordenStats[i];
                JLabel label = labels[i];

                double base = vista_principal.getValorBase(stat);
                double pct = acumulados.getOrDefault(stat, 0.0);
                double resultado = base * (1 + pct);

                label.setText(getTextoStat(stat, resultado)); // <-- texto bonito
                label.setVisible(true);
            }

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
            case "daño_frio":
                return "frio";
            case "daño_electrico":
                return "electrico";
            case "daño_calor":
                return "calor";
            case "daño_toxina":
                return "toxina";
            case "critico":
                return "critico";
            case "mult_critico":
                return "mult_critico";
            case "precision":
                return "precision";
            case "cadencia":
                return "cadencia";
            // añade más si es necesario
            default:
                return atributoMod; // por si coincide con ordenStats
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

}
