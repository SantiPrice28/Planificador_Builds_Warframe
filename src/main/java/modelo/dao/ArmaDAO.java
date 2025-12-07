/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import modelo.vo.Arma;
import modelo.vo.TipoArma;
import vista.Ventana;

/**
 *
 * @author aizpu
 */
public class ArmaDAO {

    public void cargarArmasCombo(Connection conn, JComboBox<Arma> combo) throws SQLException {
        String sql = "SELECT * FROM arma";
        Statement sent = conn.createStatement();
        ResultSet rs = sent.executeQuery(sql);

        while (rs.next()) {
            Arma arma = new Arma(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("id_tipo_arma"),
                    rs.getDouble("dano_impacto"),
                    rs.getDouble("dano_perforante"),
                    rs.getDouble("dano_cortante"),
                    rs.getDouble("dano_frio"),
                    rs.getDouble("dano_electrico"),
                    rs.getDouble("dano_calor"),
                    rs.getDouble("dano_toxina"),
                    rs.getDouble("critico"),
                    rs.getDouble("mult_critico"),
                    rs.getDouble("estado"),
                    rs.getDouble("precision"),
                    rs.getDouble("cadencia"),
                    rs.getString("descripcion")
            );
            combo.addItem(arma);
        }
    }

    public void cargarStatsBaseArmas(Connection conn, JComboBox<Arma> cmb_armas,
            JLabel[] labelsArray, Ventana vista_principal) {

        Arma arma = (Arma) cmb_armas.getSelectedItem();
        if (arma == null) {
            return;
        }

        // Guardar stats base en el mapa
        vista_principal.setValorBase("impacto", arma.getDañoImpacto());
        vista_principal.setValorBase("perforante", arma.getDañoPerforante());
        vista_principal.setValorBase("cortante", arma.getDañoCortante());

        vista_principal.setValorBase("frio", arma.getDañoFrio());
        vista_principal.setValorBase("electrico", arma.getDañoElectrico());
        vista_principal.setValorBase("calor", arma.getDañoCalor());
        vista_principal.setValorBase("toxina", arma.getDañoToxina());

        vista_principal.setValorBase("critico", arma.getCritico());
        vista_principal.setValorBase("mult_critico", arma.getMultCritico());
        vista_principal.setValorBase("estado", arma.getEstado());
        vista_principal.setValorBase("precision", arma.getPrecision());
        vista_principal.setValorBase("cadencia", arma.getCadencia());

        // Ocultar todos los labels
        for (JLabel lbl : labelsArray) {
            lbl.setVisible(false);
        }

        //Vamos filtrando los que sean mayor a 0
        List<String> textos = new ArrayList<>();

        // === Daños físicos ===
        if (arma.getDañoImpacto() > 0) {
            textos.add("Impacto: " + arma.getDañoImpacto());
        }

        if (arma.getDañoPerforante() > 0) {
            textos.add("Perforante: " + arma.getDañoPerforante());
        }

        if (arma.getDañoCortante() > 0) {
            textos.add("Cortante: " + arma.getDañoCortante());
        }

        // === Daños elementales ===
        if (arma.getDañoFrio() > 0) {
            textos.add("Frío: " + arma.getDañoFrio());
        }

        if (arma.getDañoElectrico() > 0) {
            textos.add("Eléctrico: " + arma.getDañoElectrico());
        }

        if (arma.getDañoCalor() > 0) {
            textos.add("Calor: " + arma.getDañoCalor());
        }

        if (arma.getDañoToxina() > 0) {
            textos.add("Toxina: " + arma.getDañoToxina());
        }

        // === Stats generales ===
        if (arma.getCritico() > 0) {
            textos.add("Crítico: " + arma.getCritico());
        }

        if (arma.getMultCritico() > 0) {
            textos.add("Mult. Crítico: " + arma.getMultCritico());
        }

        if (arma.getEstado() > 0) {
            textos.add("Estado: " + arma.getEstado());
        }

        if (arma.getPrecision() > 0) {
            textos.add("Precisión: " + arma.getPrecision());
        }

        if (arma.getCadencia() > 0) {
            textos.add("Cadencia: " + arma.getCadencia());
        }

        // Mostrar solo los necesarios
        for (int i = 0; i < textos.size() && i < labelsArray.length; i++) {
            labelsArray[i].setText(textos.get(i));
            labelsArray[i].setVisible(true);
        }

        vista_principal.panel_armas.revalidate();
        vista_principal.panel_armas.repaint();
    }
}
