package com.example.bildungmaidant.pojos;

public class Recordatorio {
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String administrador;
    private String claveRecordatorio;
    private String grupoPertenece;
    private Boolean estadoEnProceso;

    public Recordatorio(){

    }

    public Recordatorio(String titulo, String descripcion, String fecha, String hora, String administrador, String claveRecordatorio, String grupoPertenece, Boolean estadoEnProceso) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.administrador = administrador;
        this.claveRecordatorio = claveRecordatorio;
        this.grupoPertenece = grupoPertenece;
        this.estadoEnProceso = estadoEnProceso;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getClaveRecordatorio() {
        return claveRecordatorio;
    }

    public void setClaveRecordatorio(String claveRecordatorio) {
        this.claveRecordatorio = claveRecordatorio;
    }

    public String getGrupoPertenece() {
        return grupoPertenece;
    }

    public void setGrupoPertenece(String grupoPertenece) {
        this.grupoPertenece = grupoPertenece;
    }

    public Boolean getEstadoEnProceso() {
        return estadoEnProceso;
    }

    public void setEstadoEnProceso(Boolean estadoEnProceso) {
        this.estadoEnProceso = estadoEnProceso;
    }
}
