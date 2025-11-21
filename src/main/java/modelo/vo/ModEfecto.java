/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

/**
 *
 * @author aizpu
 */
public class ModEfecto {
    private int id;
    private int id_mod;
    private String objetivo;
    private String campo;
    private String operacion;
    private double valor;
    private String descripcion;
    private String atributo;

    public ModEfecto(int id, int id_mod, String objetivo, String campo, String operacion, double valor, String descripcion, String atributo) {
        this.id = id;
        this.id_mod = id_mod;
        this.objetivo = objetivo;
        this.campo = campo;
        this.operacion = operacion;
        this.valor = valor;
        this.descripcion = descripcion;
        this.atributo = atributo;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_mod() {
        return id_mod;
    }

    public void setId_mod(int id_mod) {
        this.id_mod = id_mod;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
    
}
