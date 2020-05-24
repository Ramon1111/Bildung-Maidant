package com.example.bildungmaidant.pojos;

import java.util.ArrayList;

public class Grupo {

    private String nombreGrupo;
    private String administrador;
    private String claveGrupo;
    private ArrayList<Integer> numRecordatorios;
    private ArrayList<Integer> numRecursosDidacticos;
    private ArrayList<Integer> numAvisos;
    private ArrayList<Integer> miembrosGrupo;
    private Boolean estadoAltaBaja;

    public Grupo(){

    }

    //por lo mientras que aun no cargamos la info y aun no definimos bien los atributos de la clase
    public Grupo(String nombreGrupo, String administrador){
        this.nombreGrupo=nombreGrupo;
        this.administrador=administrador;
    }

    //puse los array para seguir la idea de relacionar la BD de Firebase con numeros enteros que se castean a cadenas
    public Grupo(String nombreGrupo, String administrador, String claveGrupo, ArrayList<Integer> numRecordatorios, ArrayList<Integer> numRecursosDidacticos, ArrayList<Integer> numAvisos, ArrayList<Integer> miembrosGrupo, Boolean estadoAltaBaja) {
        this.nombreGrupo = nombreGrupo;
        this.administrador = administrador;
        this.claveGrupo = claveGrupo;
        this.numRecordatorios = numRecordatorios;
        this.numRecursosDidacticos = numRecursosDidacticos;
        this.numAvisos = numAvisos;
        this.miembrosGrupo = miembrosGrupo;
        this.estadoAltaBaja = estadoAltaBaja;
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

    public ArrayList<Integer> getNumRecordatorios() {
        return numRecordatorios;
    }

    public void setNumRecordatorios(ArrayList<Integer> numRecordatorios) {
        this.numRecordatorios = numRecordatorios;
    }

    public ArrayList<Integer> getNumRecursosDidacticos() {
        return numRecursosDidacticos;
    }

    public void setNumRecursosDidacticos(ArrayList<Integer> numRecursosDidacticos) {
        this.numRecursosDidacticos = numRecursosDidacticos;
    }

    public ArrayList<Integer> getNumAvisos() {
        return numAvisos;
    }

    public void setNumAvisos(ArrayList<Integer> numAvisos) {
        this.numAvisos = numAvisos;
    }

    public ArrayList<Integer> getMiembrosGrupo() {
        return miembrosGrupo;
    }

    public void setMiembrosGrupo(ArrayList<Integer> miembrosGrupo) {
        this.miembrosGrupo = miembrosGrupo;
    }

    public Boolean getEstadoAltaBaja() {
        return estadoAltaBaja;
    }

    public void setEstadoAltaBaja(Boolean estadoAltaBaja) {
        this.estadoAltaBaja = estadoAltaBaja;
    }

}
