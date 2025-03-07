package com.mycompany.modeloia;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Chestroom {
    private String nombre;
    private int cantidad;
    private Color color;

    public Chestroom(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        Random random = new Random();
        this.color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return nombre + " x" + cantidad;
    }
}

class InventarioPrincipalGUI extends JPanel {
    private static final int TAMANO = 27;
    private static final int MAIN_CELL_SIZE = 60;
    private static final int GAP = 8;

    private List<Chestroom> espacios;
    private JPanel gridPanel;
    private JButton cerrar;
    private JButton seleccionado = null;
    private JButton abrirInventario;
    private JLabel statusLabel;

    public InventarioPrincipalGUI(JFrame parentFrame, JButton abrirInventario) {
        this.abrirInventario = abrirInventario;
        int gridWidth = 9 * MAIN_CELL_SIZE + (9 - 1) * GAP;
        int gridHeight = 3 * MAIN_CELL_SIZE + (3 - 1) * GAP;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(gridWidth, gridHeight + 80));
        setMaximumSize(new Dimension(gridWidth, gridHeight + 80));
        setBackground(new Color(139, 69, 19));
        setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 5, true));

        espacios = new ArrayList<>(TAMANO);
        for (int i = 0; i < TAMANO; i++) {
            espacios.add(null);
        }

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 9, GAP, GAP));
        gridPanel.setBackground(new Color(205, 133, 63));
        gridPanel.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
        actualizarInventario();
        gridPanel.setPreferredSize(new Dimension(gridWidth, gridHeight));
        gridPanel.setMaximumSize(new Dimension(gridWidth, gridHeight));

        JPanel gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridWrapper.setPreferredSize(new Dimension(gridWidth, gridHeight));
        gridWrapper.setMaximumSize(new Dimension(gridWidth, gridHeight));
        gridWrapper.setBackground(new Color(205, 133, 63));
        gridWrapper.add(gridPanel);

        cerrar = new JButton("X");
        cerrar.setBackground(new Color(255, 99, 71));
        cerrar.setBorder(BorderFactory.createLineBorder(new Color(139, 0, 0), 3, true));
        cerrar.setFocusPainted(false);
        cerrar.setPreferredSize(new Dimension(20, 20));
        cerrar.addActionListener(e -> {
            setVisible(false);
            abrirInventario.setEnabled(true);
        });

        JLabel titulo = new JLabel("Inventario Principal");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setHorizontalAlignment(SwingConstants.LEFT);
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titulo, BorderLayout.WEST);
        topPanel.add(cerrar, BorderLayout.EAST);

        statusLabel = new JLabel("");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(255, 224, 189));
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel botonesAccionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton botarItem = new JButton("Botar item");
        botarItem.setBackground(new Color(210, 105, 30));
        botarItem.setForeground(Color.WHITE);
        botarItem.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
        botarItem.addActionListener(e -> {
            BarraAccesoRapido barra = (BarraAccesoRapido) parentFrame;
            barra.botarItemSeleccionado();
        });
        JButton intercambiarPosicion = new JButton("Intercambiar posición");
        intercambiarPosicion.setBackground(new Color(160, 82, 45));
        intercambiarPosicion.setForeground(Color.WHITE);
        intercambiarPosicion.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
        intercambiarPosicion.addActionListener(e -> {
            BarraAccesoRapido barra = (BarraAccesoRapido) parentFrame;
            barra.intercambiarObjetos();
        });
        botonesAccionPanel.add(botarItem);
        botonesAccionPanel.add(intercambiarPosicion);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        bottomPanel.setPreferredSize(new Dimension(gridWidth, 40));
        bottomPanel.setMaximumSize(new Dimension(gridWidth, 40));
        bottomPanel.add(statusLabel);
        bottomPanel.add(botonesAccionPanel);

        add(topPanel, BorderLayout.NORTH);
        add(gridWrapper, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void actualizarInventario() {
        gridPanel.removeAll();
        for (int i = 0; i < TAMANO; i++) {
            final int index = i;
            JButton boton = new JButton(espacios.get(index) == null ? " " : espacios.get(index).toString());
            boton.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
            boton.setBackground(espacios.get(index) == null ? new Color(222, 184, 135) : espacios.get(index).getColor());
            boton.setPreferredSize(new Dimension(MAIN_CELL_SIZE, MAIN_CELL_SIZE));
            boton.setMaximumSize(new Dimension(MAIN_CELL_SIZE, MAIN_CELL_SIZE));
            boton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    seleccionarCasilla(boton);
                }
            });
            gridPanel.add(boton);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void seleccionarCasilla(JButton boton) {
        if (seleccionado == boton) {
            boton.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
            seleccionado = null;
            statusLabel.setText("");
        } else {
            if (seleccionado != null) {
                seleccionado.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
            }
            boton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3, true));
            seleccionado = boton;
            statusLabel.setText("");
        }
    }

    public void eliminarItemSeleccionado() {
        if (seleccionado != null) {
            int index = gridPanel.getComponentZOrder(seleccionado);
            espacios.set(index, null);
            actualizarInventario();
        }
    }

    public List<Chestroom> getEspacios() {
        return espacios;
    }

    public JButton getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(JButton seleccionado) {
        this.seleccionado = seleccionado;
    }

    public JPanel getGridPanel() {
        return gridPanel;
    }
}

