package swirc;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * View class for Swirc MVC-model. Extends JFrame.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class SwircView extends JFrame {
    private SwircController controller;
    private SwircModel model;
    private String activeChannel;
    
    private JTextField input = new JTextField(25);
    private JButton submit = new JButton("Send");
    private JTabbedPane tabs = new JTabbedPane();
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JMenuItem join, leave;
    
    /**
     * Constructor.
     * @param model Model object of Swircs MVC-model
     */
    public SwircView(SwircModel model) {
        this.model = model;
        controller = new SwircController(model, this);
        this.model.addObserver(controller);
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        
        this.setTitle("Swirc");
        
        JPanel topPane = new JPanel(new GridLayout(0,1));
        menuBar = new JMenuBar();
        
        JMenuItem item;
        
        JMenu swircMenu = new JMenu("Swirc");
        item = new JMenuItem("Quit");
        item.setActionCommand("quit");
        item.addActionListener(controller);
        swircMenu.add(item);
        menuBar.add(swircMenu);
        
        JMenu serverMenu = new JMenu("Server");
        item = new JMenuItem("Connect server");
        item.setActionCommand("connectServer");
        item.addActionListener(controller);
        serverMenu.add(item);
        
        item = new JMenuItem("Disconnect");
        item.setActionCommand("disconnect");
        item.addActionListener(controller);
        serverMenu.add(item);
        
        
        join = new JMenuItem("Join channel");
        join.setActionCommand("join");
        join.addActionListener(controller);
        join.setEnabled(false);
        serverMenu.add(join);
        
        leave = new JMenuItem("Leave channel");
        leave.setActionCommand("leave");
        leave.addActionListener(controller);
        leave.setEnabled(false);
        serverMenu.add(leave);
        menuBar.add(serverMenu);
        
        topPane.add(menuBar);
        cp.add(topPane, BorderLayout.NORTH);
        
        submit.setActionCommand("send");
        submit.addActionListener(controller);
        
        JPanel inputPane = new JPanel();
        inputPane.setLayout(new BorderLayout());
        inputPane.add(submit, BorderLayout.EAST);
        inputPane.add(input, BorderLayout.CENTER);
        
        cp.add(inputPane, BorderLayout.SOUTH);
        
        cp.add(tabs, BorderLayout.CENTER);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(400, 300));
        this.setPreferredSize(new Dimension(600, 400));
        this.setLocationRelativeTo(null);
        
        this.pack();
        this.setVisible(true);
    }
    
    /**
     * Gets input from input-JTextField.
     * @return String from input-JTextField
     */
    public String getInput() {
        return input.getText();
    }
    
    /**
     * Sets input-JTextFields value to given value.
     * @param val Given input value
     */
    public void setInput(String val) {
        input.setText(val);
    }
    
    /**
     * Resets input-JTextFields value to empty string.
     */
    public void resetInput() {
        input.setText("");
    }
    
    /**
     * Sets join-JMenuItem enabled
     */
    public void setJoinEnabled() {
        join.setEnabled(true);
    }
    
    /**
     * Sets join-JMenuItem unenabled
     */
    public void setJoinUnenabled() {
        join.setEnabled(false);
    }
    
    /**
     * Sets leave-JMenuItem enabled
     */
    public void setLeaveEnabled() {
        leave.setEnabled(true);
    }
    
    /**
     * Sets leave-JMenuItem unenabled
     */
    public void setLeaveUnenabled() {
        leave.setEnabled(false);
    }
    
    /**
     * Returns title of active channel
     * @return 
     */
    public String getActiveChannel() {
        return tabs.getTitleAt(tabs.getSelectedIndex());
    }
    
    /**
     * Closes selected tab in tabs-JTabbedPane
     */
    public void closeTab() {
        tabs.remove(tabs.getSelectedIndex());
    }

    
    /**
     * Adds new channel view for given channel to tabs-JTabbedPane.
     * @param channel Given channel
     */
    public void addChannelView(String channel) {
        JPanel tab = new JPanel(new BorderLayout());
        
        String[] test = {"foo", "bar"};
        DefaultListModel testm = new DefaultListModel();
        
        for(int i = 0; i < test.length; i++) {
            testm.addElement(test[i]);
        }
        
        JTextPane messages = new JTextPane();
        messages.setText("foobar");
        messages.setEditable(false);
        JScrollPane msgPane = new JScrollPane(messages);
        tab.add(msgPane, BorderLayout.CENTER);
        
        JList users = new JList(testm);
        JScrollPane userPane = new JScrollPane(users);
        userPane.setPreferredSize(new Dimension(120, 100));
        tab.add(userPane, BorderLayout.EAST);
        
        tabs.addTab(channel, tab);
    }
    
    /**
     * Adds new server view for given server to tabs-JTabbedPane.
     * @param serverAddress 
     */
    public void addServerView(String serverAddress) {
        JPanel tab = new JPanel(new BorderLayout());
        
        JTextPane messages = new JTextPane();
        messages.setText("Connected " + serverAddress + "\n");
        messages.setEditable(false);
        JScrollPane msgPane = new JScrollPane(messages);
        tab.add(msgPane, BorderLayout.CENTER);
        
        tabs.addTab(serverAddress, tab);
    }
    
    /**
     * Sets up ConnectDialog and returns connection details given in it
     * @return Connection details given in dialog in HashMap.
     */
    public HashMap<String,String> connectPrompt() {
        ConnectDialog cd = new ConnectDialog(this, model);
        cd.setVisible(true);
        HashMap<String,String> connectDetails = new HashMap<String,String>();
        if(cd.isConfirmed()) {
            connectDetails.put("serverAddress", cd.getServerAddress());  
            connectDetails.put("nick", cd.getNick());  
            return connectDetails;
        }
        else {
            return null;
        }
    }
    
    /**
     * Sets up JoinDialog and returns channel given in it.
     * @return Channel given in dialog
     */
    public String joinPrompt() {
        JoinDialog jd = new JoinDialog(this, model);
        jd.setVisible(true);
        String channel = jd.getChannel();
        return channel;
    }
}
