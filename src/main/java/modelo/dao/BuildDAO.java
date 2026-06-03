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

    //Metodo con JavaFX
    public List<Build> buscarBuilds(Connection conn, int idUsuarioLogeado, boolean verPublicas, String categoria, int idFiltro) throws Exception {
        if (!conn.getAutoCommit()) {
            conn.commit();
        }

        List<Build> lista = new ArrayList<>();

        // Hacemos JOIN con las tablas warframe y arma para traernos sus nombres
        String sql = "SELECT b.*, w.nombre AS nombre_warframe, a.nombre AS nombre_arma "
                + "FROM build b "
                + "LEFT JOIN warframe w ON b.id_warframe = w.id "
                + "LEFT JOIN arma a ON b.id_arma = a.id "
                + "WHERE ";

        if (verPublicas) {
            sql += "b.es_publica = 1 ";
        } else {
            sql += "b.id_usuario = ? ";
        }

        // filtro del ComboBox
        if (categoria.equals("SOLO_WARFRAMES")) {
            sql += "AND b.id_warframe IS NOT NULL ";
        } else if (categoria.equals("SOLO_ARMAS")) {
            sql += "AND b.id_arma IS NOT NULL ";
        } else if (categoria.equals("WARFRAME_ESPECIFICO")) {
            sql += "AND b.id_warframe = " + idFiltro + " ";
        } else if (categoria.equals("ARMA_ESPECIFICA")) {
            sql += "AND b.id_arma = " + idFiltro + " ";
        }

        try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!verPublicas) {
                pstmt.setInt(1, idUsuarioLogeado);
            }

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Build b = new Build();

                    b.setId(rs.getInt("id"));
                    b.setIdUsuario(rs.getInt("id_usuario"));
                    b.setNombre(rs.getString("nombre"));
                    b.setDescripcion(rs.getString("descripcion"));
                    b.setTipo(rs.getString("tipo"));

                    b.setIdWarframe(rs.getObject("id_warframe") != null ? rs.getInt("id_warframe") : null);
                    b.setIdArma(rs.getObject("id_arma") != null ? rs.getInt("id_arma") : null);

                    b.setMod1Id(rs.getObject("mod_1_id") != null ? rs.getInt("mod_1_id") : null);
                    b.setMod2Id(rs.getObject("mod_2_id") != null ? rs.getInt("mod_2_id") : null);
                    b.setMod3Id(rs.getObject("mod_3_id") != null ? rs.getInt("mod_3_id") : null);
                    b.setMod4Id(rs.getObject("mod_4_id") != null ? rs.getInt("mod_4_id") : null);
                    b.setMod5Id(rs.getObject("mod_5_id") != null ? rs.getInt("mod_5_id") : null);
                    b.setMod6Id(rs.getObject("mod_6_id") != null ? rs.getInt("mod_6_id") : null);
                    b.setMod7Id(rs.getObject("mod_7_id") != null ? rs.getInt("mod_7_id") : null);
                    b.setMod8Id(rs.getObject("mod_8_id") != null ? rs.getInt("mod_8_id") : null);

                    
                    if (b.getIdWarframe() != null) {
                        b.setNombreEquipamiento(rs.getString("nombre_warframe"));
                    } else if (b.getIdArma() != null) {
                        b.setNombreEquipamiento(rs.getString("nombre_arma"));
                    }

                    lista.add(b);
                }
            }
        }
        return lista;
    }

    // PARA CREAR UNA BUILD NUEVA
    public int guardarBuild(Connection conn, Build build) throws Exception {
        String sql = "INSERT INTO build (id_usuario, nombre, tipo, id_warframe, id_arma, descripcion, es_publica, "
                + "mod_1_id, mod_2_id, mod_3_id, mod_4_id, mod_5_id, mod_6_id, mod_7_id, mod_8_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, build.getIdUsuario());
            pstmt.setString(2, build.getNombre());
            pstmt.setString(3, build.getTipo());

            if (build.getIdWarframe() != null) {
                pstmt.setInt(4, build.getIdWarframe());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }

            if (build.getIdArma() != null) {
                pstmt.setInt(5, build.getIdArma());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            pstmt.setString(6, build.getDescripcion());
            pstmt.setInt(7, build.isEsPublica() ? 1 : 0);

            Integer[] mods = {build.getMod1Id(), build.getMod2Id(), build.getMod3Id(), build.getMod4Id(),
                build.getMod5Id(), build.getMod6Id(), build.getMod7Id(), build.getMod8Id()};
            for (int i = 0; i < mods.length; i++) {
                if (mods[i] != null) {
                    pstmt.setInt(8 + i, mods[i]);
                } else {
                    pstmt.setNull(8 + i, java.sql.Types.INTEGER);
                }
            }

            System.out.println("Aqui esta dentro del metodo guardar");
            int filasAfectadas = pstmt.executeUpdate();

            if (!conn.getAutoCommit()) {
                conn.commit();
                System.out.println("Commit realizado con éxito");
            }

            if (filasAfectadas > 0) {
                try (java.sql.ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error de BD: " + ex.getMessage());
        }
        System.out.println("Si sale esto no se ha guardado");
        return 0;
    }

    // PARA MODIFICAR UNA BUILD EXISTENTE
    public boolean modificarBuild(Connection conn, Build build) throws Exception {
        String sql = "UPDATE build SET nombre = ?, descripcion = ?, es_publica = ?, "
                + "mod_1_id = ?, mod_2_id = ?, mod_3_id = ?, mod_4_id = ?, "
                + "mod_5_id = ?, mod_6_id = ?, mod_7_id = ?, mod_8_id = ? "
                + "WHERE id = ?";

        try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, build.getNombre());
            pstmt.setString(2, build.getDescripcion());
            pstmt.setInt(3, build.isEsPublica() ? 1 : 0);

            Integer[] mods = {build.getMod1Id(), build.getMod2Id(), build.getMod3Id(), build.getMod4Id(),
                build.getMod5Id(), build.getMod6Id(), build.getMod7Id(), build.getMod8Id()};

            for (int i = 0; i < mods.length; i++) {
                if (mods[i] != null) {
                    pstmt.setInt(4 + i, mods[i]);
                } else {
                    pstmt.setNull(4 + i, java.sql.Types.INTEGER);
                }
            }
            pstmt.setInt(12, build.getId());

            int filasAfectadas = pstmt.executeUpdate();

            if (!conn.getAutoCommit()) {
                conn.commit();
                System.out.println("Build actualizada y commit realizado.");
            }

            return filasAfectadas > 0;

        } catch (Exception ex) {
            System.out.println("Error al actualizar build: " + ex.getMessage());
            throw ex; 
        }
    }

    public boolean eliminarBuild(Connection conn, int idBuild) {
        String sql = "DELETE FROM `build` WHERE id = ?";

        try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBuild);
            int filasAfectadas = ps.executeUpdate();
            
            if (!conn.getAutoCommit()) {
                conn.commit();
            }
            
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
