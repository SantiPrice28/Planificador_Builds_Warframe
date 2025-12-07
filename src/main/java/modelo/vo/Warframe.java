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
    private double duracion;
    private double eficiencia;
    private double fuerza;
    private double rango;

    public Warframe(int id, String nombre, int salud, int escudo, int armadura, int energia, String descripcion, double duracion, double eficiencia, double fuerza, double rango) {
        this.id = id;
        this.nombre = nombre;
        this.salud = salud;
        this.escudo = escudo;
        this.armadura = armadura;
        this.energia = energia;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.eficiencia = eficiencia;
        this.fuerza = fuerza;
        this.rango = rango;
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

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public double getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(double eficiencia) {
        this.eficiencia = eficiencia;
    }

    public double getFuerza() {
        return fuerza;
    }

    public void setFuerza(double fuerza) {
        this.fuerza = fuerza;
    }

    public double getRango() {
        return rango;
    }

    public void setRango(double rango) {
        this.rango = rango;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
