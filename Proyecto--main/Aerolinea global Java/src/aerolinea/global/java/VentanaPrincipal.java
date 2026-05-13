package aerolinea.global.java;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    static final Color CF = new Color(10, 12, 20);
    static final Color CP = new Color(18, 22, 36);
    static final Color CP2 = new Color(24, 30, 50);
    static final Color CB = new Color(40, 50, 80);
    static final Color CD = new Color(212, 175, 95);
    static final Color CDC = new Color(240, 210, 130);
    static final Color CA = new Color(60, 130, 230);
    static final Color CV = new Color(50, 200, 120);
    static final Color CR = new Color(220, 70, 70);
    static final Color CT = new Color(220, 225, 240);
    static final Color CTD = new Color(110, 120, 150);
    static final Color CAMB = new Color(180, 130, 40);
    static final Color CPUR = new Color(100, 60, 200);

    // aliases usados por otras ventanas
    static final Color C_FONDO = CF;
    static final Color C_PANEL = CP;
    static final Color C_PANEL2 = CP2;
    static final Color C_BORDE = CB;
    static final Color C_DORADO = CD;
    static final Color C_DORADO_C = CDC;
    static final Color C_AZUL = CA;
    static final Color C_VERDE = CV;
    static final Color C_ROJO = CR;
    static final Color C_TEXTO = CT;
    static final Color C_TEXTO_DIM = CTD;

    private final Avion avion;
    private final Vuelo vuelo;
    private final VectorReservas reservas = new VectorReservas();

    public VentanaPrincipal() {
        avion = new Avion("AV-001", "Airbus A320", 10, 6);
        vuelo = new Vuelo("AG-345", "San Jose", "Miami", "2025-08-10", avion, 200.0);

        setTitle("AeroGo  —  Sistema de Reservas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 640);
        setMinimumSize(new Dimension(660, 580));
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(CF);
        setLayout(new BorderLayout());
        add(mkHeader(), BorderLayout.NORTH);
        add(mkMenu(), BorderLayout.CENTER);
        add(mkFooter(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel mkHeader() {
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
        p.setPreferredSize(new Dimension(0, 100));
        p.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));

        JLabel logo = new JLabel("AEROGO");
        logo.setFont(new Font("Monospaced", Font.BOLD, 14));
        logo.setForeground(CD);
        logo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CD, 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));

        JLabel titulo = new JLabel("SISTEMA DE RESERVAS AEREAS");
        titulo.setFont(new Font("Dialog", Font.BOLD, 21));
        titulo.setForeground(CT);

        JLabel sub = new JLabel("Vuelo " + vuelo.getCodigo() + "  |  " + avion
                + "  |  " + vuelo.getOrigen() + " -> " + vuelo.getDestino()
                + "  |  " + vuelo.getFecha());
        sub.setFont(new Font("Monospaced", Font.PLAIN, 10));
        sub.setForeground(CTD);

        JPanel txt = new JPanel(new GridLayout(2, 1, 0, 5));
        txt.setOpaque(false);
        txt.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        txt.add(titulo);
        txt.add(sub);

        p.add(logo, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private JPanel mkMenu() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(CF);
        p.setBorder(BorderFactory.createEmptyBorder(12, 54, 12, 54));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 0, 6, 0);
        gc.weightx = 1;
        gc.gridx = 0;

        gc.gridy = 0;
        p.add(tarjeta("COMPRAR TIQUETE", "Selecciona asiento, completa datos y confirma la reserva.", CA, e -> new VentanaCompra(vuelo, reservas, this)), gc);
        gc.gridy = 1;
        p.add(tarjeta("VER Y GESTIONAR RESERVAS", "Consulta reservas, asigna menu especial o abre compras a bordo.", CV, e -> new VentanaReservas(reservas, this)), gc);
        gc.gridy = 2;
        p.add(tarjeta("CHECK-IN Y EQUIPAJE", "Registra maletas, paga cargos y confirma abordaje.", CAMB, e -> new VentanaCheckin(reservas, this)), gc);
        gc.gridy = 3;
        p.add(tarjeta("CANCELAR RESERVA", "Cancela por tiquete o cedula y consulta reembolso.", CR, e -> new VentanaCancelaciones(reservas, this)), gc);
        gc.gridy = 4;
        p.add(tarjeta("REPORTES DEL SISTEMA", "Ocupacion por clase, manifesto de comidas y resumen financiero.", CPUR, e -> new VentanaReportes(reservas, vuelo, this)), gc);
        gc.gridy = 5;
        p.add(tarjeta("SALIR", "Cerrar la aplicacion.", new Color(55, 60, 85), e -> System.exit(0)), gc);
        return p;
    }

    private JPanel tarjeta(String titulo, String desc, Color col, ActionListener al) {
        Color[] bg = {CP};
        JPanel c = new JPanel(new BorderLayout(14, 0)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg[0]);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
            }
        };
        c.setOpaque(false);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CB, 1),
                BorderFactory.createEmptyBorder(11, 18, 11, 18)));
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel bar = new JPanel();
        bar.setBackground(col);
        bar.setPreferredSize(new Dimension(4, 0));

        JLabel lt = new JLabel(titulo);
        lt.setFont(new Font("Dialog", Font.BOLD, 14));
        lt.setForeground(CT);
        JLabel ld = new JLabel(desc);
        ld.setFont(new Font("Dialog", Font.PLAIN, 11));
        ld.setForeground(CTD);
        JPanel tx = new JPanel(new GridLayout(2, 1, 0, 2));
        tx.setOpaque(false);
        tx.add(lt);
        tx.add(ld);

        JLabel arr = new JLabel("  >");
        arr.setFont(new Font("Monospaced", Font.BOLD, 20));
        arr.setForeground(col);

        c.add(bar, BorderLayout.WEST);
        c.add(tx, BorderLayout.CENTER);
        c.add(arr, BorderLayout.EAST);
        c.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                bg[0] = CP2;
                c.repaint();
            }

            public void mouseExited(MouseEvent e) {
                bg[0] = CP;
                c.repaint();
            }

            public void mouseClicked(MouseEvent e) {
                al.actionPerformed(null);
            }
        });
        return c;
    }

    private JPanel mkFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));
        p.setBackground(new Color(8, 10, 22));
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, CB));
        JLabel l = new JLabel("AeroGo  //  v2.0  |  Compra  Reservas  Check-in  Cancelaciones  Reportes");
        l.setFont(new Font("Monospaced", Font.PLAIN, 10));
        l.setForeground(CTD);
        p.add(l);
        return p;
    }
}
