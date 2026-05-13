package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class VentanaReservas extends JDialog {

    private final VectorReservas reservas;
    private DefaultTableModel modelo;
    private JTable tabla;

    public VentanaReservas(VectorReservas reservas, JFrame padre) {
        super(padre, "Reservas del Sistema", true);
        this.reservas = reservas;
        setSize(880, 520);
        setMinimumSize(new Dimension(740, 420));
        setLocationRelativeTo(padre);
        getContentPane().setBackground(VentanaPrincipal.CF);
        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(cuerpo(), BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
        cargar();
        setVisible(true);
    }

    private JPanel header() {
        JPanel p = gradPanel(VentanaPrincipal.CV);
        p.setPreferredSize(new Dimension(0, 56));
        p.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        JLabel t = lbl("RESERVAS DEL SISTEMA", 17, true, VentanaPrincipal.CT);
        JLabel s = lbl(reservas.size() + " registradas", 11, false, VentanaPrincipal.CV);
        p.add(stack(t, s), BorderLayout.WEST);
        JPanel bts = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0));
        bts.setOpaque(false);
        JButton bm = btn("MENU ESPECIAL", VentanaPrincipal.CAMB);
        JButton bb = btn("MENU A BORDO", VentanaPrincipal.CA);
        JButton br = btn("REFRESCAR", VentanaPrincipal.CV);
        bm.addActionListener(e -> asignarMenu());
        bb.addActionListener(e -> abrirABordo());
        br.addActionListener(e -> cargar());
        bts.add(bm);
        bts.add(bb);
        bts.add(br);
        p.add(bts, BorderLayout.EAST);
        return p;
    }

    private JPanel cuerpo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(VentanaPrincipal.CF);
        p.setBorder(BorderFactory.createEmptyBorder(12, 14, 4, 14));
        String[] cols = {"#", "Tiquete", "Pasajero", "Cedula", "Vuelo", "Asiento", "Clase", "Precio", "Menu", "Estado"};
        modelo = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        estilizar(tabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(28);
        // Estado col 9
        tabla.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Dialog", Font.BOLD, 11));
                String st = v == null ? "" : v.toString();
                if (st.equals(Reserva.ACTIVA)) {
                    setForeground(VentanaPrincipal.CV);
                } else if (st.equals(Reserva.CANCELADA)) {
                    setForeground(VentanaPrincipal.CR);
                } else {
                    setForeground(VentanaPrincipal.CD);
                }
                setBackground(s ? VentanaPrincipal.CP2 : VentanaPrincipal.CP);
                return this;
            }
        });
        // Menu col 8
        tabla.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setFont(new Font("Dialog", Font.BOLD, 11));
                String m = v == null ? "" : v.toString();
                if (m.equals("Estandar")) {
                    setForeground(VentanaPrincipal.CTD);
                } else if (m.equals("Vegetariano")) {
                    setForeground(VentanaPrincipal.CV);
                } else {
                    setForeground(VentanaPrincipal.CD);
                }
                setBackground(s ? VentanaPrincipal.CP2 : VentanaPrincipal.CP);
                return this;
            }
        });
        JScrollPane sc = new JScrollPane(tabla);
        sc.getViewport().setBackground(VentanaPrincipal.CP);
        sc.setBorder(BorderFactory.createLineBorder(VentanaPrincipal.CB, 1));
        p.add(sc, BorderLayout.CENTER);
        return p;
    }

    private JPanel footer() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, VentanaPrincipal.CB));
        JLabel l = new JLabel("> Selecciona una fila y usa los botones del header para gestionar.");
        l.setFont(new Font("Monospaced", Font.PLAIN, 11));
        l.setForeground(VentanaPrincipal.CTD);
        p.add(l);
        return p;
    }

    private void cargar() {
        modelo.setRowCount(0);
        for (int i = 0; i < reservas.size(); i++) {
            Reserva r = reservas.get(i);
            Tiquete t = r.getTiquete();
            char col = (char) ('A' + t.getColumna());
            Asiento a = t.getVuelo().getAvion().getAsientos()[t.getFila()][t.getColumna()];
            modelo.addRow(new Object[]{i + 1, t.getNumero(), t.getPasajero().getNombre(),
                t.getPasajero().getId(), t.getVuelo().getCodigo(),
                (t.getFila() + 1) + "" + col, a.getClase().toString(),
                String.format("$%.2f", t.getPrecio()), r.getMenuEspecial(), r.getEstado()});
        }
    }

    private Reserva sel() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return reservas.get(row);
    }

    private void asignarMenu() {
        Reserva r = sel();
        if (r == null) {
            return;
        }
        if (r.getEstado().equals(Reserva.CANCELADA)) {
            JOptionPane.showMessageDialog(this, "Reserva cancelada.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] ops = {"Estandar", "Vegetariano", "Kosher", "Sin Gluten"};
        String s = (String) JOptionPane.showInputDialog(this,
                "Menu para: " + r.getTiquete().getPasajero().getNombre(),
                "Menu Especial", JOptionPane.PLAIN_MESSAGE, null, ops, r.getMenuEspecial());
        if (s != null) {
            r.setMenuEspecial(s);
            cargar();
        }
    }

    private void abrirABordo() {
        Reserva r = sel();
        if (r == null) {
            return;
        }
        if (r.getEstado().equals(Reserva.CANCELADA)) {
            JOptionPane.showMessageDialog(this, "Reserva cancelada.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Tiquete t = r.getTiquete();
        Asiento a = t.getVuelo().getAvion().getAsientos()[t.getFila()][t.getColumna()];
        new VentanaMenuComida(a, t.getPasajero().getNombre(), r);
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
