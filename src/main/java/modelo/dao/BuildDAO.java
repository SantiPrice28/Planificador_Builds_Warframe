/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.vo.Build;

/**
 *
 * @author aizpu
 */
public class BuildDAO {

    /**
     * Listar todas las builds de un usuario
     */
    public List<Build> listarBuilds(Connection conn, int idUsuario) throws SQLException {
        List<Build> builds = new ArrayList<>();

        String sql = """
            SELECT * FROM `build`
            WHERE id_usuario = ?
            ORDER BY id DESC
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Build build = new Build(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        (Integer) rs.getObject("id_warframe"),
                        (Integer) rs.getObject("id_arma"),
                        rs.getString("descripcion"),
                        (Integer) rs.getObject("mod_1_id"),
                        (Integer) rs.getObject("mod_2_id"),
                        (Integer) rs.getObject("mod_3_id"),
                        (Integer) rs.getObject("mod_4_id"),
                        (Integer) rs.getObject("mod_5_id"),
                        (Integer) rs.getObject("mod_6_id"),
                        (Integer) rs.getObject("mod_7_id"),
                        (Integer) rs.getObject("mod_8_id")
                );
                builds.add(build);
            }
        }

        return builds;
    }

    /**
     * Listar builds por tipo CON nombre del arma/warframe
     */
    public List<Build> listarBuildsPorTipo(Connection conn, int idUsuario, String tipo) throws SQLException {
        List<Build> builds = new ArrayList<>();

        String sql;
        if ("Arma".equals(tipo)) {
            sql = """
            SELECT b.*, a.nombre as nombre_arma
            FROM `build` b
            LEFT JOIN arma a ON b.id_arma = a.id
            WHERE b.id_usuario = ? AND b.tipo = ?
            ORDER BY b.id DESC
        """;
        } else {
            sql = """
            SELECT b.*, w.nombre as nombre_warframe
            FROM `build` b
            LEFT JOIN warframe w ON b.id_warframe = w.id
            WHERE b.id_usuario = ? AND b.tipo = ?
            ORDER BY b.id DESC
        """;
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, tipo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Build build = new Build(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        (Integer) rs.getObject("id_warframe"),
                        (Integer) rs.getObject("id_arma"),
                        rs.getString("descripcion"),
                        (Integer) rs.getObject("mod_1_id"),
                        (Integer) rs.getObject("mod_2_id"),
                        (Integer) rs.getObject("mod_3_id"),
                        (Integer) rs.getObject("mod_4_id"),
                        (Integer) rs.getObject("mod_5_id"),
                        (Integer) rs.getObject("mod_6_id"),
                        (Integer) rs.getObject("mod_7_id"),
                        (Integer) rs.getObject("mod_8_id")
                );

                // ⭐ Añadir el nombre del arma/warframe al nombre de la build
                if ("Arma".equals(tipo)) {
                    String nombreArma = rs.getString("nombre_arma");
                    if (nombreArma != null) {
                        build.setNombre(nombreArma + " - " + build.getNombre());
                    }
                } else {
                    String nombreWarframe = rs.getString("nombre_warframe");
                    if (nombreWarframe != null) {
                        build.setNombre(nombreWarframe + " - " + build.getNombre());
                    }
                }

                builds.add(build);
            }
        }

        return builds;
    }

    /**
     * Obtener una build por ID
     */
    public Build obtenerBuild(Connection conn, int idBuild) throws SQLException {
        String sql = "SELECT * FROM `build` WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBuild);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Build(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        (Integer) rs.getObject("id_warframe"),
                        (Integer) rs.getObject("id_arma"),
                        rs.getString("descripcion"),
                        (Integer) rs.getObject("mod_1_id"),
                        (Integer) rs.getObject("mod_2_id"),
                        (Integer) rs.getObject("mod_3_id"),
                        (Integer) rs.getObject("mod_4_id"),
                        (Integer) rs.getObject("mod_5_id"),
                        (Integer) rs.getObject("mod_6_id"),
                        (Integer) rs.getObject("mod_7_id"),
                        (Integer) rs.getObject("mod_8_id")
                );
            }
        }

        return null;
    }

    /**
     * Guardar una nueva build
     */
    public int guardarBuild(Connection conn, Build build) throws SQLException {
        String sql = """
        INSERT INTO `build` (id_usuario, nombre, tipo, id_warframe, id_arma, descripcion,
                           mod_1_id, mod_2_id, mod_3_id, mod_4_id, mod_5_id, mod_6_id, mod_7_id, mod_8_id)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, build.getIdUsuario());
            ps.setString(2, build.getNombre());
            ps.setString(3, build.getTipo());
            ps.setObject(4, build.getIdWarframe());
            ps.setObject(5, build.getIdArma());
            ps.setString(6, build.getDescripcion());
            ps.setObject(7, build.getMod1Id());
            ps.setObject(8, build.getMod2Id());
            ps.setObject(9, build.getMod3Id());
            ps.setObject(10, build.getMod4Id());
            ps.setObject(11, build.getMod5Id());
            ps.setObject(12, build.getMod6Id());
            ps.setObject(13, build.getMod7Id());
            ps.setObject(14, build.getMod8Id());

            ps.executeUpdate();

            if (!conn.getAutoCommit()) {
                conn.commit();
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return id;
            }
        }

        return -1;
    }

    /**
     * Actualizar una build existente
     */
    public void actualizarBuild(Connection conn, Build build) throws SQLException {
        String sql = """
            UPDATE `build`
            SET nombre = ?, tipo = ?, id_warframe = ?, id_arma = ?, descripcion = ?,
                mod_1_id = ?, mod_2_id = ?, mod_3_id = ?, mod_4_id = ?,
                mod_5_id = ?, mod_6_id = ?, mod_7_id = ?, mod_8_id = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, build.getNombre());
            ps.setString(2, build.getTipo());
            ps.setObject(3, build.getIdWarframe());
            ps.setObject(4, build.getIdArma());
            ps.setString(5, build.getDescripcion());
            ps.setObject(6, build.getMod1Id());
            ps.setObject(7, build.getMod2Id());
            ps.setObject(8, build.getMod3Id());
            ps.setObject(9, build.getMod4Id());
            ps.setObject(10, build.getMod5Id());
            ps.setObject(11, build.getMod6Id());
            ps.setObject(12, build.getMod7Id());
            ps.setObject(13, build.getMod8Id());
            ps.setInt(14, build.getId());

            ps.executeUpdate();
        }
    }

    /**
     * Eliminar una build
     */
    public void eliminarBuild(Connection conn, int idBuild) throws SQLException {
        String sql = "DELETE FROM `build` WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBuild);
            ps.executeUpdate();
        }
    }
}
