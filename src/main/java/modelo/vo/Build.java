/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

import java.sql.Timestamp;

/**
 *
 * @author aizpu
 */
public class Build {

    private int id;
    private int idUsuario;
    private String nombre;
    private String tipo;
    private Integer idWarframe;
    private Integer idArma;
    private String descripcion;
    private Integer mod1Id;
    private Integer mod2Id;
    private Integer mod3Id;
    private Integer mod4Id;
    private Integer mod5Id;
    private Integer mod6Id;
    private Integer mod7Id;
    private Integer mod8Id;

    // Constructor vacío
    public Build() {
    }

    public Build(int id, int idUsuario, String nombre, String tipo, Integer idWarframe,
            Integer idArma, String descripcion, Integer mod1Id, Integer mod2Id,
            Integer mod3Id, Integer mod4Id, Integer mod5Id, Integer mod6Id,
            Integer mod7Id, Integer mod8Id) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.tipo = tipo;
        this.idWarframe = idWarframe;
        this.idArma = idArma;
        this.descripcion = descripcion;
        this.mod1Id = mod1Id;
        this.mod2Id = mod2Id;
        this.mod3Id = mod3Id;
        this.mod4Id = mod4Id;
        this.mod5Id = mod5Id;
        this.mod6Id = mod6Id;
        this.mod7Id = mod7Id;
        this.mod8Id = mod8Id;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getIdWarframe() {
        return idWarframe;
    }

    public void setIdWarframe(Integer idWarframe) {
        this.idWarframe = idWarframe;
    }

    public Integer getIdArma() {
        return idArma;
    }

    public void setIdArma(Integer idArma) {
        this.idArma = idArma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getMod1Id() {
        return mod1Id;
    }

    public void setMod1Id(Integer mod1Id) {
        this.mod1Id = mod1Id;
    }

    public Integer getMod2Id() {
        return mod2Id;
    }

    public void setMod2Id(Integer mod2Id) {
        this.mod2Id = mod2Id;
    }

    public Integer getMod3Id() {
        return mod3Id;
    }

    public void setMod3Id(Integer mod3Id) {
        this.mod3Id = mod3Id;
    }

    public Integer getMod4Id() {
        return mod4Id;
    }

    public void setMod4Id(Integer mod4Id) {
        this.mod4Id = mod4Id;
    }

    public Integer getMod5Id() {
        return mod5Id;
    }

    public void setMod5Id(Integer mod5Id) {
        this.mod5Id = mod5Id;
    }

    public Integer getMod6Id() {
        return mod6Id;
    }

    public void setMod6Id(Integer mod6Id) {
        this.mod6Id = mod6Id;
    }

    public Integer getMod7Id() {
        return mod7Id;
    }

    public void setMod7Id(Integer mod7Id) {
        this.mod7Id = mod7Id;
    }

    public Integer getMod8Id() {
        return mod8Id;
    }

    public void setMod8Id(Integer mod8Id) {
        this.mod8Id = mod8Id;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
