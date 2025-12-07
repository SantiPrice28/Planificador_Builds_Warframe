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
        // Crear un mod vacio para mostrar texto para seleccionar mods
        Mod modVacio = new Mod(
                0, // id 0 indica mod vacío
                "Selecciona un mod",
                "", // tipo_objeto
                "", // categoria
                0, // id_tipo_arma
                "", // efecto
                "" // rareza
        );

        // Añadir el mod vacio a todos los combos
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

    public List<Mod> obtenerModsParaArma(Connection conn, String categoriaArma, int idTipoArma) throws SQLException {

        List<Mod> lista = new ArrayList<>();

        String sql = """
        SELECT id, nombre, tipo_objeto, categoria, id_tipo_arma, efecto, rareza
        FROM `mod`
        WHERE tipo_objeto = 'Arma'
          AND categoria = ?
          AND (id_tipo_arma = ? OR id_tipo_arma IS NULL)
        ORDER BY nombre
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoriaArma);
            ps.setInt(2, idTipoArma);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Mod m = new Mod();
                    m.setId(rs.getInt("id"));
                    m.setNombre(rs.getString("nombre"));
                    m.setTipoObjeto(rs.getString("tipo_objeto"));
                    m.setCategoria(rs.getString("categoria"));
                    m.setIdTipoArma(rs.getInt("id_tipo_arma"));

                    m.setEfecto(rs.getString("efecto"));
                    m.setRareza(rs.getString("rareza"));

                    lista.add(m);
                }
            }
        }

        return lista;
    }

    /**
     * Obtiene todos los mods de tipo Warframe
     */
    public List<Mod> obtenerModsParaWarframe(Connection conn) throws SQLException {
        List<Mod> lista = new ArrayList<>();

        String sql = """
        SELECT id, nombre, tipo_objeto, categoria, id_tipo_arma, efecto, rareza
        FROM `mod`
        WHERE tipo_objeto = 'Warframe'
        ORDER BY nombre
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mod m = new Mod();
                    m.setId(rs.getInt("id"));
                    m.setNombre(rs.getString("nombre"));
                    m.setTipoObjeto(rs.getString("tipo_objeto"));
                    m.setCategoria(rs.getString("categoria"));

                    // Para warframes, id_tipo_arma es NULL, asi hacemos que guarde un 0, en vez de null
                    int idTipoArma = rs.getInt("id_tipo_arma");
                    m.setIdTipoArma(rs.wasNull() ? 0 : idTipoArma);

                    m.setEfecto(rs.getString("efecto"));
                    m.setRareza(rs.getString("rareza"));

                    lista.add(m);
                }
            }
        }

        return lista;
    }

    /**
     * Verifica si dos mods son incompatibles entre sí
     */
    public boolean sonModsIncompatibles(Connection conn, int idMod1, int idMod2) throws SQLException {
        String sql = """
        SELECT COUNT(*) as total FROM mod_incompatible 
        WHERE (id_mod_1 = ? AND id_mod_2 = ?) 
           OR (id_mod_1 = ? AND id_mod_2 = ?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMod1);
            ps.setInt(2, idMod2);
            ps.setInt(3, idMod2);
            ps.setInt(4, idMod1);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        }

        return false;
    }

    /**
     * Obtiene lista de IDs de mods incompatibles con el dado
     */
    public List<Integer> obtenerModsIncompatibles(Connection conn, int idMod) throws SQLException {
        List<Integer> incompatibles = new ArrayList<>();

        String sql = """
        SELECT id_mod_2 as incompatible FROM mod_incompatible WHERE id_mod_1 = ?
        UNION
        SELECT id_mod_1 as incompatible FROM mod_incompatible WHERE id_mod_2 = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMod);
            ps.setInt(2, idMod);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                incompatibles.add(rs.getInt("incompatible"));
            }
        }

        return incompatibles;
    }
}
