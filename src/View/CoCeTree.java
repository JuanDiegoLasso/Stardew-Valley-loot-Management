package View;

import Model.Servicio;
import Model.Servicio.HashEstructura;
import Model.Servicio.MiListaEnlazada;
import Model.Servicio.NodoCultivo;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import java.util.Iterator;

/**
 * Ejemplo que cumple con:
 * - Estructura lineal propia (MiListaEnlazada)
 * - Estructura árbol propia (Arbol)
 * - Estructura grafo propia (Grafo)
 * - Estructura hash propia (HashEstructura)
 */
public class CoCeTree extends JPanel implements KeyListener, ActionListener {

    private int arbolActual = 0;
    private int arbolAnteriorAlMultiverso = -1;
    private Servicio servicio;

    // Sustituimos List<NodoCultivo> por nuestra propia lista enlazada
    private MiListaEnlazada<NodoCultivo> nodos;

    private NodoCultivo nodoActual;
    private boolean conexionesVisibles = false;

    private JButton nextButton, backButton, multiversoButton, exitMultiversoButton;
    private JLabel treeTitleLabel;

    // Usaremos una estructura Hash propia para almacenar imágenes
    private HashEstructura<String, Image> imagesHash;

    // Para facilidad, guardamos referencias al resultado del hash
    private Image loteImagen;
    private Image loteCompletoImagen;
    private Image backgroundImage;

