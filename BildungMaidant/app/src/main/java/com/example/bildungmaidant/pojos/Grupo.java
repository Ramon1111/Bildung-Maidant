package com.example.bildungmaidant.pojos;

import java.util.ArrayList;

public class Grupo {

    private String nombreGrupo;
    private String administrador;
    private String claveGrupo;
    private String descripcion;
    private ArrayList<String> listaRecordatorios;
    private ArrayList<String> listaRecursosDidacticos;
    private ArrayList<String> listaAvisos;
    private ArrayList<String> miembrosGrupo;
    private Boolean estadoAltaBaja;

    public Grupo(){

    }

    //por lo mientras que aun no cargamos la info y aun no definimos bien los atributos de la clase
    public Grupo(String nombreGrupo, String administrador, String claveGrupo){
        this.nombreGrupo=nombreGrupo;
        this.administrador=administrador;
        this.claveGrupo=claveGrupo;
    }

    public Grupo(String nombreGrupo, String administrador, String claveGrupo, ArrayList<String> listaRecordatorios, ArrayList<String> listaRecursosDidacticos, ArrayList<String> listaAvisos, ArrayList<String> miembrosGrupo, Boolean estadoAltaBaja, String descripcion) {
        this.nombreGrupo = nombreGrupo;
        this.administrador = administrador;
        this.claveGrupo = claveGrupo;
        this.listaRecordatorios = listaRecordatorios;
        this.listaRecursosDidacticos = listaRecursosDidacticos;
        this.listaAvisos = listaAvisos;
        this.miembrosGrupo = miembrosGrupo;
        this.estadoAltaBaja = estadoAltaBaja;
        this.descripcion=descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getClaveGrupo() {
        return claveGrupo;
    }

    public void setClaveGrupo(String claveGrupo) {
        this.claveGrupo = claveGrupo;
    }

    public ArrayList<String > getListaRecordatorios() {
        return listaRecordatorios;
    }

    public void setListaRecordatorios(ArrayList<String> ListaRecordatorios) {
        this.listaRecordatorios = listaRecordatorios;
    }

    public ArrayList<String> getListaRecursosDidacticos() {
        return listaRecursosDidacticos;
    }

    public void setListaRecursosDidacticos(ArrayList<String> listaRecursosDidacticos) {
        this.listaRecursosDidacticos = listaRecursosDidacticos;
    }

    public ArrayList<String> getListaAvisos() {
        return listaAvisos;
    }

    public void setListaAvisos(ArrayList<String> listaAvisos) {
        this.listaAvisos = listaAvisos;
    }

    public ArrayList<String> getMiembrosGrupo() {
        return miembrosGrupo;
    }

    public void setMiembrosGrupo(ArrayList<String> miembrosGrupo) {
        this.miembrosGrupo = miembrosGrupo;
    }

    public Boolean getEstadoAltaBaja() {
        return estadoAltaBaja;
    }

    public void setEstadoAltaBaja(Boolean estadoAltaBaja) {
        this.estadoAltaBaja = estadoAltaBaja;
    }

}
