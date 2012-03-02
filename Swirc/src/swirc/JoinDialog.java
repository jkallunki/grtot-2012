/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swirc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.*;

/**
 *
 * @author jask
 */
public class JoinDialog extends JDialog {
    private SwircModel model;
    private JoinDialogController controller;
    
    private JTextField channel;
    
    public JoinDialog(JFrame parent, SwircModel model) {
        this.model = model;
        controller = new JoinDialogController(model, this);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        
        JPanel formPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        formPane.add(new JLabel("Channel"));
        channel = new JTextField(20);
        formPane.add(channel);
        
        cp.add(formPane, BorderLayout.CENTER);
        
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(controller);
        buttonPane.add(cancel);
        
        JButton join = new JButton("Join");
        join.setActionCommand("join");
        join.addActionListener(controller);
        buttonPane.add(join);
        
        cp.add(buttonPane, BorderLayout.PAGE_END);
        
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        setTitle("Join Channel");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        setLocationRelativeTo(parent);
    }
    
    public String getChannel() {
        return channel.getText();
    }
    
}