    public CoCeTree() {
        // Inicializamos el "servicio" que construye los grafos (listas)
        servicio = new Servicio();

        // Creamos nuestro hash de imágenes con capacidad arbitraria (p.ej. 50)
        imagesHash = new HashEstructura<>(50);

        setPreferredSize(new Dimension(600, 400));
        setBackground(new Color(255, 239, 191));

        // Cargamos imágenes en el Hash
        cargarImagenEnHash("lote.png");
        cargarImagenEnHash("lotecompleto.png");
        cargarImagenEnHash("arbol.png");

        // Extraemos las imágenes del hash (si existen)
        loteImagen         = imagesHash.get("lote.png");
        loteCompletoImagen = imagesHash.get("lotecompleto.png");
        backgroundImage    = imagesHash.get("arbol.png");

        // Por defecto cargamos el "grafo" de manualidades
        nodos = servicio.crearGrafoManualidades();
        if (!nodos.isEmpty()) {
            nodoActual = nodos.get(0);
        }

        treeTitleLabel = new JLabel("Sala de Manualidades", SwingConstants.CENTER);
        treeTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        nextButton = new JButton("Next");
        backButton = new JButton("Back");
        multiversoButton = new JButton("Multiverso");
        exitMultiversoButton = new JButton("Salir del Multiverso");

        nextButton.addActionListener(this);
        backButton.addActionListener(this);
        multiversoButton.addActionListener(this);
        exitMultiversoButton.addActionListener(this);

        nextButton.setFocusable(false);
        backButton.setFocusable(false);
        multiversoButton.setFocusable(false);
        exitMultiversoButton.setFocusable(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(treeTitleLabel, BorderLayout.CENTER);
        topPanel.add(nextButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(multiversoButton);
        bottomPanel.add(exitMultiversoButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    /**
     * Método auxiliar para cargar imágenes en el Hash.
     */
    private void cargarImagenEnHash(String fileName) {
        URL url = getClass().getResource("/" + fileName);
        if (url == null) {
            System.out.println("No se encontró " + fileName);
        } else {
            Image img = new ImageIcon(url).getImage();
            imagesHash.put(fileName, img);
        }
    }

    private void cargarArbol(int indice) {
        conexionesVisibles = false;
        switch (indice) {
            case 0:
                nodos = servicio.crearGrafoManualidades();
                treeTitleLabel.setText("Sala de Manualidades");
                setPreferredSize(new Dimension(600, 400));
                break;
            case 1:
                nodos = servicio.crearGrafoAlacena();
                treeTitleLabel.setText("Alacena");
                setPreferredSize(new Dimension(600, 400));
                break;
            case 2:
                nodos = servicio.crearGrafoPecera();
                treeTitleLabel.setText("Pecera");
                setPreferredSize(new Dimension(600, 400));
                break;
            case 3:
                nodos = servicio.crearGrafoCaldera();
                treeTitleLabel.setText("Sala de Caldera");
                setPreferredSize(new Dimension(600, 400));
                break;
            case 4:
                nodos = servicio.crearGrafoTablon();
                treeTitleLabel.setText("Tablón de Anuncios");
                setPreferredSize(new Dimension(600, 400));
                break;
            case 5:
                nodos = servicio.crearGrafoMultiverso();
                treeTitleLabel.setText("Multiverso");
                setPreferredSize(new Dimension(800, 2800));
                break;
            case 6:
                nodos = servicio.crearGrafoHuerto();
                treeTitleLabel.setText("Huerto");
                setPreferredSize(new Dimension(600, 400));
                break;
            case 7:
                nodos = servicio.crearGrafoCueva();
                treeTitleLabel.setText("Cueva");
                setPreferredSize(new Dimension(600, 400));
                break;
        }
        if (!nodos.isEmpty()) {
            nodoActual = nodos.get(0);
        }
        revalidate();
        repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
  
@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        if (conexionesVisibles && nodos != null) {
            g.setColor(Color.BLACK);
            // Dibujamos líneas de conexión entre cada nodo y su "siguiente"
            for (NodoCultivo nodo : nodos) {
                if (nodo.siguiente != null) {
                    g.drawLine(nodo.x, nodo.y, nodo.siguiente.x, nodo.siguiente.y);
                }
            }
        }
        if (nodos != null) {
            for (NodoCultivo n : nodos) {
                Image img = n.completado ? loteCompletoImagen : loteImagen;
                if (img != null) {
                    g.drawImage(img, n.x - 20, n.y - 20, 40, 40, this);
                } else {
                    g.setColor(n.completado ? Color.GREEN : Color.BLUE);
                    g.fillOval(n.x - 20, n.y - 20, 40, 40);
                }
            }
        }
        if (nodoActual != null) {
            g.setColor(Color.RED);
            g.fillOval(nodoActual.x + 15, nodoActual.y - 3, 6, 6);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        requestFocusInWindow();
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!conexionesVisibles && nodos != null) {
                // Conectamos los nodos con nuestro método propio
                servicio.conectarNodos(nodos);
                conexionesVisibles = true;
                repaint();
            } else {
                if (nodoActual != null) {
                    ItemsDialog dialog = new ItemsDialog(
                        (JFrame) SwingUtilities.getWindowAncestor(this),
                        nodoActual
                    );
                    dialog.setVisible(true);
                    repaint();
                }
            }
        }
        else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_DOWN) && conexionesVisibles) {
            if (nodoActual != null && nodoActual.siguiente != null) {
                nodoActual = nodoActual.siguiente;
                repaint();
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            if (arbolActual == 0) {
                arbolActual = 1;
                cargarArbol(arbolActual);
            } else if (arbolActual == 1) {
                arbolActual = 2;
                cargarArbol(arbolActual);
            } else if (arbolActual == 2) {
                arbolActual = 3;
                cargarArbol(arbolActual);
            } else if (arbolActual == 3) {
                arbolActual = 4;
                cargarArbol(arbolActual);
            } else if (arbolActual == 4) {
                arbolActual = 6;
                cargarArbol(arbolActual);
            } else if (arbolActual == 6) {
                arbolActual = 7;
                cargarArbol(arbolActual);
            } else if (arbolActual == 7) {
                JOptionPane.showMessageDialog(this, "Ya estás en la cueva.");
            } else if (arbolActual == 5) {
                JOptionPane.showMessageDialog(this, "Estás en el Multiverso, no hay 'Next'.");
            }
        }
        else if (e.getSource() == backButton) {
            if (arbolActual == 7) {
                arbolActual = 6;
                cargarArbol(arbolActual);
            } else if (arbolActual == 6) {
                arbolActual = 4;
                cargarArbol(arbolActual);
            } else if (arbolActual == 4) {
                arbolActual = 3;
                cargarArbol(arbolActual);
            } else if (arbolActual == 3) {
                arbolActual = 2;
                cargarArbol(arbolActual);
            } else if (arbolActual == 2) {
                arbolActual = 1;
                cargarArbol(arbolActual);
            } else if (arbolActual == 1) {
                arbolActual = 0;
                cargarArbol(arbolActual);
            } else if (arbolActual == 0) {
                JOptionPane.showMessageDialog(this, "Ya estás en la Sala de Manualidades.");
            } else if (arbolActual == 5) {
                JOptionPane.showMessageDialog(this, "Estás en el Multiverso, usa 'Salir del Multiverso'.");
            }
        }
        else if (e.getSource() == multiversoButton) {
            arbolAnteriorAlMultiverso = arbolActual;
            arbolActual = 5;
            cargarArbol(arbolActual);
        }
        else if (e.getSource() == exitMultiversoButton) {
            if (arbolActual == 5 && arbolAnteriorAlMultiverso != -1) {
                arbolActual = arbolAnteriorAlMultiverso;
                arbolAnteriorAlMultiverso = -1;
                setPreferredSize(new Dimension(600, 400));
                cargarArbol(arbolActual);
            } else {
                JOptionPane.showMessageDialog(this, "No estás en el Multiverso.");
            }
        }
        requestFocusInWindow();
    }

    /**
     * Diálogo que aparece al pulsar Enter sobre un nodo ya conectado.
     */
    class ItemsDialog extends JDialog {
        private NodoCultivo nodo;
        private JButton aceptarButton, guardarButton;
        private ItemsPanel itemsPanel;

        public ItemsDialog(JFrame parent, NodoCultivo nodo) {
            super(parent, "Completar Lote", true);
            this.nodo = nodo;
            setLayout(new BorderLayout());

            itemsPanel = new ItemsPanel(nodo.sourceTree, nodo.sourceNodeIndex);
            add(itemsPanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel();
            guardarButton = new JButton("Guardar");
            guardarButton.addActionListener(e -> dispose());
            aceptarButton = new JButton("Aceptar");
            aceptarButton.addActionListener(e -> {
                if (itemsPanel.todosLlenos()) {
                    nodo.completado = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(ItemsDialog.this,
                            "Aún faltan colocar imágenes en los cuadritos.",
                            "Atención",
                            JOptionPane.WARNING_MESSAGE);
                }
            });
            bottomPanel.add(guardarButton);
            bottomPanel.add(aceptarButton);
            add(bottomPanel, BorderLayout.SOUTH);

            setSize(450, 300);
            setLocationRelativeTo(parent);
        }

        class ItemsPanel extends JPanel implements MouseListener {
            private Image[] topImages; 
            private Rectangle[] topRects;
            private Image[] slots;
            private Rectangle[] slotRects;
            private int selectedIndex = -1;

            // Estos arrays se mantienen tal como estaban
            private final String[][] manualidadesMapping = {
                {"Wild_Horseradish.png","Daffodil.png","Leek.png","Dandelion.png"},
                {"Grape.png","Spice_Berry.png","Sweet_Pea.png"},
                {"Common_Mushroom.png","Wild_Plum.png","Hazelnut.png","Blackberry.png"},
                {"Winter_Root.png","Crystal_Fruit.png","Snow_Yam.png","Crocus.png"},
                {"Wood.png","Stone.png","Hardwood.png"},
                {"Coconut.png","Cactus_Fruit.png","Cave_Carrot.png","Red_Mushroom.png","Purple_Mushroom.png","Maple_Syrup.png","Oak_Resin.png","Pine_Tar.png","Morel.png"}
            };
            private final String[][] alacenaMapping = {
                {"Parsnip.png","Green_Bean.png","Cauliflower.png","Potato.png"},
                {"Tomato.png","Hot_Pepper.png","Blueberry.png","Melon.png"},
                {"Corn.png","Eggplant.png","Pumpkin.png","Yam.png"},
                {"Parsnip.png","Melon.png","Pumpkin.png","Corn.png"},
                {"Large_Milk_ES.png","Large_egg.png","Large_Goat_Milk_ES.png","Wool.png","Duck_Egg.png"},
                {"Truffle_Oil.png","Cloth.png","Goat_Cheese.png","Cheese.png","Honey.png","Jelly.png","Apple.png","Apricot.png","Orange.png","Peach.png","Pomegranate.png","Cherry.png"}
            };
            private final String[][] peceraMapping = {
                {"Sunfish.png","Catfish.png","Shad.png","Tiger_Trout.png"},
                {"Largemouth_Bass.png","Carp.png","Bullhead.png","Sturgeon.png"},
                {"Sardine.png","Tuna.png","Red_Snapper.png","Tilapia.png"},
                {"Walleye.png","Bream.png","Eel.png"},
                {"Lobster.png","Crayfish.png","Crab.png","Cockle.png","Museel.png","Shrimp.png","Snail.png","Periwinkle.png","Oyster.png","Clam.png"},
                {"Pufferfish.png","Ghostfish.png","Sandfish.png","Woodskip.png"}
            };
            private final String[][] calderaMapping = {
                {"Copper_Bar.png","Iron_Bar.png","Gold_Bar.png"},
                {"Quartz.png","Earth_Crystal.png","Frozen_Tear.png","Fire_Quartz.png"},
                {"Slime.png","Bat_Wing.png","Solar_Essence.png","Void_Essence.png"}
            };
            private final String[][] tablonMapping = {
                {"Maple_Syrup.png","Fiddlehead_Fern.png","Truffle.png","Poppy.png","Maki_Roll.png","Fried_Egg.png"},
                {"Red_Mushroom.png","Sea_Urchin.png","Sunflower.png","Duck_Feather.png","Aquamarine.png","Red_Cabbage.png"},
                {"Purple_Mushroom.png","Nautilus_Shell.png","Chub.png","Frozen_Geode.png"},
                {"Wheat.png","Hay.png","Apple.png"},
                {"Oak_Resin.png","Wine.png","Rabbit's_Foot.png","Pomegranate.png"}
            };
            private final String[] multiversoPaths = {
                // Un set grande para multiverso
                "Wild_Horseradish.png","Daffodil.png","Leek.png","Dandelion.png",
                "Grape.png","Spice_Berry.png","Sweet_Pea.png",
                "Common_Mushroom.png","Wild_Plum.png","Hazelnut.png","Blackberry.png",
                "Winter_Root.png","Crystal_Fruit.png","Snow_Yam.png","Crocus.png",
                "Wood.png","Stone.png","Hardwood.png",
                "Coconut.png","Cactus_Fruit.png","Cave_Carrot.png","Red_Mushroom.png","Purple_Mushroom.png",
                "Maple_Syrup.png","Oak_Resin.png","Pine_Tar.png","Morel.png",
                "Parsnip.png","Green_Bean.png","Cauliflower.png","Potato.png",
                "Tomato.png","Hot_Pepper.png","Blueberry.png","Melon.png",
                // etc...
            };

            public ItemsPanel(int sourceTree, int sourceNodeIndex) {
                setPreferredSize(new Dimension(400, 200));
                addMouseListener(this);

                String[] eligiblePaths = new String[0];
                if (sourceTree == 0 && sourceNodeIndex < manualidadesMapping.length) {
                    eligiblePaths = manualidadesMapping[sourceNodeIndex];
                } else if (sourceTree == 1 && sourceNodeIndex < alacenaMapping.length) {
                    eligiblePaths = alacenaMapping[sourceNodeIndex];
                } else if (sourceTree == 2 && sourceNodeIndex < peceraMapping.length) {
                    eligiblePaths = peceraMapping[sourceNodeIndex];
                } else if (sourceTree == 3 && sourceNodeIndex < calderaMapping.length) {
                    eligiblePaths = calderaMapping[sourceNodeIndex];
                } else if (sourceTree == 4 && sourceNodeIndex < tablonMapping.length) {
                    eligiblePaths = tablonMapping[sourceNodeIndex];
                } else if (sourceTree == 5) {
                    eligiblePaths = multiversoPaths;
                }

                int n = eligiblePaths.length;

                topImages = new Image[n];
                topRects  = new Rectangle[n];
                for (int i = 0; i < n; i++) {
                    topImages[i] = loadImage("/" + eligiblePaths[i]);
                }

                for (int i = 0; i < n; i++) {
                    int x = 20 + i * 70;
                    int y = 20;
                    topRects[i] = new Rectangle(x, y, 60, 60);
                }

                slots = new Image[n];
                slotRects = new Rectangle[n];
                for (int i = 0; i < n; i++) {
                    int x = 20 + i * 70;
                    int y = 120;
                    slotRects[i] = new Rectangle(x, y, 60, 60);
                    slots[i] = null;
                }
            }

            private Image loadImage(String path) {
                URL url = getClass().getResource(path);
                if (url == null) {
                    System.out.println("No se encontró: " + path);
                    return null;
                }
                return new ImageIcon(url).getImage();
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (int i = 0; i < topImages.length; i++) {
                    Rectangle r = topRects[i];
                    if (topImages[i] != null) {
                        g.drawImage(topImages[i], r.x, r.y, r.width, r.height, this);
                    } else {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(r.x, r.y, r.width, r.height);
                    }
                    g.setColor(Color.BLACK);
                    g.drawRect(r.x, r.y, r.width, r.height);
                }

                for (int i = 0; i < slots.length; i++) {
                    Rectangle r = slotRects[i];
                    g.setColor(Color.ORANGE);
                    g.fillRect(r.x, r.y, r.width, r.height);
                    if (slots[i] != null) {
                        g.drawImage(slots[i], r.x, r.y, r.width, r.height, this);
                    }
                    g.setColor(Color.BLACK);
                    g.drawRect(r.x, r.y, r.width, r.height);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                for (int i = 0; i < topImages.length; i++) {
                    if (topRects[i].contains(p)) {
                        selectedIndex = i;
                        System.out.println("Seleccionaste la imagen " + i);
                        return;
                    }
                }

                for (int i = 0; i < slots.length; i++) {
                    if (slotRects[i].contains(p)) {
                        if (selectedIndex != -1 && topImages[selectedIndex] != null) {
                            slots[i] = topImages[selectedIndex];
                            repaint();
                        }
                        return;
                    }
                }
            }

            public boolean todosLlenos() {
                for (int i = 0; i < slots.length; i++) {
                    if (slots[i] == null) {
                        return false;
                    }
                }
                return true;
            }

            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CoCeTree - Cada Nodo del Multiverso su Propio Origen");
        CoCeTree panel = new CoCeTree();

        JScrollPane scrollPane = new JScrollPane(panel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.setContentPane(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

        panel.requestFocusInWindow();
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

