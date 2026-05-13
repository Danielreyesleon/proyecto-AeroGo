package aerolinea.global.java;

public class Tiquete {

    private static int contador = 1;
    private String numero;
    private Pasajero pasajero;
    private Vuelo vuelo;
    private int fila, columna;
    private double precio;

    public Tiquete(Pasajero pasajero, Vuelo vuelo, int fila, int columna, double precio) {
        this.numero = String.format("TK-%04d", contador++);
        this.pasajero = pasajero;
        this.vuelo = vuelo;
        this.fila = fila;
        this.columna = columna;
        this.precio = precio;
    }

    public String getNumero() {
        return numero;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public double getPrecio() {
        return precio;
    }
}
