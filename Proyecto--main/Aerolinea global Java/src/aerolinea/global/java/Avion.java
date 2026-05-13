package aerolinea.global.java;

public class Avion {

    private String matricula, modelo;
    private int filas, columnas;
    private Asiento[][] asientos;

    public Avion(String matricula, String modelo, int filas, int columnas) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.filas = filas;
        this.columnas = columnas;
        asientos = new Asiento[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                TipoClase c = (i < 2) ? TipoClase.PRIMERA
                        : (i < 5) ? TipoClase.EJECUTIVA
                                : TipoClase.ECONOMICA;
                asientos[i][j] = new Asiento(i, j, c);
            }
        }
    }

    public Asiento[][] getAsientos() {
        return asientos;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public double ocupacion() {
        int ocu = 0, total = filas * columnas;
        for (Asiento[] fila : asientos) {
            for (Asiento a : fila) {
                if (a.isOcupado()) {
                    ocu++;
                }
            }
        }
        return (ocu * 100.0) / total;
    }

    public String toString() {
        return matricula + " - " + modelo;
    }
}
