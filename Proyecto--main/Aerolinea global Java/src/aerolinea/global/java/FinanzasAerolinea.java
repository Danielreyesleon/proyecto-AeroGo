package aerolinea.global.java;

public class FinanzasAerolinea {

    private static FinanzasAerolinea inst;
    private double tiquetes, abordo, equipaje, cancelaciones;

    private FinanzasAerolinea() {
    }

    public static FinanzasAerolinea get() {
        if (inst == null) {
            inst = new FinanzasAerolinea();
        }
        return inst;
    }

    public void sumarTiquete(double m) {
        tiquetes += m;
    }

    public void sumarABordo(double m) {
        abordo += m;
    }

    public void sumarEquipaje(double m) {
        equipaje += m;
    }

    public void sumarCancelacion(double m) {
        cancelaciones += m;
    }

    public double getTiquetes() {
        return tiquetes;
    }

    public double getABordo() {
        return abordo;
    }

    public double getEquipaje() {
        return equipaje;
    }

    public double getCancelaciones() {
        return cancelaciones;
    }

    public double getTotal() {
        return tiquetes + abordo + equipaje + cancelaciones;
    }
}
