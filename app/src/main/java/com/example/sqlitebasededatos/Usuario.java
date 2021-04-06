package com.example.sqlitebasededatos;

public class Usuario {

    public int documento;
    public String usuario;
    public String nombres;
    public String apellidos;
    public String contra;

    @Override
    public String toString() {
        return "Documentos: "+ documento + " " +"Usuario " + usuario;
    }
}
