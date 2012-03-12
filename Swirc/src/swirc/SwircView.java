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
    private Action quit, connect, disconnect, reconnect, join, leave;
    
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
        quit = new ActionQuit("Quit");
        item = new JMenuItem(quit);
        item.setActionCommand("quit");
        item.addActionListener(controller);
        swircMenu.add(item);
        menuBar.add(swircMenu);
        
        JMenu serverMenu = new JMenu("Server");
        connect = new ActionConnect("Connect server");
        item = new JMenuItem(connect);
        item.setActionCommand("connectServer");
        item.addActionListener(controller);
        serverMenu.add(item);
        
        disconnect = new ActionDisconnect("Disconnect");
        item = new JMenuItem(disconnect);
        item.setActionCommand("disconnect");
        item.addActionListener(controller);
        serverMenu.add(item);
        
        reconnect = new ActionReconnect("Reconnect");
        item = new JMenuItem(reconnect);
        item.setActionCommand("reconnect");
        item.addActionListener(controller);
        reconnect.setEnabled(false);
        serverMenu.add(item);
               
        join = new ActionJoin("Join channel");
        item = new JMenuItem(join);
        item.setActionCommand("join");
        item.addActionListener(controller);
        join.setEnabled(false);
        serverMenu.add(item);
        
        leave = new ActionLeave("Leave channel");
        item = new JMenuItem(leave);
        item.setActionCommand("leave");
        item.addActionListener(controller);
        leave.setEnabled(false);
        serverMenu.add(item);
        menuBar.add(serverMenu);
        
        JMenu optionsMenu = new JMenu("Options");
        item = new JMenuItem("User data");
        item.setActionCommand("userData");
        item.addActionListener(controller);
        optionsMenu.add(item);
        menuBar.add(optionsMenu);
        
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
    
    public void setReconnectEnabled() {
        reconnect.setEnabled(true);
    }
    
    public void setReconnectUnenabled() {
        reconnect.setEnabled(false);
    }
    
    /**
     * Returns title of active channel
     * @return 
     */
    public String getActiveChannel() {
        return tabs.getTitleAt(tabs.getSelectedIndex());
    }
    
    public int getTabCount() {
        return tabs.getTabCount();
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
    public HashMap<String, String> joinPrompt() {
        JoinDialog jd = new JoinDialog(this, model);
        jd.setVisible(true);
        HashMap<String, String> joinDetails = new HashMap<String, String>();
        if(jd.isConfirmed()) {
            joinDetails.put("server", Integer.toString(jd.getServer()));
            joinDetails.put("channel", jd.getChannel());
            return joinDetails;
        }
        else {
            return null;
        }
    }
    
     /**
     * Sets up JoinDialog and returns channel given in it.
     * @return Channel given in dialog
     */
    public HashMap<String, String> userPrompt() {
        UserDataDialog udd = new UserDataDialog(this, model);
        udd.setVisible(true);
        HashMap<String, String> userDetails = new HashMap<String, String>();
        if(udd.isConfirmed()) {
            userDetails.put("nick", udd.getNick());
            userDetails.put("secondaryNick", udd.getSecondaryNick());
            userDetails.put("username", udd.getUsername());
            userDetails.put("realName", udd.getRealName());
            return userDetails;
        }
        else {
            return null;
        }
    }
    
    public void appendMessage() {
        String [] messageArray = model.getMessage();
        System.out.println(messageArray[2]+" : "+messageArray[1]+" : "+messageArray[0]);
        int tabIndex = tabs.indexOfTab(messageArray[1]);
        //TODO viestin lisäys textpaneen
    }
    
    public class ActionQuit extends AbstractAction {
        public ActionQuit(String text) {
            super(text);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
    
    public class ActionConnect extends AbstractAction {
        public ActionConnect(String text) {
            super(text);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
    
    public class ActionDisconnect extends AbstractAction {
        public ActionDisconnect(String text) {
            super(text);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
    
    public class ActionReconnect extends AbstractAction {
        public ActionReconnect(String text) {
            super(text);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
    
    public class ActionJoin extends AbstractAction {
        public ActionJoin(String text) {
            super(text);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
    
    public class ActionLeave extends AbstractAction {
        public ActionLeave(String text) {
            super(text);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
}
