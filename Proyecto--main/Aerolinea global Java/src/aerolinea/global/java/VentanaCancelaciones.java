package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class VentanaCancelaciones extends JDialog {

    private final VectorReservas reservas;
    private JTextField txtTiquete, txtCedula;
    private DefaultTableModel modeloHistorial;

    public VentanaCancelaciones(VectorReservas reservas, JFrame padre) {
        super(padre, "Cancelar Reserva", true);
        this.reservas = reservas;
        setSize(820, 560);
        setMinimumSize(new Dimension(700, 470));
        setLocationRelativeTo(padre);
        getContentPane().setBackground(VentanaPrincipal.CF);
        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(cuerpo(), BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel header() {
        JPanel p = gradPanel(VentanaPrincipal.CR);
        p.setPreferredSize(new Dimension(0, 56));
        p.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        JLabel t = lbl("CANCELACION DE RESERVAS", 17, true, VentanaPrincipal.CT);
        JLabel s = lbl("Cancela por numero de tiquete o por cedula del pasajero", 11, false, new Color(220, 120, 120));
        p.add(stack(t, s), BorderLayout.WEST);
        return p;
    }

    private JPanel cuerpo() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(VentanaPrincipal.CF);
        p.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        p.add(panelFormularios(), BorderLayout.NORTH);
        p.add(panelHistorial(), BorderLayout.CENTER);
        return p;
    }

    private JPanel panelFormularios() {
        JPanel p = new JPanel(new GridLayout(1, 2, 12, 0));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(0, 148));
        // Por tiquete
        JPanel pt = tarjeta();
        pt.add(secLbl("CANCELAR POR TIQUETE"));
        pt.add(Box.createVerticalStrut(8));
        pt.add(dimLbl("Numero de tiquete (ej: TK-0001)"));
        pt.add(Box.createVerticalStrut(3));
        txtTiquete = campo();
        pt.add(txtTiquete);
        pt.add(Box.createVerticalStrut(10));
        JButton bt = btnF("CANCELAR TIQUETE", VentanaPrincipal.CR);
        bt.addActionListener(e -> cancelarTiquete());
        pt.add(bt);
        // Por cedula
        JPanel pc = tarjeta();
        pc.add(secLbl("CANCELAR POR PASAJERO"));
        pc.add(Box.createVerticalStrut(8));
        pc.add(dimLbl("Cedula / ID del pasajero"));
        pc.add(Box.createVerticalStrut(3));
        txtCedula = campo();
        pc.add(txtCedula);
        pc.add(Box.createVerticalStrut(10));
        JButton bc = btnF("CANCELAR TODAS", new Color(180, 80, 20));
        bc.addActionListener(e -> cancelarCedula());
        pc.add(bc);
        p.add(pt);
        p.add(pc);
        return p;
    }

    private JPanel panelHistorial() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        JLabel tit = secLbl("HISTORIAL DE CANCELACIONES");
        p.add(tit, BorderLayout.NORTH);
        String[] cols = {"Tiquete", "Pasajero", "Vuelo", "Asiento", "Pagado", "Reembolso", "Penalizacion", "Estado"};
        modeloHistorial = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tbl = new JTable(modeloHistorial);
        estilizar(tbl);
        tbl.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setForeground(VentanaPrincipal.CR);
                setBackground(s ? VentanaPrincipal.CP2 : VentanaPrincipal.CP);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Dialog", Font.BOLD, 11));
                return this;
            }
        });
        JScrollPane sc = new JScrollPane(tbl);
        sc.getViewport().setBackground(VentanaPrincipal.CP);
        sc.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1));
        p.add(sc, BorderLayout.CENTER);
        return p;
    }

    private JPanel footer() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, VentanaPrincipal.CB));
        JLabel l = new JLabel("> Platino: reembolso 100%  |  Regular/Oro: se retiene 30% como penalizacion.");
        l.setFont(new Font("Monospaced", Font.PLAIN, 11));
        l.setForeground(new Color(180, 80, 80));
        p.add(l);
        return p;
    }

    private void cancelarTiquete() {
        String num = txtTiquete.getText().trim();
        if (num.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el numero de tiquete.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Reserva r = reservas.buscarPorTiquete(num);
        if (r == null) {
            JOptionPane.showMessageDialog(this, "No se encontro reserva con tiquete: " + num, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (r.getEstado().equals(Reserva.CANCELADA)) {
            JOptionPane.showMessageDialog(this, "Esta reserva ya esta cancelada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this,
                "Cancelar reserva " + num + "?\n"
                + "Pasajero: " + r.getTiquete().getPasajero().getNombre() + "\n"
                + "Nivel: " + r.getTiquete().getPasajero().getNivel() + "\n"
                + String.format("Precio pagado: $%.2f", r.getTiquete().getPrecio()),
                "Confirmar cancelacion", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) {
            return;
        }
        r.cancelar();
        agregarHistorial(r);
        txtTiquete.setText("");
        JOptionPane.showMessageDialog(this,
                "Reserva cancelada.\n"
                + String.format("Reembolso:    $%.2f%n", r.getReembolso())
                + String.format("Penalizacion: $%.2f", r.getPenCancel()),
                "Cancelacion exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelarCedula() {
        String id = txtCedula.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa la cedula del pasajero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // collect activas
        int cnt = 0;
        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            if (r.getTiquete().getPasajero().getId().equalsIgnoreCase(id) && !r.getEstado().equals(Reserva.CANCELADA)) {
                cnt++;
            }
        }
        if (cnt == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron reservas activas para cedula: " + id, "Sin resultados", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "Cancelar " + cnt + " reserva(s) para cedula: " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) {
            return;
        }
        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            if (r.getTiquete().getPasajero().getId().equalsIgnoreCase(id) && !r.getEstado().equals(Reserva.CANCELADA)) {
                r.cancelar();
                agregarHistorial(r);
            }
        }
        txtCedula.setText("");
        JOptionPane.showMessageDialog(this, "Se cancelaron " + cnt + " reserva(s).", "Listo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void agregarHistorial(Reserva r) {
        Tiquete t = r.getTiquete();
        char col = (char) ('A' + t.getColumna());
        modeloHistorial.addRow(new Object[]{
            t.getNumero(), t.getPasajero().getNombre(), t.getVuelo().getCodigo(),
            (t.getFila() + 1) + "" + col,
            String.format("$%.2f", t.getPrecio()),
            String.format("$%.2f", r.getReembolso()),
            String.format("$%.2f", r.getPenCancel()),
            r.getEstado()
        });
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

    private JPanel tarjeta() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(VentanaPrincipal.CP);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1), BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        return p;
    }

    private JTextField campo() {
        JTextField t = new JTextField();
        t.setFont(new Font("Monospaced", Font.PLAIN, 13));
        t.setBackground(VentanaPrincipal.CP2);
        t.setForeground(VentanaPrincipal.CT);
        t.setCaretColor(VentanaPrincipal.CD);
        t.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1), BorderFactory.createEmptyBorder(5, 9, 5, 9)));
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        return t;
    }

    private JButton btnF(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Dialog", Font.BOLD, 11));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
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
