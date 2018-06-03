/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.gui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import Client.Client;

/**
 *
 * @author fangyunniu
 */
public class RegistPanel extends javax.swing.JPanel {

    private static Client client;
	/**
     * Creates new form RegistPanel
     */
    public RegistPanel(Client client) {
    	
    	this.client = client;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jPasswordField2 = new javax.swing.JPasswordField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(241, 239, 241));
        setPreferredSize(new java.awt.Dimension(540, 320));

        jLabel1.setFont(new java.awt.Font("幼圆", 1, 24)); // NOI18N
        jLabel1.setText("密码");

        jPasswordField1.setBackground(new java.awt.Color(240, 240, 240));
        jPasswordField1.setFont(new java.awt.Font("宋体", 0, 24)); // NOI18N
        jPasswordField1.setBorder(null);
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });
        jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPasswordField1KeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("幼圆", 1, 24)); // NOI18N
        jLabel3.setText("用户名");

        jTextField1.setBackground(new java.awt.Color(240, 240, 240));
        jTextField1.setFont(new java.awt.Font("宋体", 0, 22)); // NOI18N
        jTextField1.setBorder(null);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jPasswordField2.setBackground(new java.awt.Color(240, 240, 240));
        jPasswordField2.setFont(new java.awt.Font("宋体", 0, 24)); // NOI18N
        jPasswordField2.setBorder(null);
        jPasswordField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField2ActionPerformed(evt);
            }
        });
        jPasswordField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPasswordField2KeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("幼圆", 1, 24)); // NOI18N
        jLabel2.setText("确认密码");

        jLabel7.setBackground(new java.awt.Color(101, 109, 138));
        jLabel7.setFont(new java.awt.Font("幼圆", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(227, 234, 245));
        jLabel7.setText("  注册");
        jLabel7.setOpaque(true);
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel1)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator1)
                            .addComponent(jTextField1)
                            .addComponent(jSeparator2)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator3)
                            .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(108, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(214, 214, 214))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(67, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2))
                .addGap(41, 41, 41)
                .addComponent(jLabel7)
                .addGap(38, 38, 38))
        );
    }// </editor-fold>                        

    protected void jLabel7MouseClicked(MouseEvent evt) {
		// TODO Auto-generated method stub
    	regist();
	}

	private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void jPasswordField1KeyPressed(java.awt.event.KeyEvent evt) {                                           
        // TODO add your handling code here:
     
    }                                          

    private void jPasswordField1KeyReleased(java.awt.event.KeyEvent evt) {                                            
        // TODO add your handling code here:
 
    }                                           

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void jPasswordField2ActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void jPasswordField2KeyPressed(java.awt.event.KeyEvent evt) {                                           
        // TODO add your handling code here:
        if(evt.getKeyChar() == KeyEvent.VK_ENTER ) {
            jLabel7.setForeground(new java.awt.Color(105, 114, 145));
            jLabel7.setBackground(new java.awt.Color(222, 221, 222));
        }
    }                                          

    private void jPasswordField2KeyReleased(java.awt.event.KeyEvent evt) {                                            
        // TODO add your handling code here:
        if(evt.getKeyChar() == KeyEvent.VK_ENTER ) {
            jLabel7.setForeground(new java.awt.Color(227, 234, 245));
            jLabel7.setBackground(new java.awt.Color(101,109,138));
            regist();
        }
    }                                           

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {                                     
        // TODO add your handling code here:
        jLabel7.setForeground(new java.awt.Color(105, 114, 145));
        jLabel7.setBackground(new java.awt.Color(222, 221, 222));
    }                                    

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {                                    
        // TODO add your handling code here:
        jLabel7.setForeground(new java.awt.Color(227, 234, 245));
        jLabel7.setBackground(new java.awt.Color(101,109,138));
    }                                   
    
    @SuppressWarnings("deprecation")
	private void regist() {
    	
    	short id;
    	String psw1;
    	String psw2;
    	try {
    		id = Short.parseShort(jTextField1.getText());
    		psw1 = jPasswordField1.getText();
    		psw2 = jPasswordField2.getText();
    	} catch (Exception e) {
    		JOptionPane.showMessageDialog(null,  "用户名或密码输入有误！", "出错啦", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
    	if (!psw1.equals(psw2)) {
    		JOptionPane.showMessageDialog(null,  "两次密码输入不一致！", "出错啦", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
    	
    	String res = client.regist(id, psw1);
    	
    	if (res.equals("regist successful")) {
    		JOptionPane.showMessageDialog(null, "注册成功！");
    	} else if(res.equals("regist failed")) {
    		JOptionPane.showMessageDialog(null, "注册失败，用户名可能已经存在！");
    	}
   
    }


    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}
