/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

/**
 *
 * @author aizpu
 */
public class Arma {
    private int id;
    private String nombre;
    private int idTipoArma;
    private double dañoImpacto;
    private double dañoPerforante;
    private double dañoCortante;
    private double dañoFrio;
    private double dañoElectrico;
    private double dañoCalor;
    private double dañoToxina;
    private double critico;
    private double multCritico;
    private double estado;
    private double precision;
    private double cadencia;
    private String descripcion;

    public Arma(int id, String nombre, int idTipoArma, double dañoImpacto, double dañoPerforante, double dañoCortante, double dañoFrio, double dañoElectrico, double dañoCalor, double dañoToxina, double critico, double multCritico, double estado, double precision, double fireRate, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.idTipoArma = idTipoArma;
        this.dañoImpacto = dañoImpacto;
        this.dañoPerforante = dañoPerforante;
        this.dañoCortante = dañoCortante;
        this.dañoFrio = dañoFrio;
        this.dañoElectrico = dañoElectrico;
        this.dañoCalor = dañoCalor;
        this.dañoToxina = dañoToxina;
        this.critico = critico;
        this.multCritico = multCritico;
        this.estado = estado;
        this.precision = precision;
        this.cadencia = fireRate;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdTipoArma() {
        return idTipoArma;
    }

    public double getDañoImpacto() {
        return dañoImpacto;
    }

    public double getDañoPerforante() {
        return dañoPerforante;
    }

    public double getDañoCortante() {
        return dañoCortante;
    }

    public double getDañoFrio() {
        return dañoFrio;
    }

    public double getDañoElectrico() {
        return dañoElectrico;
    }

    public double getDañoCalor() {
        return dañoCalor;
    }

    public double getDañoToxina() {
        return dañoToxina;
    }

    public double getCritico() {
        return critico;
    }

    public double getMultCritico() {
        return multCritico;
    }

    public double getEstado() {
        return estado;
    }

    public double getPrecision() {
        return precision;
    }

    public double getCadencia() {
        return cadencia;
    }

    public String getDescripcion() {
        return descripcion;
    }
    

    @Override
    public String toString() {
        return nombre;
    }
}

