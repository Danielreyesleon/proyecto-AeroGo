package aerolinea.global.java;

public class Asiento {

    private int fila, columna;
    private TipoClase clase;
    private boolean ocupado;

    public Asiento(int fila, int columna, TipoClase clase) {
        this.fila = fila;
        this.columna = columna;
        this.clase = clase;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void ocupar() {
        ocupado = true;
    }

    public void liberar() {
        ocupado = false;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public TipoClase getClase() {
        return clase;
    }

    public String toString() {
        if (ocupado) {
            return "[X]";
        }
        if (clase == TipoClase.PRIMERA) {
            return "[P]";
        }
        if (clase == TipoClase.EJECUTIVA) {
            return "[E]";
        }
        return "[ ]";
    }
}
