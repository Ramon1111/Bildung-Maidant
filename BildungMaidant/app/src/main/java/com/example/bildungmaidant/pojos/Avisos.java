package com.example.bildungmaidant.pojos;

public class Avisos {
    private String claveAviso;
    private String administrador;
    private String grupoPertenece;
    private String titulo;
    private String descripcion;

    public Avisos(String claveAviso, String administrador, String grupoPertenece, String titulo, String descripcion) {
        this.claveAviso = claveAviso;
        this.administrador = administrador;
        this.grupoPertenece = grupoPertenece;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getClaveAviso() {
        return claveAviso;
    }

    public void setClaveAviso(String claveAviso) {
        this.claveAviso = claveAviso;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getGrupoPertenece() {
        return grupoPertenece;
    }

    public void setGrupoPertenece(String grupoPertenece) {
        this.grupoPertenece = grupoPertenece;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
