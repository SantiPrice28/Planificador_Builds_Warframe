/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import modelo.vo.Warframe;
import vista.Ventana;

/**
 *
 * @author aizpu
 */
public class WarframeDAO {

    public void cargarWarframesCombo(Connection conn, JComboBox<Warframe> combo) throws SQLException {
        String sql = "SELECT * FROM warframe";
        Statement sent = conn.createStatement();
        ResultSet rs = sent.executeQuery(sql);

        while (rs.next()) {
            Warframe warframe = new Warframe(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("salud_base"),
                    rs.getInt("escudo_base"),
                    rs.getInt("armadura_base"),
                    rs.getInt("energia_base"),
                    rs.getString("descripcion"),
                    rs.getDouble("duracion"),
                    rs.getDouble("eficiencia"),
                    rs.getDouble("fuerza"),
                    rs.getDouble("rango")
            );
            combo.addItem(warframe);
        }
    }

    public void cargarStatsBaseWarframe(Connection conn, JComboBox<Warframe> cmb_warframes,
            JLabel[] labelsArray, Ventana vista_principal) {

        Warframe warframe = (Warframe) cmb_warframes.getSelectedItem();
        if (warframe == null) {
            return;
        }

        //Guardar stats base en el mapa
        vista_principal.setValorBaseWarframe("salud", warframe.getSalud());
        vista_principal.setValorBaseWarframe("escudo", warframe.getEscudo());
        vista_principal.setValorBaseWarframe("armadura", warframe.getArmadura());
        vista_principal.setValorBaseWarframe("energia", warframe.getEnergia());
        vista_principal.setValorBaseWarframe("duracion", warframe.getDuracion());
        vista_principal.setValorBaseWarframe("eficiencia", warframe.getEficiencia());
        vista_principal.setValorBaseWarframe("fuerza", warframe.getFuerza());
        vista_principal.setValorBaseWarframe("rango", warframe.getRango());

        //️Ocultar todos los labels
        for (JLabel lbl : labelsArray) {
            lbl.setVisible(false);
        }

        List<String> textos = new ArrayList<>();

        // === Stats Base ===
        textos.add("Salud: " + warframe.getSalud());
        textos.add("Escudo: " + warframe.getEscudo());
        textos.add("Armadura: " + warframe.getArmadura());
        textos.add("Energía: " + warframe.getEnergia());

        // === Stats de Habilidades ===
        textos.add("Duración: " + String.format("%.2f%%", warframe.getDuracion() * 100));
        textos.add("Eficiencia: " + String.format("%.2f%%", warframe.getEficiencia() * 100));
        textos.add("Fuerza: " + String.format("%.2f%%", warframe.getFuerza() * 100));
        textos.add("Rango: " + String.format("%.2f%%", warframe.getRango() * 100));

        // Mostrar en los labels
        for (int i = 0; i < textos.size() && i < labelsArray.length; i++) {
            labelsArray[i].setText(textos.get(i));
            labelsArray[i].setVisible(true);
        }

        vista_principal.panel_warframes.revalidate();
        vista_principal.panel_warframes.repaint();
    }

}
