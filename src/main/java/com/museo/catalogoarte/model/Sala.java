package com.museo.catalogoarte.model;

public class Sala {
    private int id;
    private String nombre;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return nombre; // Importante para que el ComboBox muestre el nombre
    }
}