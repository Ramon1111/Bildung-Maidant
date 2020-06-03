package com.example.bildungmaidant.pojos;

public class Usuario {
    private String nombres, apellidos, correo;

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Usuario(String nombres, String apellidos, String correo){
        this.nombres=nombres;
        this.apellidos=apellidos;
        this.correo=correo;
    }

}
