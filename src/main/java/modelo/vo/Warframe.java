/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

/**
 *
 * @author aizpu
 */
public class Warframe {
    private int id;
    private String nombre;
    private int salud;
    private int escudo;
    private int armadura;
    private int energia;
    private String descripcion;

    public Warframe(int id, String nombre, int salud, int escudo, int armadura, int energia, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.salud = salud;
        this.escudo = escudo;
        this.armadura = armadura;
        this.energia = energia;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getSalud() {
        return salud;
    }

    public int getEscudo() {
        return escudo;
    }

    public int getArmadura() {
        return armadura;
    }

    public int getEnergia() {
        return energia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    
}
