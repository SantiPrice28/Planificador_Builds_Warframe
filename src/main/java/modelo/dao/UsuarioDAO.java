/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.vo.Usuario;

/**
 *
 * @author aizpu
 */
public class UsuarioDAO {
    
    public Usuario obtenerPorNombreUsuario(Connection conn, String nombreUsuario) throws Exception {
        String sql = "SELECT * FROM usuario WHERE nombre_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre_usuario"),
                    rs.getString("email"),
                    rs.getString("contraseña")
                );
            }
        }
        return null;
    }
}
