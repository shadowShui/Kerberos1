/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TGS.gui;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import System.Time;
import TGS.TGSServer;

/**
 *
 * @author fangyunniu
 */
public class TGSFrame extends javax.swing.JFrame {

    
    /**
     *  用于解决窗体拖动问题
     */
    private static Point origin = new Point();
    private static javax.swing.JFrame thisFrame;
    private boolean isStart;

    
    /**
     * Creates new form NewJFrame
     */
    public TGSFrame() {
        
        initComponents();
         /**
         *  居中并使得窗口可以拖动
         * @author NiuYunfang
         */
        thisFrame = this;
        TGSServer.setFrame(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextArea1.setEditable(false);
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TGS服务器");

        jLabel2.setBackground(new java.awt.Color(101, 109, 138));
        jLabel2.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(227, 234, 245));
        jLabel2.setText("     关闭");
        jLabel2.setOpaque(true);
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(101, 109, 138));
        jLabel1.setFont(new java.awt.Font("幼圆", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(227, 234, 245));
        jLabel1.setText("     开启");
        jLabel1.setOpaque(true);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel4.setText("日志记录");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                          

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {                                     
        // TODO add your handling code here:
    	if (isStart) {
    		TGSServer.stop();
    		isStart = false;
    		this.append(Time.getCurrentTime() + "\n服务器已关闭");	
        }
    }                                    

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {                                     
        // TODO add your handling code here:
    
        jLabel2.setForeground(new java.awt.Color(105, 114, 145));
        jLabel2.setBackground(new java.awt.Color(222, 221, 222));
  
    }                                    

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:

        jLabel2.setForeground(new java.awt.Color(227, 234, 245));
        jLabel2.setBackground(new java.awt.Color(101,109,138));
    }                                   

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
    
        jLabel1.setForeground(new java.awt.Color(227, 234, 245));
        jLabel1.setBackground(new java.awt.Color(101,109,138));
  
    }                                   

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {                                     
        // TODO add your handling code here:
  
        jLabel1.setForeground(new java.awt.Color(105, 114, 145));
        jLabel1.setBackground(new java.awt.Color(222, 221, 222));

    }                                    

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {                                     
        // TODO add your handling code here:
    	if (!isStart) {
    		
    		TGSServer.start();
    		isStart = true;
    		this.append(Time.getCurrentTime() + "\n服务器已启动");
        }
    }
    
    public void append(String text) {
    	
    	jTextArea1.append(text + "\n\n");
    	jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
            java.util.logging.Logger.getLogger(TGSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TGSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TGSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TGSFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TGSFrame().setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration                   
}
