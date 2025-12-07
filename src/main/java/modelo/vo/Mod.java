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
    private String categoria;
    private int idTipoArma;
    private String efecto;
    private String rareza;

    public Mod() {}
    
    public Mod(int id, String nombre, String tipoObjeto, String caregoria, int id_tipo_arma, String efecto, String rareza) {
        this.id = id;
        this.nombre = nombre;
        this.tipoObjeto = tipoObjeto;
        this.categoria = caregoria;
        this.idTipoArma = id_tipo_arma;
        this.efecto = efecto;
        this.rareza = rareza;
    }

    //Getters y setters
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getIdTipoArma() {
        return idTipoArma;
    }

    public void setIdTipoArma(int idTipoArma) {
        this.idTipoArma = idTipoArma;
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
