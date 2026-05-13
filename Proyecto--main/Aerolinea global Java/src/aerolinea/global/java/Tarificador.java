package aerolinea.global.java;
public class Tarificador {
    public static double calcularPrecio(Vuelo vuelo, Asiento asiento, Pasajero pasajero) {
        double precio = vuelo.getPrecioBase();
        if      (asiento.getClase() == TipoClase.PRIMERA)   precio *= 2.0;
        else if (asiento.getClase() == TipoClase.EJECUTIVA) precio *= 1.5;
        if (vuelo.getAvion().ocupacion() > 80) precio *= 1.20;
        String nv = pasajero.getNivel().toUpperCase();
        if      (nv.equals("PLATINO")) precio *= 0.90;
        else if (nv.equals("ORO"))     precio *= 0.95;
        return precio;
    }
}