class BarraAccesoRapido extends JFrame {
    private static final int TAMANO = 9;
    private static final int QUICK_CELL_SIZE = 60;
    private JButton[] botones;
    private InventarioPrincipalGUI inventarioGUI;
    private JPanel inventarioPanel;
    private JButton seleccionado = null;
    private JButton abrirInventario;

    public BarraAccesoRapido() {
        setTitle("Inventario");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(139, 69, 19));

        abrirInventario = new JButton("...");
        abrirInventario.setPreferredSize(new Dimension(40, 40));
        abrirInventario.setMaximumSize(new Dimension(40, 40));
        abrirInventario.setBackground(new Color(222, 184, 135));
        abrirInventario.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
        abrirInventario.addActionListener(e -> {
            inventarioPanel.setVisible(true);
            abrirInventario.setEnabled(false);
            inventarioGUI.setVisible(true);
        });

        inventarioGUI = new InventarioPrincipalGUI(this, abrirInventario);
        inventarioPanel = new JPanel(new BorderLayout());
        inventarioPanel.add(inventarioGUI, BorderLayout.CENTER);
        inventarioPanel.setVisible(false);
        add(inventarioPanel, BorderLayout.NORTH);

        JPanel quickAccessContainer = new JPanel();
        quickAccessContainer.setLayout(new BoxLayout(quickAccessContainer, BoxLayout.Y_AXIS));
        quickAccessContainer.setBackground(new Color(205, 133, 63));
        quickAccessContainer.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 5, true));
        quickAccessContainer.setPreferredSize(new Dimension(600, 140));
        quickAccessContainer.setMaximumSize(new Dimension(600, 140));

        JPanel topBarPanel = new JPanel(new BorderLayout());
        topBarPanel.setOpaque(false);
        topBarPanel.setPreferredSize(new Dimension(600, 40));
        topBarPanel.setMaximumSize(new Dimension(600, 40));
        JLabel titulo = new JLabel("Barra de Acceso Rápido");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topBarPanel.add(titulo, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        JButton agregarInventario = new JButton("Agregar al inventario");
        agregarInventario.setBackground(new Color(222, 184, 135));
        agregarInventario.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
        agregarInventario.setPreferredSize(new Dimension(150, 40));
        agregarInventario.addActionListener(e -> agregarItem());
        rightPanel.add(agregarInventario);
        rightPanel.add(abrirInventario);
        topBarPanel.add(rightPanel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(600, 100));
        bottomPanel.setMaximumSize(new Dimension(600, 100));
        botones = new JButton[TAMANO];
        for (int i = 0; i < TAMANO; i++) {
            final int index = i;
            botones[index] = new JButton(" ");
            botones[index].setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
            botones[index].setBackground(new Color(222, 184, 135));
            botones[index].setPreferredSize(new Dimension(QUICK_CELL_SIZE, QUICK_CELL_SIZE));
            botones[index].setMaximumSize(new Dimension(QUICK_CELL_SIZE, QUICK_CELL_SIZE));
            botones[index].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    seleccionarCasilla(botones[index]);
                }
            });
            bottomPanel.add(botones[index]);
        }

        quickAccessContainer.add(topBarPanel);
        quickAccessContainer.add(bottomPanel);

        add(quickAccessContainer, BorderLayout.SOUTH);
    }

    private void seleccionarCasilla(JButton boton) {
        if (seleccionado == boton) {
            boton.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
            seleccionado = null;
        } else {
            if (seleccionado != null) {
                seleccionado.setBorder(BorderFactory.createLineBorder(new Color(101, 67, 33), 3, true));
            }
            boton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3, true));
            seleccionado = boton;
        }
    }

    private void agregarItem() {
        for (int i = 0; i < TAMANO; i++) {
            if (botones[i].getText().trim().isEmpty()) {
                Chestroom item = new Chestroom("Item " + (i + 1), 1);
                botones[i].setText(item.toString());
                botones[i].setBackground(item.getColor());
                botones[i].putClientProperty("item", item);
                return;
            }
        }

        List<Chestroom> espacios = inventarioGUI.getEspacios();
        for (int i = 0; i < espacios.size(); i++) {
            if (espacios.get(i) == null) {
                Chestroom item = new Chestroom("Item " + (i + 1), 1);
                espacios.set(i, item);
                inventarioGUI.actualizarInventario();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "¡No hay más espacio en los inventarios!", "Inventario lleno", JOptionPane.WARNING_MESSAGE);
    }

    public void botarItemSeleccionado() {
        if (seleccionado != null) {
            for (int i = 0; i < botones.length; i++) {
                if (botones[i] == seleccionado) {
                    botones[i].setText(" ");
                    botones[i].setBackground(new Color(222, 184, 135));
                    botones[i].putClientProperty("item", null);
                    seleccionado = null;
                    return;
                }
            }
        }

        if (inventarioGUI.getSeleccionado() != null) {
            inventarioGUI.eliminarItemSeleccionado();
        }
    }

    public void intercambiarObjetos() {
        if (seleccionado != null && inventarioGUI.getSeleccionado() != null) {
            JButton botonSeleccionadoBarra = seleccionado;
            JButton botonSeleccionadoInventario = inventarioGUI.getSeleccionado();

            Chestroom itemBarra = (Chestroom) botonSeleccionadoBarra.getClientProperty("item");
            Chestroom itemInventario = inventarioGUI.getEspacios().get(inventarioGUI.getGridPanel().getComponentZOrder(botonSeleccionadoInventario));

            if (itemBarra == null && itemInventario == null) {
                JOptionPane.showMessageDialog(this, "No se puede intercambiar objetos vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            botonSeleccionadoBarra.putClientProperty("item", itemInventario);
            botonSeleccionadoBarra.setText(itemInventario != null ? itemInventario.toString() : " ");
            botonSeleccionadoBarra.setBackground(itemInventario != null ? itemInventario.getColor() : new Color(222, 184, 135));

            inventarioGUI.getEspacios().set(inventarioGUI.getGridPanel().getComponentZOrder(botonSeleccionadoInventario), itemBarra);

            inventarioGUI.actualizarInventario();
            seleccionado = null;
            inventarioGUI.setSeleccionado(null);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione dos casillas para intercambiar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BarraAccesoRapido barra = new BarraAccesoRapido();
            barra.setVisible(true);
        });
    }
}