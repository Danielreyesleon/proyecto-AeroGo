package aerolinea.global.java;

public class Vuelo {

    private String codigo, origen, destino, fecha;
    private Avion avion;
    private double precioBase;

    public Vuelo(String codigo, String origen, String destino,
            String fecha, Avion avion, double precioBase) {
        this.codigo = codigo;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.avion = avion;
        this.precioBase = precioBase;
    }

    public Vuelo(String codigo, Avion avion, double precioBase) {
        this(codigo, "N/A", "N/A", "N/A", avion, precioBase);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public String getFecha() {
        return fecha;
    }

    public Avion getAvion() {
        return avion;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public boolean asientoDisponible(int f, int c) {
        return !avion.getAsientos()[f][c].isOcupado();
    }

    public void ocuparAsiento(int f, int c) {
        avion.getAsientos()[f][c].ocupar();
    }

    public double calcularOcupacion() {
        return avion.ocupacion() / 100.0;
    }

    public String toString() {
        return codigo + " | " + origen + " -> " + destino;
    }
}
