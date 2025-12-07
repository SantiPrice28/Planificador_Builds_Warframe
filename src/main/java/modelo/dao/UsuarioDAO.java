/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import modelo.vo.Usuario;

/**
 *
 * @author aizpu
 */
public class UsuarioDAO {

    /**
     * Obtener usuario por nombre de usuario
     */
    public Usuario obtenerPorNombreUsuario(Connection conn, String nombreUsuario) throws Exception {
        String sql = "SELECT * FROM usuario WHERE nombre_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña")
                );
            }
        }
        return null;
    }

    /**
     * Crear un nuevo usuario
     */
    public int crearUsuario(Connection conn, Usuario usuario) throws Exception {
        String sql = "INSERT INTO usuario (nombre_usuario, contraseña) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContraseña());

            int affectedRows = ps.executeUpdate();

            // Commit si no está en autocommit
            if (!conn.getAutoCommit()) {
                conn.commit();
            }

            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

    /**
     * Verificar si un nombre de usuario ya existe
     */
    public boolean existeNombreUsuario(Connection conn, String nombreUsuario) throws Exception {
        String sql = "SELECT COUNT(*) as total FROM usuario WHERE nombre_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        }
        return false;
    }
}
