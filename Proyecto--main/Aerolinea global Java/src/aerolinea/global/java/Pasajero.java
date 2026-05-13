package aerolinea.global.java;

public class Pasajero {

    private String id, nombre, nivel;

    public Pasajero(String id, String nombre, String nivel) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNivel() {
        return nivel;
    }
}
