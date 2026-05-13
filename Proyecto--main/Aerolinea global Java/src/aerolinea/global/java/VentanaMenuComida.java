package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class VentanaMenuComida extends JFrame {

    private static final Color CF = new Color(10, 12, 20);
    private static final Color CP = new Color(18, 22, 36);
    private static final Color CP2 = new Color(24, 30, 50);
    private static final Color CB = new Color(40, 50, 80);
    private static final Color CD = new Color(212, 175, 95);
    private static final Color CDC = new Color(240, 210, 130);
    private static final Color CA = new Color(60, 130, 230);
    private static final Color CV = new Color(50, 200, 120);
    private static final Color CR = new Color(220, 70, 70);
    private static final Color CT = new Color(220, 225, 240);
    private static final Color CTD = new Color(110, 120, 150);

    private final Asiento asiento;
    private final String nombrePasajero;
    private final Reserva reserva;

    private static final int MAX = 20;
    private final String[] mCod = new String[MAX], mNom = new String[MAX],
            mDesc = new String[MAX], mCat = new String[MAX];
    private final double[] mPr = new double[MAX];
    private final TipoClase[] mCls = new TipoClase[MAX];
    private int numM = 0;

    private final String[] cCod = new String[MAX];
    private final int[] cCant = new int[MAX];
    private int cSize = 0;

    private JPanel panelCarrito;
    private JLabel lblTotal, lblEstado, lblBadge;

    public VentanaMenuComida(Asiento asiento, String nombre, Reserva reserva) {
        this.asiento = asiento;
        this.nombrePasajero = nombre;
        this.reserva = reserva;
        llenarMenu();
        buildUI();
        refreshCarrito();
        setTitle("AeroGo  —  Menu a Bordo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1040, 660);
        setMinimumSize(new Dimension(840, 540));
        setLocationRelativeTo(null);
        getContentPane().setBackground(CF);
        setVisible(true);
    }

    private void am(String cod, String nom, String desc, double pr, TipoClase cls, String cat) {
        mCod[numM] = cod;
        mNom[numM] = nom;
        mDesc[numM] = desc;
        mPr[numM] = pr;
        mCls[numM] = cls;
        mCat[numM] = cat;
        numM++;
    }

    private void llenarMenu() {
        am("B01", "Agua mineral", "Botella 500 ml", 2.50, null, "BEBIDA");
        am("B02", "Jugo de naranja", "Vaso 250 ml, natural", 3.00, null, "BEBIDA");
        am("B03", "Refresco", "Lata 355 ml", 2.00, null, "BEBIDA");
        am("S01", "Snack de mani", "Bolsa 50 g", 2.00, null, "SNACK");
        am("S02", "Galletas saladas", "Paquete surtido", 2.50, null, "SNACK");
        am("C01", "Sandwich de pollo", "Pan integral, lechuga", 7.00, null, "COMIDA");
        am("C02", "Wrap vegetariano", "Tortilla, hummus, vegetales", 6.50, null, "COMIDA");
        am("E01", "Vino tinto", "Copa 150 ml, Merlot", 9.00, TipoClase.EJECUTIVA, "PREMIUM");
        am("E02", "Cerveza artesanal", "Lata 355 ml", 6.00, TipoClase.EJECUTIVA, "PREMIUM");
        am("E03", "Plato caliente", "Pollo o pasta + pan", 18.00, TipoClase.EJECUTIVA, "PREMIUM");
        am("P01", "Champagne", "Copa 150 ml, Brut", 14.00, TipoClase.PRIMERA, "PRIMERA");
        am("P02", "Menu gourmet", "3 tiempos + maridaje", 35.00, TipoClase.PRIMERA, "PRIMERA");
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(norte(), BorderLayout.NORTH);
        add(centro(), BorderLayout.CENTER);
        add(sur(), BorderLayout.SOUTH);
    }

    private JPanel norte() {
        JPanel p = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(6, 8, 20), getWidth(), 0, new Color(18, 26, 50)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(CD);
                g2.fillRect(0, getHeight() - 2, getWidth(), 2);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 68));
        p.setBorder(BorderFactory.createEmptyBorder(13, 24, 13, 24));
        JPanel iz = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iz.setOpaque(false);
        JLabel logo = new JLabel("AEROGO");
        logo.setFont(new Font("Monospaced", Font.BOLD, 13));
        logo.setForeground(CD);
        logo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CD, 1), BorderFactory.createEmptyBorder(3, 8, 3, 8)));
        JLabel sep = new JLabel("   //   ");
        sep.setFont(new Font("Dialog", Font.PLAIN, 16));
        sep.setForeground(CB);
        JLabel tit = new JLabel("MENU A BORDO");
        tit.setFont(new Font("Dialog", Font.BOLD, 18));
        tit.setForeground(CT);
        iz.add(logo);
        iz.add(sep);
        iz.add(tit);
        JPanel dr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        dr.setOpaque(false);
        char col = (char) ('A' + asiento.getColumna());
        dr.add(chip("PASAJERO", nombrePasajero.toUpperCase()));
        dr.add(chip("ASIENTO", (asiento.getFila() + 1) + "" + col));
        dr.add(chip("CLASE", asiento.getClase().toString()));
        p.add(iz, BorderLayout.WEST);
        p.add(dr, BorderLayout.EAST);
        return p;
    }

    private JPanel chip(String e, String v) {
        JPanel c = new JPanel(new BorderLayout(0, 2));
        c.setOpaque(false);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(CB, 1), BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        JLabel le = new JLabel(e);
        le.setFont(new Font("Monospaced", Font.PLAIN, 9));
        le.setForeground(CD);
        JLabel lv = new JLabel(v);
        lv.setFont(new Font("Dialog", Font.BOLD, 12));
        lv.setForeground(CT);
        c.add(le, BorderLayout.NORTH);
        c.add(lv, BorderLayout.CENTER);
        return c;
    }

    private JPanel centro() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(CF);
        p.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        p.add(panelLista(), BorderLayout.CENTER);
        p.add(panelCarritoUI(), BorderLayout.EAST);
        return p;
    }

    private JPanel panelLista() {
        JPanel wrap = new JPanel(new BorderLayout(0, 8));
        wrap.setBackground(CF);
        JLabel sub = new JLabel("  Selecciona tus items");
        sub.setFont(new Font("Dialog", Font.BOLD, 13));
        sub.setForeground(CTD);
        wrap.add(sub, BorderLayout.NORTH);
        JPanel grid = new JPanel();
        grid.setLayout(new BoxLayout(grid, BoxLayout.Y_AXIS));
        grid.setBackground(CF);
        String catAct = "";
        for (int i = 0; i < numM; i++) {
            if (!disponible(i)) {
                continue;
            }
            if (!mCat[i].equals(catAct)) {
                catAct = mCat[i];
                grid.add(Box.createVerticalStrut(6));
                grid.add(catLabel(catAct));
                grid.add(Box.createVerticalStrut(5));
            }
            final int idx = i;
            grid.add(itemCard(idx));
            grid.add(Box.createVerticalStrut(4));
        }
        JScrollPane sc = new JScrollPane(grid);
        sc.setOpaque(false);
        sc.getViewport().setOpaque(false);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sc.getVerticalScrollBar().setUnitIncrement(16);
        wrap.add(sc, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel catLabel(String cat) {
        Color c;
        if (cat.equals("BEBIDA")) {
            c = new Color(30, 80, 160);
        } else if (cat.equals("SNACK")) {
            c = new Color(100, 60, 160);
        } else if (cat.equals("COMIDA")) {
            c = new Color(160, 80, 30);
        } else {
            c = new Color(150, 120, 20);
        }
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        JLabel l = new JLabel("  " + cat + "  ");
        l.setFont(new Font("Monospaced", Font.BOLD, 10));
        l.setForeground(Color.WHITE);
        l.setBackground(c);
        l.setOpaque(true);
        l.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
        p.add(l);
        return p;
    }

    private JPanel itemCard(int idx) {
        Color[] bg = {CP};
        JPanel t = new JPanel(new BorderLayout(14, 0)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg[0]);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 9, 9));
                g2.dispose();
            }
        };
        t.setOpaque(false);
        t.setBorder(BorderFactory.createEmptyBorder(11, 14, 11, 14));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        t.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel tx = new JPanel(new BorderLayout(0, 3));
        tx.setOpaque(false);
        JLabel ln = new JLabel(mNom[idx]);
        ln.setFont(new Font("Dialog", Font.BOLD, 13));
        ln.setForeground(CT);
        JLabel ld = new JLabel(mDesc[idx]);
        ld.setFont(new Font("Dialog", Font.PLAIN, 11));
        ld.setForeground(CTD);
        tx.add(ln, BorderLayout.NORTH);
        tx.add(ld, BorderLayout.CENTER);
        JPanel dr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        dr.setOpaque(false);
        JLabel lp = new JLabel(String.format("$%.2f", mPr[idx]));
        lp.setFont(new Font("Dialog", Font.BOLD, 15));
        lp.setForeground(CD);
        JButton ba = boton("+ AGREGAR", CV);
        ba.addActionListener(e -> agregar(idx));
        dr.add(lp);
        dr.add(ba);
        t.add(tx, BorderLayout.CENTER);
        t.add(dr, BorderLayout.EAST);
        t.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                bg[0] = CP2;
                t.repaint();
            }

            public void mouseExited(MouseEvent e) {
                bg[0] = CP;
                t.repaint();
            }
        });
        return t;
    }

    private JPanel panelCarritoUI() {
        JPanel lat = new JPanel(new BorderLayout());
        lat.setPreferredSize(new Dimension(280, 0));
        lat.setBackground(CP);
        lat.setBorder(BorderFactory.createLineBorder(CB, 1));
        JPanel hdr = new JPanel(new BorderLayout());
        hdr.setBackground(CP2);
        hdr.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CB), BorderFactory.createEmptyBorder(11, 14, 11, 14)));
        JLabel tit = new JLabel("MI PEDIDO");
        tit.setFont(new Font("Monospaced", Font.BOLD, 13));
        tit.setForeground(CD);
        lblBadge = new JLabel("0");
        lblBadge.setFont(new Font("Dialog", Font.BOLD, 11));
        lblBadge.setForeground(Color.WHITE);
        lblBadge.setBackground(CA);
        lblBadge.setOpaque(true);
        lblBadge.setHorizontalAlignment(SwingConstants.CENTER);
        lblBadge.setPreferredSize(new Dimension(24, 20));
        hdr.add(tit, BorderLayout.WEST);
        hdr.add(lblBadge, BorderLayout.EAST);
        panelCarrito = new JPanel();
        panelCarrito.setLayout(new BoxLayout(panelCarrito, BoxLayout.Y_AXIS));
        panelCarrito.setBackground(CP);
        panelCarrito.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane sc = new JScrollPane(panelCarrito);
        sc.setOpaque(false);
        sc.getViewport().setBackground(CP);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel foot = new JPanel(new BorderLayout(0, 10));
        foot.setBackground(CP2);
        foot.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, CB), BorderFactory.createEmptyBorder(13, 14, 13, 14)));
        lblTotal = new JLabel("TOTAL:  $0.00");
        lblTotal.setFont(new Font("Monospaced", Font.BOLD, 15));
        lblTotal.setForeground(CDC);
        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
        JButton bok = boton("CONFIRMAR PEDIDO", CD);
        bok.setForeground(new Color(10, 12, 20));
        bok.setFont(new Font("Dialog", Font.BOLD, 13));
        bok.setPreferredSize(new Dimension(252, 40));
        bok.addActionListener(e -> confirmar());
        foot.add(lblTotal, BorderLayout.NORTH);
        foot.add(bok, BorderLayout.SOUTH);
        lat.add(hdr, BorderLayout.NORTH);
        lat.add(sc, BorderLayout.CENTER);
        lat.add(foot, BorderLayout.SOUTH);
        return lat;
    }

    private JPanel sur() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 6));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CB));
        JLabel pt = new JLabel(">");
        pt.setFont(new Font("Monospaced", Font.BOLD, 12));
        pt.setForeground(CD);
        lblEstado = new JLabel("Selecciona un item para agregarlo al pedido.");
        lblEstado.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblEstado.setForeground(CTD);
        p.add(pt);
        p.add(lblEstado);
        return p;
    }

    private int cIdx(String cod) {
        for (int i = 0; i < cSize; i++) {
            if (cCod[i].equals(cod)) {
                return i;
            }
        }
        return -1;
    }

    private void agregar(int idx) {
        String cod = mCod[idx];
        int ci = cIdx(cod);
        if (ci >= 0) {
            cCant[ci]++;
        } else {
            cCod[cSize] = cod;
            cCant[cSize] = 1;
            cSize++;
        }
        refreshCarrito();
        lblEstado.setText("\"" + mNom[idx] + "\" agregado.");
    }

    private void quitar(String cod) {
        int ci = cIdx(cod);
        if (ci < 0) {
            return;
        }
        cCant[ci]--;
        if (cCant[ci] <= 0) {
            for (int i = ci; i < cSize - 1; i++) {
                cCod[i] = cCod[i + 1];
                cCant[i] = cCant[i + 1];
            }
            cSize--;
        }
        refreshCarrito();
        lblEstado.setText("Item eliminado.");
    }

    private void confirmar() {
        if (cSize == 0) {
            JOptionPane.showMessageDialog(this, "El carrito esta vacio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        char col = (char) ('A' + asiento.getColumna());
        StringBuilder sb = new StringBuilder();
        sb.append("============================================\n");
        sb.append("         RESUMEN DE TU PEDIDO\n");
        sb.append("============================================\n");
        sb.append(String.format("  Pasajero : %s%n", nombrePasajero));
        sb.append(String.format("  Asiento  : %d-%c   Clase: %s%n", asiento.getFila() + 1, col, asiento.getClase()));
        sb.append("--------------------------------------------\n");
        double total = 0;
        for (int i = 0; i < cSize; i++) {
            int mi = mIdx(cCod[i]);
            if (mi < 0) {
                continue;
            }
            double sub = mPr[mi] * cCant[i];
            total += sub;
            sb.append(String.format("  %dx %-24s $%6.2f%n", cCant[i], mNom[mi], sub));
        }
        sb.append("============================================\n");
        sb.append(String.format("  TOTAL                          $%6.2f%n", total));
        int op = JOptionPane.showConfirmDialog(this, sb.toString(), "Confirmar pedido", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (op == JOptionPane.OK_OPTION) {
            FinanzasAerolinea.get().sumarABordo(total);
            cSize = 0;
            refreshCarrito();
            lblEstado.setText("Pedido confirmado. Buen provecho!");
            JOptionPane.showMessageDialog(this, "Pedido realizado con exito.\nSe le servira en breve.", "Pedido OK", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private int mIdx(String cod) {
        for (int i = 0; i < numM; i++) {
            if (mCod[i].equals(cod)) {
                return i;
            }
        }
        return -1;
    }

    private boolean disponible(int i) {
        TipoClase cm = mCls[i];
        if (cm == null) {
            return true;
        }
        TipoClase ca = asiento.getClase();
        if (cm == TipoClase.PRIMERA) {
            return ca == TipoClase.PRIMERA;
        }
        if (cm == TipoClase.EJECUTIVA) {
            return ca == TipoClase.PRIMERA || ca == TipoClase.EJECUTIVA;
        }
        return true;
    }

    private void refreshCarrito() {
        panelCarrito.removeAll();
        int tot = 0;
        for (int i = 0; i < cSize; i++) {
            tot += cCant[i];
        }
        lblBadge.setText(String.valueOf(tot));
        if (cSize == 0) {
            JPanel v = new JPanel(new GridBagLayout());
            v.setOpaque(false);
            GridBagConstraints gc = new GridBagConstraints();
            gc.gridy = 0;
            JLabel ico = new JLabel("[ vacio ]");
            ico.setFont(new Font("Monospaced", Font.BOLD, 13));
            ico.setForeground(CB);
            v.add(ico, gc);
            gc.gridy = 1;
            gc.insets = new Insets(6, 0, 0, 0);
            JLabel msg = new JLabel("Agrega items al pedido");
            msg.setFont(new Font("Dialog", Font.PLAIN, 11));
            msg.setForeground(CTD);
            v.add(msg, gc);
            panelCarrito.add(v);
            lblTotal.setText("TOTAL:  $0.00");
        } else {
            double total = 0;
            for (int i = 0; i < cSize; i++) {
                int mi = mIdx(cCod[i]);
                if (mi < 0) {
                    continue;
                }
                double sub = mPr[mi] * cCant[i];
                total += sub;
                JPanel fila = new JPanel(new BorderLayout(8, 0));
                fila.setOpaque(false);
                fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
                fila.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 50, 80, 100)),
                        BorderFactory.createEmptyBorder(7, 4, 7, 4)));
                JLabel badge = new JLabel(String.valueOf(cCant[i]), SwingConstants.CENTER);
                badge.setFont(new Font("Monospaced", Font.BOLD, 11));
                badge.setForeground(CD);
                badge.setBackground(new Color(40, 35, 10));
                badge.setOpaque(true);
                badge.setPreferredSize(new Dimension(26, 26));
                badge.setBorder(BorderFactory.createLineBorder(CD, 1));
                JLabel lnom = new JLabel(mNom[mi]);
                lnom.setFont(new Font("Dialog", Font.PLAIN, 12));
                lnom.setForeground(CT);
                JPanel centro = new JPanel(new BorderLayout(8, 0));
                centro.setOpaque(false);
                centro.add(badge, BorderLayout.WEST);
                centro.add(lnom, BorderLayout.CENTER);
                JPanel dr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
                dr.setOpaque(false);
                JLabel lsub = new JLabel(String.format("$%.2f", sub));
                lsub.setFont(new Font("Monospaced", Font.BOLD, 12));
                lsub.setForeground(CDC);
                final String cod = cCod[i];
                JButton bq = boton("-", CR);
                bq.setFont(new Font("Dialog", Font.BOLD, 13));
                bq.setPreferredSize(new Dimension(26, 26));
                bq.addActionListener(ev -> quitar(cod));
                dr.add(lsub);
                dr.add(bq);
                fila.add(centro, BorderLayout.CENTER);
                fila.add(dr, BorderLayout.EAST);
                panelCarrito.add(fila);
                panelCarrito.add(Box.createVerticalStrut(2));
            }
            lblTotal.setText(String.format("TOTAL:  $%.2f", total));
        }
        panelCarrito.revalidate();
        panelCarrito.repaint();
    }

    private JButton boton(String txt, Color c) {
        JButton b = new JButton(txt);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Dialog", Font.BOLD, 11));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(c.brighter());
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(c);
            }
        });
        return b;
    }
}
