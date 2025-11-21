/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

/**
 *
 * @author aizpu
 */
public class Mod {
    private int id;
    private String nombre;
    private String tipoObjeto;
    private String caregoria;
    private int id_tipo_arma;
    private String efecto;
    private String rareza;

    public Mod(int id, String nombre, String tipoObjeto, String caregoria, int id_tipo_arma, String efecto, String rareza) {
        this.id = id;
        this.nombre = nombre;
        this.tipoObjeto = tipoObjeto;
        this.caregoria = caregoria;
        this.id_tipo_arma = id_tipo_arma;
        this.efecto = efecto;
        this.rareza = rareza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public String getCaregoria() {
        return caregoria;
    }

    public void setCaregoria(String caregoria) {
        this.caregoria = caregoria;
    }

    public int getId_tipo_arma() {
        return id_tipo_arma;
    }

    public void setId_tipo_arma(int id_tipo_arma) {
        this.id_tipo_arma = id_tipo_arma;
    }

    public String getEfecto() {
        return efecto;
    }

    public void setEfecto(String efecto) {
        this.efecto = efecto;
    }

    public String getRareza() {
        return rareza;
    }

    public void setRareza(String rareza) {
        this.rareza = rareza;
    }

    @Override
    public String toString() {
        return nombre + ' ' + efecto;
    }
    
    
}
