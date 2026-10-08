package com.museo.catalogoarte.model;

public class Usuario {
    private int id;
    private String nombre;
    private String correoElectronico;
    private String contrasenaHash;
    private String rol;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
