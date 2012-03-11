package swirc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.*;

/**
 * Class for dialog that prompts channel user wants to join.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class JoinDialog extends JDialog {
    private SwircModel model;
    private JoinDialogController controller;
    
    private JTextField channel;
    private JComboBox servers;
    private boolean confirmed;
    
    /**
     * Constructer.
     * @param parent JFrame that called dialog
     * @param model SwircModel of this.
     */
    public JoinDialog(JFrame parent, SwircModel model) {
        this.model = model;
        controller = new JoinDialogController(model, this);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        this.confirmed = false;
        
        JPanel formPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        servers = new JComboBox(this.model.getConnectedServers());
        formPane.add(new JLabel("Servers"));
        formPane.add(servers);
        
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
    
    /**
     * Returns channel given in channel-JTextField.
     * @return Channel in channel-JTextField
     */
    public String getChannel() {
        return channel.getText();
    }
    
    public int getServer() {
        return servers.getSelectedIndex();
    }

    public void setConfirmed(boolean b) {
        this.confirmed = b;
    }
    
    public boolean isConfirmed() {
        return this.confirmed;
    }
    
}
