package swirc;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * Class for displaying servers
 * @author Janne Kallunki
 */
public class ServerTab extends JPanel {
    
    private JTextPane messages;
    private String serverAddress;
    
    /**
     * Constructor
     * @param serverAddress Address of the server
     */
    public ServerTab(String serverAddress) {
        this.serverAddress = serverAddress;
        this.setLayout(new BorderLayout());     
  
        messages = new JTextPane();
        messages.setText("Connecting " + serverAddress);
        messages.setEditable(false);
        JScrollPane msgPane = new JScrollPane(messages);
        this.add(msgPane, BorderLayout.CENTER);
    }
    
    public void addMsg(String msg) {
        this.messages.setText(this.messages.getText() + "\n" + msg);
    }
}
