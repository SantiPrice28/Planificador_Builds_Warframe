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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
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
    public static UsuarioDAO usuDAO;

    public static Usuario usuarioActual = null;

    public static void iniciaFactory() {
        try {
            mySQLFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
            armDAO = new ArmaDAO();
            buildDAO = new BuildDAO();
            modDAO = new ModDAO();
            tipoArmaDAO = new TipoArmaDAO();
            warDAO = new WarframeDAO();
            usuDAO = new UsuarioDAO();
        } catch (Exception ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void loginJavaFX(String usuario, String contrasena, LoginController vistaController) {
        Connection conn = null;
        try {
            conn = mySQLFactory.getConnection();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario user = usuarioDAO.obtenerPorNombreUsuario(conn, usuario);

            if (user != null && PasswordUtil.verificarPassword(contrasena, user.getContraseña())) {
                usuarioActual = user;
                System.out.println("¡Login exitoso! Abriendo programa...");
                vistaController.abrirVentanaPrincipal();
            } else {
                vistaController.mostrarError("El usuario o la contraseña no son correctos.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            vistaController.mostrarError("Error al conectar con la base de datos.");
        } finally {
            if (conn != null) {
                mySQLFactory.releaseConnection(conn);
            }
        }
    }

    public static void registrarUsuarioFX(String nombreUsuario, String contrasena, vista.RegistroController vistaController) {
        java.sql.Connection conn = null;
        try {
            conn = mySQLFactory.getConnection();
            modelo.dao.UsuarioDAO usuarioDAO = new modelo.dao.UsuarioDAO();

            // Validaciones iniciales
            if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
                vistaController.mostrarError("El nombre de usuario no puede estar vacío");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            if (contrasena == null || contrasena.trim().isEmpty()) {
                vistaController.mostrarError("La contraseña no puede estar vacía");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            if (contrasena.length() < 6) {
                vistaController.mostrarError("La contraseña debe tener al menos 6 caracteres");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            // Verificar si ya existe el usuario
            if (usuarioDAO.existeNombreUsuario(conn, nombreUsuario)) {
                vistaController.mostrarError("El nombre de usuario ya existe");
                mySQLFactory.releaseConnection(conn);
                return;
            }

            // ENCRIPTAR CONTRASEÑA
            String hashContraseña = modelo.util.PasswordUtil.encriptarPassword(contrasena);

            // Crear usuario
            modelo.vo.Usuario nuevoUsuario = new modelo.vo.Usuario();
            nuevoUsuario.setNombreUsuario(nombreUsuario);
            nuevoUsuario.setContraseña(hashContraseña);

            // Llamada que devuelve el ID generado
            int idGenerado = usuarioDAO.crearUsuario(conn, nuevoUsuario);

            // Commit
            if (!conn.getAutoCommit()) {
                conn.commit();
            }

            if (idGenerado > 0) {
                vistaController.mostrarInfo("Usuario creado correctamente. Ya puedes iniciar sesión.");
                vistaController.cerrarVentana();
            } else {
                vistaController.mostrarError("Error al crear el usuario.");
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
            vistaController.mostrarError("Error al registrar usuario: " + ex.getMessage());
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

    public static void cerrarFactory() {
        try {
            mySQLFactory.shutdown();
        } catch (Exception ex) {
            Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void calcularStatsWarframe(Warframe base, Mod[] mods,
            Label lblSalud, Label lblEscudo,
            Label lblArmadura, Label lblEnergia) {
        if (base == null) {
            return;
        }

        double saludFinal = base.getSalud();
        double escudoFinal = base.getEscudo();
        double armaduraFinal = base.getArmadura();
        double energiaFinal = base.getEnergia();

        // Recorremos los mods equipados para aplicar multiplicadores
        for (Mod m : mods) {
            if (m != null) {
                if (m.getNombre().equals("Vitality")) {
                    saludFinal += base.getSalud() * 1.0;
                }
                if (m.getNombre().equals("Redirection")) {
                    escudoFinal += base.getEscudo() * 1.0;
                }
            }
        }

        // Actualizamos la interfaz
        lblSalud.setText(String.valueOf((int) saludFinal));
        lblEscudo.setText(String.valueOf((int) escudoFinal));
        lblArmadura.setText(String.valueOf((int) armaduraFinal));
        lblEnergia.setText(String.valueOf((int) energiaFinal));
    }

    //Combinaciones de daños elementales
    public enum Elemento {
        IMPACTO, PERFORACION, CORTANTE,
        CALOR, FRIO, ELECTRICIDAD, TOXINA,
        EXPLOSION, VIRAL, GAS, MAGNETICO, RADIACION, CORROSIVO;

        public static Elemento combinar(Elemento e1, Elemento e2) {
            if ((e1 == CALOR && e2 == FRIO) || (e1 == FRIO && e2 == CALOR)) {
                return EXPLOSION;
            }
            if ((e1 == FRIO && e2 == TOXINA) || (e1 == TOXINA && e2 == FRIO)) {
                return VIRAL;
            }
            if ((e1 == CALOR && e2 == TOXINA) || (e1 == TOXINA && e2 == CALOR)) {
                return GAS;
            }
            if ((e1 == FRIO && e2 == ELECTRICIDAD) || (e1 == ELECTRICIDAD && e2 == FRIO)) {
                return MAGNETICO;
            }
            if ((e1 == CALOR && e2 == ELECTRICIDAD) || (e1 == ELECTRICIDAD && e2 == CALOR)) {
                return RADIACION;
            }
            if ((e1 == TOXINA && e2 == ELECTRICIDAD) || (e1 == ELECTRICIDAD && e2 == TOXINA)) {
                return CORROSIVO;
            }
            return null;
        }
    }

    //Metodo para calcular los daños combinados y TODAS las stats del arma
    public static Map<String, Double> calcularDanoFinal(Arma base, Mod[] mods) {
        Map<String, Double> danosFinales = new LinkedHashMap<>();

        // Multiplicadores base
        double multDanoBase = 1.0;
        double multMultishot = 0.0;
        double multCritico = 0.0;
        double multDanoCritico = 0.0;
        double multEstado = 0.0;
        double multCadencia = 0.0;
        double multPrecision = 0.0;

        List<Elemento> elementosEnOrden = new ArrayList<>();
        Map<Elemento, Double> porcentajesElementales = new HashMap<>();

        for (Mod m : mods) {
            if (m == null) {
                continue;
            }
            // Separamos por "/" por si el mod tiene múltiples efectos (Ej: +Daño / +Multidisparo)
            String[] partes = m.getEfecto().toLowerCase().split("/");

            for (String parte : partes) {
                double valor = extraerPorcentaje(parte);

                // Si la frase tiene un "-" reducimos el stat (Para mods corruptos)
                if (parte.contains("-")) {
                    valor = -Math.abs(valor);
                }

                // Clasificamos qué está mejorando esta parte del mod
                if (parte.contains("multidisparo") || parte.contains("multishot")) {
                    multMultishot += valor;
                } else if (parte.contains("probabilidad crítica") || parte.contains("probabilidad critica")) {
                    multCritico += valor;
                } else if (parte.contains("daño crítico") || parte.contains("dano critico") || parte.contains("multiplicador")) {
                    multDanoCritico += valor;
                } else if (parte.contains("estado")) {
                    multEstado += valor;
                } else if (parte.contains("cadencia")) {
                    multCadencia += valor;
                } else if (parte.contains("precisión") || parte.contains("precision")) {
                    multPrecision += valor;
                } else if (parte.contains("daño") && !parte.contains("impacto") && !parte.contains("perfor") && !parte.contains("cortan")) {
                    // Evitamos atrapar "daño crítico" en el daño base
                    if (!parte.contains("crítico") && !parte.contains("critico")) {
                        multDanoBase += valor;
                    }
                }

                // Elementos
                Elemento e = null;
                if (parte.contains("calor")) {
                    e = Elemento.CALOR;
                } else if (parte.contains("frío") || parte.contains("frio")) {
                    e = Elemento.FRIO;
                } else if (parte.contains("toxina")) {
                    e = Elemento.TOXINA;
                } else if (parte.contains("eléctrico") || parte.contains("electrico")) {
                    e = Elemento.ELECTRICIDAD;
                }

                if (e != null) {
                    if (!elementosEnOrden.contains(e)) {
                        elementosEnOrden.add(e);
                    }
                    double porcentajeActual = porcentajesElementales.getOrDefault(e, 0.0);
                    porcentajesElementales.put(e, porcentajeActual + valor);
                }
            }
        }

        // El multidisparo final (Mínimo 0 para no romper matematicas)
        double factorMultishot = 1.0 + multMultishot;
        if (factorMultishot < 0) {
            factorMultishot = 0;
        }

        // Calcular Daños Físicos (Base * Mod de Daño * Multishot)
        double imp = base.getDañoImpacto() * multDanoBase * factorMultishot;
        double perf = base.getDañoPerforante() * multDanoBase * factorMultishot;
        double cort = base.getDañoCortante() * multDanoBase * factorMultishot;

        double totalBase = base.getDañoImpacto() + base.getDañoPerforante() + base.getDañoCortante();

        if (imp > 0) {
            danosFinales.put("Impacto", imp);
        }
        if (perf > 0) {
            danosFinales.put("Perforación", perf);
        }
        if (cort > 0) {
            danosFinales.put("Cortante", cort);
        }

        // Combinación Elemental
        int i = 0;
        while (i < elementosEnOrden.size()) {
            Elemento actual = elementosEnOrden.get(i);

            if (i + 1 < elementosEnOrden.size()) {
                Elemento siguiente = elementosEnOrden.get(i + 1);
                Elemento combinado = Elemento.combinar(actual, siguiente);

                if (combinado != null) {
                    double sumaPorcentajes = porcentajesElementales.get(actual) + porcentajesElementales.get(siguiente);
                    double danoCombo = (totalBase * multDanoBase) * sumaPorcentajes * factorMultishot;

                    danosFinales.put(combinado.name(), danoCombo);
                    i += 2;
                    continue;
                }
            }

            double danoSimple = (totalBase * multDanoBase) * porcentajesElementales.get(actual) * factorMultishot;
            danosFinales.put(actual.name(), danoSimple);
            i++;
        }

        // APLICAR STATS FIJAS (Multiplicadas por sus respectivos mods)
        double critFinal = base.getCritico() * (1.0 + multCritico);
        double multCritFinal = base.getMultCritico() * (1.0 + multDanoCritico);
        double estadoFinal = base.getEstado() * (1.0 + multEstado);
        double cadenciaFinal = base.getCadencia() * (1.0 + multCadencia);
        double precisionFinal = base.getPrecision() * (1.0 + multPrecision);

        // Seguros para que no bajen de 0
        if (critFinal < 0) {
            critFinal = 0;
        }
        if (multCritFinal < 0) {
            multCritFinal = 0;
        }
        if (estadoFinal < 0) {
            estadoFinal = 0;
        }
        if (cadenciaFinal < 0) {
            cadenciaFinal = 0;
        }
        if (precisionFinal < 0) {
            precisionFinal = 0;
        }

        danosFinales.put("Crítico", critFinal * 100);
        danosFinales.put("Multiplicador", multCritFinal);
        danosFinales.put("Estado", estadoFinal * 100);
        danosFinales.put("Cadencia", cadenciaFinal);

        // Solo mostramos precisión si el arma tiene más de 0
        if (base.getPrecision() > 0) {
            danosFinales.put("Precisión", precisionFinal);
        }

        return danosFinales;
    }

    // Método auxiliar para sacar el número de "+90% Calor" -> 0.9
    public static double extraerPorcentaje(String texto) {
        if (texto == null || texto.isEmpty()) {
            return 0.0;
        }
        try {
            // Buscamos el primer número que aparezca en el texto
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("(\\d+(\\.\\d+)?)");
            java.util.regex.Matcher m = p.matcher(texto);
            if (m.find()) {
                return Double.parseDouble(m.group(1)) / 100.0;
            }
        } catch (Exception e) {
            return 0.0;
        }
        return 0.0;
    }
}
