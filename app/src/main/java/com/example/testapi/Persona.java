package com.example.testapi;

public class Persona {
    private int id;
    private String nombre;
    private String pais;

    public Persona(int id, String nombre, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public boolean compareTo(Persona p) {
        return this.nombre.compareTo(p.nombre) == 0;
    }
}
