package aerolinea.global.java;

public class VectorReservas {

    private Reserva[] datos = new Reserva[20];
    private int n = 0;

    public void agregar(Reserva r) {
        if (n == datos.length) {
            Reserva[] tmp = new Reserva[n * 2];
            for (int i = 0; i < n; i++) {
                tmp[i] = datos[i];
            }
            datos = tmp;
        }
        datos[n++] = r;
    }

    public Reserva get(int i) {
        return datos[i];
    }

    public int size() {
        return n;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public Reserva buscarPorTiquete(String num) {
        for (int i = 0; i < n; i++) {
            if (datos[i].getTiquete().getNumero().equalsIgnoreCase(num)) {
                return datos[i];
            }
        }
        return null;
    }

    public int cancelarPorCedula(String id) {
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            String rid = datos[i].getTiquete().getPasajero().getId();
            if (rid.equalsIgnoreCase(id) && !datos[i].getEstado().equals(Reserva.CANCELADA)) {
                datos[i].cancelar();
                cnt++;
            }
        }
        return cnt;
    }
}
