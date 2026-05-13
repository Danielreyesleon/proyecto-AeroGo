package aerolinea.global.java;

public class Reserva {

    public static final String ACTIVA = "Activa";
    public static final String CANCELADA = "Cancelada";
    public static final String CHECKIN = "Check-in Realizado";
    public static final String ABORDADO = "Abordado";

    private static final double PESO_MAX = 23.0;
    private static final double CARGO_KG = 15.0;
    private static final int MAX_MAL = 10;

    private Tiquete tiquete;
    private String estado;
    private String menuEspecial = "Estandar";
    private double[] pesos = new double[MAX_MAL];
    private int numMaletas = 0;
    private double penEquipaje = 0;
    private double reembolso = 0;
    private double penCancel = 0;

    public Reserva(Tiquete t) {
        this.tiquete = t;
        this.estado = ACTIVA;
    }

    public Tiquete getTiquete() {
        return tiquete;
    }

    public String getEstado() {
        return estado;
    }

    public String getMenuEspecial() {
        return menuEspecial;
    }

    public void setMenuEspecial(String m) {
        menuEspecial = m;
    }

    public int getNumMaletas() {
        return numMaletas;
    }

    public double getPesoMaleta(int i) {
        return pesos[i];
    }

    public double getPenEquipaje() {
        return penEquipaje;
    }

    public double getReembolso() {
        return reembolso;
    }

    public double getPenCancel() {
        return penCancel;
    }

    public void agregarMaleta(double kg) {
        if (numMaletas >= MAX_MAL) {
            return;
        }
        pesos[numMaletas++] = kg;
        if (kg > PESO_MAX) {
            double extra = (kg - PESO_MAX) * CARGO_KG;
            penEquipaje += extra;
            FinanzasAerolinea.get().sumarEquipaje(extra);
        }
    }

    public void realizarCheckin() {
        if (estado.equals(ACTIVA)) {
            estado = CHECKIN;
        }
    }

    public void confirmarAbordaje() {
        if (estado.equals(CHECKIN)) {
            estado = ABORDADO;
        }
    }

    public void cancelar() {
        if (estado.equals(CANCELADA)) {
            return;
        }
        double precio = tiquete.getPrecio();
        String nivel = tiquete.getPasajero().getNivel().toUpperCase();
        if (nivel.equals("PLATINO")) {
            reembolso = precio;
            penCancel = 0;
        } else {
            penCancel = precio * 0.30;
            reembolso = precio - penCancel;
        }
        tiquete.getVuelo().getAvion()
                .getAsientos()[tiquete.getFila()][tiquete.getColumna()].liberar();
        FinanzasAerolinea.get().sumarCancelacion(penCancel);
        estado = CANCELADA;
    }
}
