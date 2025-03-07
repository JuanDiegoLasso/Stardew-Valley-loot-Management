/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.modeloia;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class ItemSearch extends javax.swing.JFrame {
    private JLabel resultLabel;
    private ItemClassifier classifier;

    public ItemSearch() {
        initComponents();
        initCustomComponents(); 
        initClassifier();    
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    
    private void initCustomComponents() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Arrastra y suelta un archivo"));

        resultLabel = new JLabel("Arrastra un archivo de imagen aquí", JLabel.CENTER);
        panel.add(resultLabel, BorderLayout.SOUTH);
        
     
        new DropTarget(panel, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = dtde.getTransferable();
                try {
                    @SuppressWarnings("unchecked")
                    List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (!droppedFiles.isEmpty()) {
                        File file = droppedFiles.get(0);
                        resultLabel.setText("Procesando: " + file.getName());
                        new Thread(() -> {
                            try {
                                ItemClassifier.PredictionResult result = classifier.predictItem(file.getAbsolutePath());
                                SwingUtilities.invokeLater(() -> {
                                    resultLabel.setText("<html>Predicción: " + result.label
                                            + "<br>Distancia: " + String.format("%.4f", result.distance) + "</html>");
                                });
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                SwingUtilities.invokeLater(() -> {
                                    resultLabel.setText("Error: " + ex.getMessage());
                                });
                            }
                        }).start();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    resultLabel.setText("Error al leer el archivo.");
                }
            }
        }, true);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
    }  
    
    private void initClassifier() {
        new Thread(() -> {
            try {
                classifier = new ItemClassifier();
                String itemsFolder = "C:\\Users\\USUARIO\\Desktop\\STARDEW\\Stardew-Valley-loot-Management-modeloIA2\\src\\main\\resources\\ITEMS";
                classifier.buildDatabase(itemsFolder);
                SwingUtilities.invokeLater(() -> {
                    resultLabel.setText("Clasificador listo. Arrastra un archivo para probar.");
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    resultLabel.setText("Error al inicializar clasificador: " + e.getMessage());
                });
            }
        }).start();
    } 
    public JPanel getContentPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    // Agregar los componentes del frame al panel
    for (Component comp : this.getContentPane().getComponents()) {
        panel.add(comp);
    }

    return panel;
}
    
    
    
    
    
    
    
    
    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ItemSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ItemSearch().setVisible(true);
            }
        });
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
