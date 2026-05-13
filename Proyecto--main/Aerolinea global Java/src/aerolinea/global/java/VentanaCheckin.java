package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class VentanaCheckin extends JDialog {

    private final VectorReservas reservas;
    private JTextField txtBusqueda, txtPeso;
    private JLabel lblInfo;
    private JButton btnAgregarMaleta, btnCheckin, btnAbordar;
    private DefaultTableModel modeloMaletas;
    private Reserva actual = null;

    public VentanaCheckin(VectorReservas reservas, JFrame padre) {
        super(padre, "Check-in y Equipaje", true);
        this.reservas = reservas;
        setSize(740, 560);
        setMinimumSize(new Dimension(640, 480));
        setLocationRelativeTo(padre);
        getContentPane().setBackground(VentanaPrincipal.CF);
        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(cuerpo(), BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel header() {
        JPanel p = gradPanel(VentanaPrincipal.CAMB);
        p.setPreferredSize(new Dimension(0, 56));
        p.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        JLabel t = lbl("CHECK-IN Y CONTROL DE EQUIPAJE", 17, true, VentanaPrincipal.CT);
        JLabel s = lbl("Busca la reserva por numero de tiquete", 11, false, VentanaPrincipal.CAMB);
        p.add(stack(t, s), BorderLayout.WEST);
        return p;
    }

    private JPanel cuerpo() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(VentanaPrincipal.CF);
        p.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        p.add(panelBusqueda(), BorderLayout.NORTH);
        p.add(panelEquipaje(), BorderLayout.CENTER);
        return p;
    }

    private JPanel panelBusqueda() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(VentanaPrincipal.CP);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1), BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        JLabel sec = secLbl("BUSCAR RESERVA");
        p.add(sec);
        p.add(Box.createVerticalStrut(8));
        JPanel fila = new JPanel(new BorderLayout(8, 0));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        JLabel etiq = new JLabel("No. Tiquete: ");
        etiq.setFont(new Font("Dialog", Font.PLAIN, 12));
        etiq.setForeground(VentanaPrincipal.CTD);
        txtBusqueda = campo();
        JButton bbuscar = btn("BUSCAR", VentanaPrincipal.CA);
        bbuscar.addActionListener(e -> buscar());
        fila.add(etiq, BorderLayout.WEST);
        fila.add(txtBusqueda, BorderLayout.CENTER);
        fila.add(bbuscar, BorderLayout.EAST);
        p.add(fila);
        p.add(Box.createVerticalStrut(8));
        lblInfo = new JLabel("Ingresa el numero de tiquete para comenzar.");
        lblInfo.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblInfo.setForeground(VentanaPrincipal.CTD);
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblInfo);
        return p;
    }

    private JPanel panelEquipaje() {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setBackground(VentanaPrincipal.CF);
        // Panel izquierdo — acciones
        JPanel iz = new JPanel();
        iz.setLayout(new BoxLayout(iz, BoxLayout.Y_AXIS));
        iz.setBackground(VentanaPrincipal.CP);
        iz.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1), BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        iz.setPreferredSize(new Dimension(220, 0));
        iz.add(secLbl("AGREGAR MALETA"));
        iz.add(Box.createVerticalStrut(8));
        iz.add(dimLbl("Peso (kg)"));
        iz.add(Box.createVerticalStrut(3));
        txtPeso = campo();
        iz.add(txtPeso);
        iz.add(Box.createVerticalStrut(10));
        btnAgregarMaleta = btnAcc("AGREGAR MALETA", VentanaPrincipal.CAMB);
        btnAgregarMaleta.setEnabled(false);
        btnAgregarMaleta.addActionListener(e -> agregarMaleta());
        iz.add(btnAgregarMaleta);
        iz.add(Box.createVerticalStrut(16));
        btnCheckin = btnAcc("REALIZAR CHECK-IN", VentanaPrincipal.CV);
        btnCheckin.setEnabled(false);
        btnCheckin.addActionListener(e -> realizarCheckin());
        iz.add(btnCheckin);
        iz.add(Box.createVerticalStrut(8));
        btnAbordar = btnAcc("CONFIRMAR ABORDAJE", VentanaPrincipal.CD);
        btnAbordar.setEnabled(false);
        btnAbordar.setForeground(new Color(10, 12, 20));
        btnAbordar.addActionListener(e -> confirmarAbordaje());
        iz.add(btnAbordar);
        // Panel derecho — tabla maletas
        JPanel dr = new JPanel(new BorderLayout(0, 8));
        dr.setOpaque(false);
        JLabel tit = secLbl("MALETAS REGISTRADAS");
        dr.add(tit, BorderLayout.NORTH);
        String[] cols = {"#", "Peso (kg)", "Estado", "Cargo extra"};
        modeloMaletas = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tbl = new JTable(modeloMaletas);
        estilizar(tbl);
        tbl.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                String tx = v == null ? "" : v.toString();
                setForeground(tx.equals("OK") ? VentanaPrincipal.CV : VentanaPrincipal.CR);
                setBackground(s ? VentanaPrincipal.CP2 : VentanaPrincipal.CP);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Dialog", Font.BOLD, 11));
                return this;
            }
        });
        JScrollPane sc = new JScrollPane(tbl);
        sc.getViewport().setBackground(VentanaPrincipal.CP);
        sc.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1));
        dr.add(sc, BorderLayout.CENTER);
        p.add(iz, BorderLayout.WEST);
        p.add(dr, BorderLayout.CENTER);
        return p;
    }

    private JPanel footer() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, VentanaPrincipal.CB));
        JLabel l = new JLabel("> Peso max por maleta: 23 kg  |  Exceso: $15.00 por kg adicional");
        l.setFont(new Font("Monospaced", Font.PLAIN, 11));
        l.setForeground(VentanaPrincipal.CTD);
        p.add(l);
        return p;
    }

    private void buscar() {
        String num = txtBusqueda.getText().trim();
        if (num.isEmpty()) {
            return;
        }
        Reserva r = reservas.buscarPorTiquete(num);
        if (r == null) {
            lblInfo.setForeground(VentanaPrincipal.CR);
            lblInfo.setText("No se encontro reserva: " + num);
            actual = null;
            btnAgregarMaleta.setEnabled(false);
            btnCheckin.setEnabled(false);
            btnAbordar.setEnabled(false);
            return;
        }
        if (r.getEstado().equals(Reserva.CANCELADA)) {
            lblInfo.setForeground(VentanaPrincipal.CR);
            lblInfo.setText("Reserva cancelada.");
            actual = null;
            btnAgregarMaleta.setEnabled(false);
            btnCheckin.setEnabled(false);
            btnAbordar.setEnabled(false);
            return;
        }
        actual = r;
        Tiquete t = r.getTiquete();
        lblInfo.setForeground(VentanaPrincipal.CV);
        lblInfo.setText("Pasajero: " + t.getPasajero().getNombre() + "  |  Asiento: " + (t.getFila() + 1) + (char) ('A' + t.getColumna()) + "  |  Estado: " + r.getEstado());
        modeloMaletas.setRowCount(0);
        for (int i = 0; i < r.getNumMaletas(); i++) {
            double kg = r.getPesoMaleta(i);
            double extra = kg > 23 ? (kg - 23) * 15 : 0;
            modeloMaletas.addRow(new Object[]{i + 1, String.format("%.1f kg", kg), kg > 23 ? "EXCESO" : "OK", extra > 0 ? String.format("$%.2f", extra) : "-"});
        }
        boolean activa = r.getEstado().equals(Reserva.ACTIVA), chk = r.getEstado().equals(Reserva.CHECKIN);
        btnAgregarMaleta.setEnabled(activa);
        btnCheckin.setEnabled(activa);
        btnAbordar.setEnabled(chk);
    }

    private void agregarMaleta() {
        if (actual == null) {
            return;
        }
        double kg;
        try {
            kg = Double.parseDouble(txtPeso.getText().trim().replace(",", "."));
            if (kg <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un peso valido (ej: 18.5).", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        actual.agregarMaleta(kg);
        double extra = kg > 23 ? (kg - 23) * 15 : 0;
        modeloMaletas.addRow(new Object[]{actual.getNumMaletas(), String.format("%.1f kg", kg), kg > 23 ? "EXCESO" : "OK", extra > 0 ? String.format("$%.2f", extra) : "-"});
        txtPeso.setText("");
        if (extra > 0) {
            JOptionPane.showMessageDialog(this, String.format("Maleta %.1f kg registrada.\nExceso: %.1f kg  =>  Cargo: $%.2f", kg, kg - 23, extra), "Cargo por exceso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void realizarCheckin() {
        if (actual == null) {
            return;
        }
        double pen = actual.getPenEquipaje();
        if (pen > 0) {
            int op = JOptionPane.showConfirmDialog(this, String.format("Hay cargos pendientes de equipaje: $%.2f\nConfirmar pago y realizar check-in?", pen), "Cargo pendiente", JOptionPane.YES_NO_OPTION);
            if (op != JOptionPane.YES_OPTION) {
                return;
            }
        }
        actual.realizarCheckin();
        lblInfo.setForeground(VentanaPrincipal.CD);
        lblInfo.setText("Check-in realizado: " + actual.getTiquete().getPasajero().getNombre());
        btnAgregarMaleta.setEnabled(false);
        btnCheckin.setEnabled(false);
        btnAbordar.setEnabled(true);
        JOptionPane.showMessageDialog(this, "Check-in realizado exitosamente.\nProceda al area de abordaje.", "Check-in OK", JOptionPane.INFORMATION_MESSAGE);
    }

    private void confirmarAbordaje() {
        if (actual == null) {
            return;
        }
        actual.confirmarAbordaje();
        lblInfo.setForeground(VentanaPrincipal.CD);
        lblInfo.setText("Abordado: " + actual.getTiquete().getPasajero().getNombre());
        btnAbordar.setEnabled(false);
        JOptionPane.showMessageDialog(this, "Pasajero abordado exitosamente.\nBuen viaje!", "Abordaje OK", JOptionPane.INFORMATION_MESSAGE);
    }

    // helpers
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
        t.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1), BorderFactory.createEmptyBorder(5, 9, 5, 9)));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        return t;
    }

    private JButton btn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Dialog", Font.BOLD, 11));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(c.darker());
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(c);
            }
        });
        return b;
    }

    private JButton btnAcc(String t, Color c) {
        JButton b = btn(t, c);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return b;
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
