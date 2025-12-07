/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author aizpu
 */
public class PasswordUtil {

    /**
     * Encripta una contraseña usando BCrypt
     */
    public static String encriptarPassword(String passwordPlano) {
        return BCrypt.hashpw(passwordPlano, BCrypt.gensalt(12));
    }

    /**
     * Verifica si una contraseña coincide con el hash
     */
    public static boolean verificarPassword(String passwordPlano, String hashAlmacenado) {
        try {
            return BCrypt.checkpw(passwordPlano, hashAlmacenado);
        } catch (Exception e) {
            return false;
        }
    }
}
