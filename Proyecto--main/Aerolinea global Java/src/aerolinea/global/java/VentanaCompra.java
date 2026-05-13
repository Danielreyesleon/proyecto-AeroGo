package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class VentanaCompra extends JDialog {

    private final Vuelo vuelo;
    private final VectorReservas reservas;
    private JTextField txtNombre, txtCedula;
    private JComboBox<String> cmbNivel;
    private JLabel lblSel, lblPrecio;
    private JButton btnComprar;
    private JButton[][] btnAsientos;
    private int selF = -1, selC = -1;

    public VentanaCompra(Vuelo vuelo, VectorReservas reservas, JFrame padre) {
        super(padre, "Comprar Tiquete", true);
        this.vuelo = vuelo;
        this.reservas = reservas;
        setSize(900, 640);
        setMinimumSize(new Dimension(820, 580));
        setLocationRelativeTo(padre);
        setResizable(false);
        getContentPane().setBackground(VentanaPrincipal.CF);
        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(cuerpo(), BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel header() {
        JPanel p = gradPanel(VentanaPrincipal.CD);
        p.setPreferredSize(new Dimension(0, 56));
        p.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        JLabel t = lbl("COMPRA DE TIQUETE", 18, true, VentanaPrincipal.CT);
        JLabel s = lbl("Vuelo " + vuelo.getCodigo() + "  |  " + vuelo.getOrigen() + " -> " + vuelo.getDestino(), 11, false, VentanaPrincipal.CD);
        p.add(stack(t, s), BorderLayout.WEST);
        return p;
    }

    private JPanel cuerpo() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(VentanaPrincipal.CF);
        p.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        p.add(formulario(), BorderLayout.WEST);
        p.add(mapa(), BorderLayout.CENTER);
        return p;
    }

    private JPanel formulario() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(VentanaPrincipal.CP);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.CB, 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        p.setPreferredSize(new Dimension(240, 0));

        p.add(secLbl("DATOS DEL PASAJERO"));
        p.add(Box.createVerticalStrut(12));
        p.add(dimLbl("Nombre completo"));
        p.add(Box.createVerticalStrut(3));
        txtNombre = campo();
        p.add(txtNombre);
        p.add(Box.createVerticalStrut(10));
        p.add(dimLbl("Cedula / ID"));
        p.add(Box.createVerticalStrut(3));
        txtCedula = campo();
        p.add(txtCedula);
        p.add(Box.createVerticalStrut(10));
        p.add(dimLbl("Nivel de socio"));
        p.add(Box.createVerticalStrut(3));
        cmbNivel = new JComboBox<>(new String[]{"Regular", "Oro", "Platino"});
        styleCombo(cmbNivel);
        p.add(cmbNivel);
        p.add(Box.createVerticalStrut(20));

        p.add(secLbl("ASIENTO SELECCIONADO"));
        p.add(Box.createVerticalStrut(8));
        lblSel = new JLabel("Ninguno");
        lblSel.setFont(new Font("Monospaced", Font.BOLD, 14));
        lblSel.setForeground(VentanaPrincipal.CD);
        lblSel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPrecio = new JLabel("$0.00");
        lblPrecio.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblPrecio.setForeground(VentanaPrincipal.CTD);
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblSel);
        p.add(Box.createVerticalStrut(3));
        p.add(lblPrecio);
        p.add(Box.createVerticalGlue());

        btnComprar = boton("CONFIRMAR COMPRA", VentanaPrincipal.CV);
        btnComprar.setEnabled(false);
        btnComprar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnComprar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnComprar.addActionListener(e -> procesarCompra());
        p.add(btnComprar);
        return p;
    }

    private JPanel mapa() {
        JPanel wrap = new JPanel(new BorderLayout(0, 8));
        wrap.setBackground(VentanaPrincipal.CF);

        JPanel ley = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 2));
        ley.setOpaque(false);
        addLey(ley, "Primera", new Color(80, 65, 20));
        addLey(ley, "Ejecutiva", new Color(20, 50, 100));
        addLey(ley, "Economia", new Color(20, 65, 45));
        addLey(ley, "Ocupado", new Color(60, 20, 20));
        addLey(ley, "Seleccionado", VentanaPrincipal.CD);
        wrap.add(ley, BorderLayout.NORTH);

        Asiento[][] seats = vuelo.getAvion().getAsientos();
        int nf = seats.length, nc = seats[0].length;
        btnAsientos = new JButton[nf][nc];

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(VentanaPrincipal.CF);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3, 3, 3, 3);

        gc.gridy = 0;
        gc.gridx = 0;
        JLabel vl = new JLabel("");
        vl.setPreferredSize(new Dimension(30, 18));
        grid.add(vl, gc);
        for (int c = 0; c < nc; c++) {
            gc.gridx = c + 1;
            JLabel lc = new JLabel(String.valueOf((char) ('A' + c)), SwingConstants.CENTER);
            lc.setFont(new Font("Monospaced", Font.BOLD, 10));
            lc.setForeground(VentanaPrincipal.CTD);
            lc.setPreferredSize(new Dimension(44, 18));
            grid.add(lc, gc);
        }
        for (int f = 0; f < nf; f++) {
            gc.gridy = f + 1;
            gc.gridx = 0;
            JLabel lf = new JLabel(String.valueOf(f + 1), SwingConstants.CENTER);
            lf.setFont(new Font("Monospaced", Font.BOLD, 10));
            lf.setForeground(VentanaPrincipal.CTD);
            lf.setPreferredSize(new Dimension(30, 42));
            grid.add(lf, gc);
            for (int c = 0; c < nc; c++) {
                final int fi = f, ci = c;
                Asiento a = seats[f][c];
                JButton b = mkBtn(f + 1, c, a);
                if (!a.isOcupado()) {
                    b.addActionListener(e -> seleccionar(fi, ci, b));
                }
                btnAsientos[f][c] = b;
                gc.gridx = c + 1;
                grid.add(b, gc);
            }
        }
        JScrollPane sc = new JScrollPane(grid);
        sc.getViewport().setBackground(VentanaPrincipal.CF);
        sc.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1));
        wrap.add(sc, BorderLayout.CENTER);
        return wrap;
    }

    private JButton mkBtn(int fila, int col, Asiento a) {
        JButton b = new JButton(fila + "" + (char) ('A' + col)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 7, 7));
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String tx = getText();
                g2.drawString(tx, (getWidth() - fm.stringWidth(tx)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        b.setFont(new Font("Monospaced", Font.BOLD, 9));
        b.setPreferredSize(new Dimension(44, 42));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (a.isOcupado()) {
            b.setBackground(new Color(60, 20, 20));
            b.setForeground(new Color(130, 60, 60));
            b.setEnabled(false);
        } else if (a.getClase() == TipoClase.PRIMERA) {
            b.setBackground(new Color(80, 65, 20));
            b.setForeground(VentanaPrincipal.CT);
        } else if (a.getClase() == TipoClase.EJECUTIVA) {
            b.setBackground(new Color(20, 50, 100));
            b.setForeground(VentanaPrincipal.CT);
        } else {
            b.setBackground(new Color(20, 65, 45));
            b.setForeground(VentanaPrincipal.CT);
        }
        return b;
    }

    private void seleccionar(int f, int c, JButton b) {
        if (selF >= 0) {
            Asiento pv = vuelo.getAvion().getAsientos()[selF][selC];
            JButton pb = btnAsientos[selF][selC];
            if (pv.getClase() == TipoClase.PRIMERA) {
                pb.setBackground(new Color(80, 65, 20));
            } else if (pv.getClase() == TipoClase.EJECUTIVA) {
                pb.setBackground(new Color(20, 50, 100));
            } else {
                pb.setBackground(new Color(20, 65, 45));
            }
            pb.setForeground(VentanaPrincipal.CT);
        }
        selF = f;
        selC = c;
        b.setBackground(VentanaPrincipal.CD);
        b.setForeground(new Color(10, 12, 20));
        Asiento a = vuelo.getAvion().getAsientos()[f][c];
        String nv = (String) cmbNivel.getSelectedItem();
        double pr = Tarificador.calcularPrecio(vuelo, a, new Pasajero("x", "x", nv));
        lblSel.setText((f + 1) + "-" + (char) ('A' + c) + "  [" + a.getClase() + "]");
        lblPrecio.setText(String.format("Precio estimado: $%.2f", pr));
        btnComprar.setEnabled(true);
    }

    private void procesarCompra() {
        String nom = txtNombre.getText().trim(), ced = txtCedula.getText().trim();
        if (nom.isEmpty() || ced.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa nombre y cedula.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selF < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un asiento.", "Sin asiento", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nv = (String) cmbNivel.getSelectedItem();
        Reserva r = Ventas.comprar(vuelo, new Pasajero(ced, nom, nv), selF, selC);
        if (r == null) {
            JOptionPane.showMessageDialog(this, "Asiento ya ocupado, selecciona otro.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        reservas.agregar(r);
        btnAsientos[selF][selC].setBackground(new Color(60, 20, 20));
        btnAsientos[selF][selC].setForeground(new Color(130, 60, 60));
        btnAsientos[selF][selC].setEnabled(false);
        JOptionPane.showMessageDialog(this,
                "Compra exitosa!\n"
                + "Tiquete:  " + r.getTiquete().getNumero() + "\n"
                + "Pasajero: " + nom + "\n"
                + "Asiento:  " + (selF + 1) + "-" + (char) ('A' + selC) + "\n"
                + String.format("Precio:   $%.2f", r.getTiquete().getPrecio()),
                "Tiquete confirmado", JOptionPane.INFORMATION_MESSAGE);
        txtNombre.setText("");
        txtCedula.setText("");
        selF = -1;
        selC = -1;
        lblSel.setText("Ninguno");
        lblPrecio.setText("$0.00");
        btnComprar.setEnabled(false);
    }

    private JPanel footer() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, VentanaPrincipal.CB));
        JLabel l = new JLabel("> Selecciona asiento en el mapa, completa datos y confirma.");
        l.setFont(new Font("Monospaced", Font.PLAIN, 11));
        l.setForeground(VentanaPrincipal.CTD);
        p.add(l);
        return p;
    }

    // ── helpers ───────────────────────────────────────────────────
    private void addLey(JPanel p, String txt, Color bg) {
        JPanel lp = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        lp.setOpaque(false);
        JLabel sq = new JLabel("  ");
        sq.setBackground(bg);
        sq.setOpaque(true);
        sq.setPreferredSize(new Dimension(14, 14));
        JLabel lb = new JLabel(txt);
        lb.setFont(new Font("Dialog", Font.PLAIN, 11));
        lb.setForeground(VentanaPrincipal.CTD);
        lp.add(sq);
        lp.add(lb);
        p.add(lp);
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

    private JLabel lbl(String t, int sz, boolean bold, Color c) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Dialog", bold ? Font.BOLD : Font.PLAIN, sz));
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

    private JLabel dimLbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Dialog", Font.PLAIN, 11));
        l.setForeground(VentanaPrincipal.CTD);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField campo() {
        JTextField t = new JTextField();
        t.setFont(new Font("Dialog", Font.PLAIN, 13));
        t.setBackground(VentanaPrincipal.CP2);
        t.setForeground(VentanaPrincipal.CT);
        t.setCaretColor(VentanaPrincipal.CD);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.CB, 1),
                BorderFactory.createEmptyBorder(5, 9, 5, 9)));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        return t;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setBackground(VentanaPrincipal.CP2);
        cb.setForeground(VentanaPrincipal.CT);
        cb.setFont(new Font("Dialog", Font.PLAIN, 13));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton boton(String txt, Color c) {
        JButton b = new JButton(txt);
        b.setBackground(c);
        b.setForeground(new Color(10, 12, 20));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Dialog", Font.BOLD, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (b.isEnabled()) {
                    b.setBackground(c.brighter());
                }
            }

            public void mouseExited(MouseEvent e) {
                if (b.isEnabled()) {
                    b.setBackground(c);
                }
            }
        });
        return b;
    }
}
