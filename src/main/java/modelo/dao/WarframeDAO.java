/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import modelo.vo.Warframe;

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
                    rs.getString("descripcion")
            );
            combo.addItem(warframe);
        }
    }
    
}
