/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import modelo.vo.Mod;
import modelo.vo.ModEfecto;

/**
 *
 * @author aizpu
 */
public class ModDAO {

    public List<ModEfecto> obtenerEfectosDeMod(Connection conn, int id) {
        List<ModEfecto> lista = new ArrayList<>();

        String sql = "SELECT * FROM mod_efecto WHERE id_mod = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ModEfecto efecto = new ModEfecto(
                        rs.getInt("id"),
                        rs.getInt("id_mod"),
                        rs.getString("objetivo"),
                        rs.getString("campo"),
                        rs.getString("operacion"),
                        rs.getDouble("valor"),
                        rs.getString("descripcion"),
                        rs.getString("atributo")
                );

                lista.add(efecto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void cargarModsCombo(Connection conn, JComboBox[] combosMods) throws SQLException {
        // 1️⃣ Crear un mod vacío
        Mod modVacio = new Mod(
                0, // id 0 indica mod vacío
                "Selecciona un mod", // nombre visible
                "", // tipo_objeto
                "", // categoria
                0, // id_tipo_arma
                "", // efecto
                "" // rareza
        );

        // 2️⃣ Añadir el mod vacío a todos los combos
        for (JComboBox<Mod> combo : combosMods) {
            combo.addItem(modVacio);
        }

        String sql = "SELECT * FROM `mod`";
        Statement sent = conn.createStatement();
        ResultSet rs = sent.executeQuery(sql);

        while (rs.next()) {
            Mod mod = new Mod(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("tipo_objeto"),
                    rs.getString("categoria"),
                    rs.getInt("id_tipo_arma"),
                    rs.getString("efecto"),
                    rs.getString("rareza")
            );

            for (JComboBox<Mod> combo : combosMods) {
                combo.addItem(mod);
            }
        }
    }

}
