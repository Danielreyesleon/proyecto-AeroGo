package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class VentanaReportes extends JDialog {

    private final VectorReservas reservas;
    private final Vuelo vuelo;

    public VentanaReportes(VectorReservas reservas, Vuelo vuelo, JFrame padre) {
        super(padre, "Reportes del Sistema", true);
        this.reservas = reservas;
        this.vuelo = vuelo;
        setSize(840, 620);
        setMinimumSize(new Dimension(720, 520));
        setLocationRelativeTo(padre);
        getContentPane().setBackground(VentanaPrincipal.CF);
        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(cuerpo(), BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel header() {
        JPanel p = gradPanel(VentanaPrincipal.CPUR);
        p.setPreferredSize(new Dimension(0, 56));
        p.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        JLabel t = lbl("REPORTES DEL SISTEMA", 17, true, VentanaPrincipal.CT);
        JLabel s = lbl("Ocupacion  |  Manifesto de comidas  |  Resumen financiero", 11, false, new Color(160, 120, 255));
        p.add(stack(t, s), BorderLayout.WEST);
        return p;
    }

    private JPanel cuerpo() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(VentanaPrincipal.CP);
        tabs.setForeground(VentanaPrincipal.CT);
        tabs.setFont(new Font("Dialog", Font.BOLD, 12));
        tabs.addTab("  OCUPACION  ", tabOcupacion());
        tabs.addTab("  MANIFIESTO  ", tabManifiesto());
        tabs.addTab("  FINANCIERO  ", tabFinanciero());
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(VentanaPrincipal.CF);
        p.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    private JPanel tabOcupacion() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(VentanaPrincipal.CF);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        Asiento[][] seats = vuelo.getAvion().getAsientos();
        int tP = 0, oP = 0, tE = 0, oE = 0, tEc = 0, oEc = 0;
        for (Asiento[] fila : seats) {
            for (Asiento a : fila) {
                if (a.getClase() == TipoClase.PRIMERA) {
                    tP++;
                    if (a.isOcupado()) {
                        oP++;
                    }
                } else if (a.getClase() == TipoClase.EJECUTIVA) {
                    tE++;
                    if (a.isOcupado()) {
                        oE++;
                    }
                } else {
                    tEc++;
                    if (a.isOcupado()) {
                        oEc++;
                    }
                }
            }
        }
        int tTot = tP + tE + tEc, oTot = oP + oE + oEc;
        double pct = tTot == 0 ? 0 : oTot * 100.0 / tTot;

        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 0));
        cards.setOpaque(false);
        cards.setPreferredSize(new Dimension(0, 95));
        cards.add(statCard("PRIMERA", oP + "/" + tP, pct2(oP, tP), new Color(212, 175, 95)));
        cards.add(statCard("EJECUTIVA", oE + "/" + tE, pct2(oE, tE), new Color(60, 130, 230)));
        cards.add(statCard("ECONOMICA", oEc + "/" + tEc, pct2(oEc, tEc), new Color(50, 200, 120)));
        Color ct = pct > 80 ? VentanaPrincipal.CR : VentanaPrincipal.CPUR;
        cards.add(statCard("TOTAL AVION", oTot + "/" + tTot, String.format("%.1f%%", pct), ct));
        p.add(cards, BorderLayout.NORTH);

        // Mapa visual
        JPanel mapWrap = new JPanel(new BorderLayout(0, 6));
        mapWrap.setOpaque(false);
        JLabel mtit = secLbl("MAPA DE ASIENTOS  (P=Primera  E=Ejecutiva  [ ]=Economia  [X]=Ocupado)");
        mapWrap.add(mtit, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(VentanaPrincipal.CF);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2, 2, 2, 2);
        int nf = seats.length, nc = seats[0].length;
        gc.gridy = 0;
        gc.gridx = 0;
        JLabel v0 = new JLabel("");
        v0.setPreferredSize(new Dimension(26, 16));
        grid.add(v0, gc);
        for (int c = 0; c < nc; c++) {
            gc.gridx = c + 1;
            JLabel lc = new JLabel(String.valueOf((char) ('A' + c)), SwingConstants.CENTER);
            lc.setFont(new Font("Monospaced", Font.BOLD, 9));
            lc.setForeground(VentanaPrincipal.CTD);
            lc.setPreferredSize(new Dimension(36, 16));
            grid.add(lc, gc);
        }
        for (int f = 0; f < nf; f++) {
            gc.gridy = f + 1;
            gc.gridx = 0;
            JLabel lf = new JLabel(String.valueOf(f + 1), SwingConstants.CENTER);
            lf.setFont(new Font("Monospaced", Font.BOLD, 9));
            lf.setForeground(VentanaPrincipal.CTD);
            lf.setPreferredSize(new Dimension(26, 32));
            grid.add(lf, gc);
            for (int c = 0; c < nc; c++) {
                gc.gridx = c + 1;
                Asiento a = seats[f][c];
                Color bg;
                if (a.isOcupado()) {
                    bg = new Color(70, 20, 20);
                } else if (a.getClase() == TipoClase.PRIMERA) {
                    bg = new Color(80, 65, 20);
                } else if (a.getClase() == TipoClase.EJECUTIVA) {
                    bg = new Color(20, 50, 100);
                } else {
                    bg = new Color(20, 65, 45);
                }
                JLabel cell = new JLabel(a.toString(), SwingConstants.CENTER);
                cell.setFont(new Font("Monospaced", Font.BOLD, 9));
                cell.setForeground(VentanaPrincipal.CT);
                cell.setBackground(bg);
                cell.setOpaque(true);
                cell.setPreferredSize(new Dimension(36, 32));
                cell.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1));
                grid.add(cell, gc);
            }
        }
        JScrollPane sc = new JScrollPane(grid);
        sc.getViewport().setBackground(VentanaPrincipal.CF);
        sc.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1));
        mapWrap.add(sc, BorderLayout.CENTER);
        p.add(mapWrap, BorderLayout.CENTER);
        return p;
    }

    private JPanel tabManifiesto() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(VentanaPrincipal.CF);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel tit = secLbl("MANIFESTO DE SERVICIOS ESPECIALES — listado para la tripulacion");
        p.add(tit, BorderLayout.NORTH);
        String[] cols = {"Asiento", "Pasajero", "Cedula", "Clase", "Menu Especial", "Estado"};
        DefaultTableModel m = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            if (r.getEstado().equals(Reserva.CANCELADA)) {
                continue;
            }
            Tiquete t = r.getTiquete();
            char col = (char) ('A' + t.getColumna());
            Asiento a = t.getVuelo().getAvion().getAsientos()[t.getFila()][t.getColumna()];
            m.addRow(new Object[]{(t.getFila() + 1) + "" + col, t.getPasajero().getNombre(),
                t.getPasajero().getId(), a.getClase().toString(), r.getMenuEspecial(), r.getEstado()});
        }
        JTable tbl = new JTable(m);
        estilizar(tbl);
        tbl.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setFont(new Font("Dialog", Font.BOLD, 11));
                String mn = v == null ? "" : v.toString();
                if (mn.equals("Estandar")) {
                    setForeground(VentanaPrincipal.CTD);
                } else if (mn.equals("Vegetariano")) {
                    setForeground(VentanaPrincipal.CV);
                } else {
                    setForeground(VentanaPrincipal.CD);
                }
                setBackground(s ? VentanaPrincipal.CP2 : VentanaPrincipal.CP);
                return this;
            }
        });
        JScrollPane sc = new JScrollPane(tbl);
        sc.getViewport().setBackground(VentanaPrincipal.CP);
        sc.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1));
        p.add(sc, BorderLayout.CENTER);
        return p;
    }

    private JPanel tabFinanciero() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(VentanaPrincipal.CF);
        p.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        JLabel tit = secLbl("RESUMEN FINANCIERO DE LA AEROLINEA");
        p.add(tit, BorderLayout.NORTH);
        FinanzasAerolinea fin = FinanzasAerolinea.get();
        JPanel cards = new JPanel(new GridLayout(2, 2, 12, 12));
        cards.setOpaque(false);
        cards.add(montoCard("VENTA DE TIQUETES", fin.getTiquetes(), VentanaPrincipal.CA));
        cards.add(montoCard("VENTAS A BORDO", fin.getABordo(), VentanaPrincipal.CV));
        cards.add(montoCard("PEN. EQUIPAJE", fin.getEquipaje(), VentanaPrincipal.CAMB));
        cards.add(montoCard("PEN. CANCELACIONES", fin.getCancelaciones(), VentanaPrincipal.CR));
        p.add(cards, BorderLayout.CENTER);
        JPanel total = new JPanel(new BorderLayout(0, 6));
        total.setBackground(VentanaPrincipal.CP2);
        total.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(VentanaPrincipal.CD, 1), BorderFactory.createEmptyBorder(14, 20, 14, 20)));
        JLabel lt = new JLabel("TOTAL GENERAL RECAUDADO");
        lt.setFont(new Font("Monospaced", Font.BOLD, 11));
        lt.setForeground(VentanaPrincipal.CTD);
        lt.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lv = new JLabel(String.format("$ %.2f", fin.getTotal()));
        lv.setFont(new Font("Dialog", Font.BOLD, 30));
        lv.setForeground(VentanaPrincipal.CDC);
        lv.setHorizontalAlignment(SwingConstants.CENTER);
        total.add(lt, BorderLayout.NORTH);
        total.add(lv, BorderLayout.CENTER);
        p.add(total, BorderLayout.SOUTH);
        return p;
    }

    private JPanel statCard(String titulo, String valor, String pct, Color c) {
        JPanel p = new JPanel(new GridLayout(3, 1, 0, 3));
        p.setBackground(VentanaPrincipal.CP);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, c), BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        JLabel lt = new JLabel(titulo);
        lt.setFont(new Font("Monospaced", Font.BOLD, 9));
        lt.setForeground(VentanaPrincipal.CTD);
        JLabel lv = new JLabel(valor);
        lv.setFont(new Font("Dialog", Font.BOLD, 18));
        lv.setForeground(VentanaPrincipal.CT);
        JLabel lp = new JLabel(pct);
        lp.setFont(new Font("Monospaced", Font.BOLD, 12));
        lp.setForeground(c);
        p.add(lt);
        p.add(lv);
        p.add(lp);
        return p;
    }

    private JPanel montoCard(String titulo, double monto, Color c) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 5));
        p.setBackground(VentanaPrincipal.CP);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, c), BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        JLabel lt = new JLabel(titulo);
        lt.setFont(new Font("Monospaced", Font.BOLD, 9));
        lt.setForeground(VentanaPrincipal.CTD);
        JLabel lv = new JLabel(String.format("$ %.2f", monto));
        lv.setFont(new Font("Dialog", Font.BOLD, 18));
        lv.setForeground(c);
        p.add(lt);
        p.add(lv);
        return p;
    }

    private String pct2(int o, int t) {
        return t == 0 ? "0%" : String.format("%.0f%%", o * 100.0 / t);
    }

    private JPanel footer() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, VentanaPrincipal.CB));
        JLabel l = new JLabel("> Datos en tiempo real. Reabre la ventana para actualizar.");
        l.setFont(new Font("Monospaced", Font.PLAIN, 11));
        l.setForeground(VentanaPrincipal.CTD);
        p.add(l);
        return p;
    }

    private JPanel gradPanel(Color lc) {
        JPanel p = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(6, 8, 20), getWidth(), 0, new Color(18, 26, 50)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(lc);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    private JLabel lbl(String t, int sz, boolean b, Color c) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Dialog", b ? Font.BOLD : Font.PLAIN, sz));
        l.setForeground(c);
        return l;
    }

    private JPanel stack(JLabel a, JLabel b) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 2));
        p.setOpaque(false);
        p.add(a);
        p.add(b);
        return p;
    }

    private JLabel secLbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Monospaced", Font.BOLD, 10));
        l.setForeground(VentanaPrincipal.CD);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void estilizar(JTable t) {
        t.setBackground(VentanaPrincipal.CP);
        t.setForeground(VentanaPrincipal.CT);
        t.setFont(new Font("Dialog", Font.PLAIN, 12));
        t.setRowHeight(30);
        t.setGridColor(VentanaPrincipal.CB);
        t.setShowVerticalLines(false);
        t.setSelectionBackground(VentanaPrincipal.CP2);
        t.setSelectionForeground(VentanaPrincipal.CD);
        t.getTableHeader().setBackground(VentanaPrincipal.CP2);
        t.getTableHeader().setForeground(VentanaPrincipal.CD);
        t.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 11));
    }
}
