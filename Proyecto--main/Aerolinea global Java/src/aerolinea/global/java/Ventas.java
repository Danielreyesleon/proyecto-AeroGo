package aerolinea.global.java;

public class Ventas {

    public static Reserva comprar(Vuelo vuelo, Pasajero p, int fila, int col) {
        if (!vuelo.asientoDisponible(fila, col)) {
            return null;
        }
        Asiento a = vuelo.getAvion().getAsientos()[fila][col];
        double pr = Tarificador.calcularPrecio(vuelo, a, p);
        vuelo.ocuparAsiento(fila, col);
        Tiquete t = new Tiquete(p, vuelo, fila, col, pr);
        FinanzasAerolinea.get().sumarTiquete(pr);
        return new Reserva(t);
    }
}
