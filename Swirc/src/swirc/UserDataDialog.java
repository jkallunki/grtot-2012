package swirc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;

/**
 * Class for dialog that prompts user info.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class UserDataDialog extends JDialog {
    private SwircModel model;
    private UserDataDialogController controller;
    
    private JTextField nick, secondaryNick, username, realName;
    private boolean confirmed;
    
    /**
     * Constructer.
     * @param parent JFrame that called dialog
     * @param model SwircModel of this.
     */
    public UserDataDialog(JFrame parent, SwircModel model) {
        this.model = model;
        controller = new UserDataDialogController(model, this);
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        this.confirmed = false;
        
        JPanel formPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JPanel inputPane = new JPanel(new GridLayout(8, 1, 5, 5));
        inputPane.add(new JLabel("Nick"));
        nick = new JTextField(20);
        inputPane.add(nick);
        
        inputPane.add(new JLabel("Secondary nick"));
        secondaryNick = new JTextField(20);
        inputPane.add(secondaryNick);
        
        inputPane.add(new JLabel("Username"));
        username = new JTextField(20);
        inputPane.add(username);
        
        inputPane.add(new JLabel("Real name"));
        realName = new JTextField(20);
        inputPane.add(realName);
        
        formPane.add(inputPane);
        
        cp.add(formPane, BorderLayout.CENTER);
        
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton cancel = new JButton("Cancel");
        cancel.setActionCommand("cancel");
        cancel.addActionListener(controller);
        buttonPane.add(cancel);
        
        JButton saveUser = new JButton("Save Data");
        saveUser.setActionCommand("saveUser");
        saveUser.addActionListener(controller);
        buttonPane.add(saveUser);
        
        cp.add(buttonPane, BorderLayout.PAGE_END);
        
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        setTitle("Set User Info");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        setLocationRelativeTo(parent);
    }
    /**
     * Sets confirmed to given boolean value
     * @param b Is confirmed
     */
    public void setConfirmed(boolean b) {
        this.confirmed = b;
    }
    
    /**
     * Returns the value of confirmed
     * @return Value of confirmed
     */
    public boolean isConfirmed() {
        return this.confirmed;
    }

    /**
     * Returns nick of the user
     * @return Nickname of the user
     */
    public String getNick() {
        return nick.getText();
    }

    /**
     * Returns the secondary nickname
     * @return Secondary nickname
     */
    public String getSecondaryNick() {
        return secondaryNick.getText();
    }

    /**
     * Returns username
     * @return Username
     */
    public String getUsername() {
        return username.getText();
    }

    /**
     * Returns users real name
     * @return Users real name
     */
    public String getRealName() {
        return realName.getText();
    }
    
}
