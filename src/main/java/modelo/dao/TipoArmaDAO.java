/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import modelo.vo.TipoArma;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author aizpu
 */
public class TipoArmaDAO {

    /**
     * Obtiene el tipo de arma por su id en la tabla tipo_arma.
     * Retorna null si no existe ningún registro con ese id.
     */
    public TipoArma obtenerTipoArmaPorId(Connection conn, int idTipoArma) throws SQLException {
        String sql = "SELECT id, nombre, categoria FROM tipo_arma WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoArma);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TipoArma t = new TipoArma();
                    t.setId(rs.getInt("id"));
                    t.setNombre(rs.getString("nombre"));
                    t.setCategoria(rs.getString("categoria"));
                    return t;
                } else {
                    return null;
                }
            }
        }
    }
}