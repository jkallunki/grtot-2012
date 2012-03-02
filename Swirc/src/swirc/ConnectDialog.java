
package swirc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import javax.swing.*;

/**
 * Class for showing dialog that lets user connect to a server.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class ConnectDialog extends JDialog {
    private SwircModel model;
    private ConnectDialogController controller;
    
    private JTextField serverAddress;
    private JTextField nick;
    
    /**
     * Constructor for ConnectDialog.
     * Parent needed for relative positioning.
     * @param parent Parent for dialog
     * @param model Model object of Swircs MVC-model
     */
    public ConnectDialog(JFrame parent, SwircModel model) {
        this.model = model;
        controller = new ConnectDialogController(model, this);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        
        JPanel formPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        formPane.add(new JLabel("Server address"));
        serverAddress = new JTextField(20);
        formPane.add(serverAddress);
        
        formPane.add(new JLabel("Nick"));
        nick = new JTextField(20);
        formPane.add(nick);
        
        
        cp.add(formPane, BorderLayout.CENTER);
        
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(controller);
        buttonPane.add(cancel);
        
        JButton connect = new JButton("Connect");
        connect.setActionCommand("connect");
        connect.addActionListener(controller);
        buttonPane.add(connect);
        cp.add(buttonPane, BorderLayout.SOUTH);
        
        setModalityType(ModalityType.APPLICATION_MODAL);

        setTitle("Connect a server");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Method gets servers address.
     * @return Servers address
     */
    public String getServerAddress() {
        String address = serverAddress.getText();
        return (address.length() > 0) ? address : null;
    }
    
    /**
     * Method gets users nickname.
     * @return Users nickname
     */
    public String getNick() {
        return nick.getText();
    }
}
