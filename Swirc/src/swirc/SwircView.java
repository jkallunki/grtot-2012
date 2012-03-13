package swirc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

/**
 * View class for Swirc MVC-model. Extends JFrame.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class SwircView extends JFrame implements Observer {
    private SwircController controller;
    private SwircModel model;
    
    private JTextField input = new JTextField(25);
    private JButton submit = new JButton("Send");
    private JTabbedPane tabs = new JTabbedPane();
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private Action quit, connect, disconnect, reconnect, join, leave;
    
    private HashMap<String,ServerTab> serverTabs = new HashMap<String,ServerTab>();
    
    /**
     * Constructor.
     * @param model Model object of Swircs MVC-model
     */
    public SwircView(SwircModel model) {
        this.model = model;
        controller = new SwircController(model, this);
        this.model.addObserver(controller);
        this.model.addObserver(this);
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
        disconnect.setEnabled(false);
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
     * Sets disconnect-JMenuItem enabled
     */
    public void setDisconnectEnabled() {
        disconnect.setEnabled(true);
    }
    
    /**
     * Sets disconnect-JMenuItem unenabled
     */
    public void setDisconnectUnenabled() {
        disconnect.setEnabled(false);
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
     * Sets reconnect action to enabled
     */
    public void setReconnectEnabled() {
        reconnect.setEnabled(true);
    }
    
    /**
     * Sets reconnect action to unenabled
     */
    public void setReconnectUnenabled() {
        reconnect.setEnabled(false);
    }
    
    /**
     * Returns title of active channel
     * @return Title of active channel
     */
    public String getActiveChannel() {
        return tabs.getTitleAt(tabs.getSelectedIndex());
    }
    
    /**
     * Returns count of tabs
     * @return Count of tabs
     */
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
     * Closes all tabs
     */
    public void closeAllTabs() {
        tabs.removeAll();
    }
    
    /**
     * Shows warning message in dialog
     * @param message Warning message
     */
    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    
    /**
     * Adds new channel view for given channel to tabs-JTabbedPane.
     * @param c Given channel
     */
    public void addChannelView(Channel c) {
        ChannelTab ct = new ChannelTab(c);
        tabs.addTab(c.getName(), ct);
    }
    
    /**
     * Adds new server view for given server to tabs-JTabbedPane.
     * @param serverAddress 
     */
    public void addServerView(String serverAddress) {
        ServerTab tab = new ServerTab(serverAddress);
        serverTabs.put(serverAddress, tab);
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
            connectDetails.put("port", cd.getPort());
            connectDetails.put("password", cd.getPassword());
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
     * Sets up UserDataDialog and returns data given in it.
     * @return User data given in dialog
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

    /**
     * This method is called whenever the observed object is changed. An 
     * application calls an Observable object's notifyObservers method to 
     * have all the object's observers notified of the change.
     * @param o The observable object.
     * @param arg An argument passed to the notifyObservers method.
     */
    @Override
    public void update(Observable o, Object arg) {
        
        // Joined a channel
        if(arg instanceof Channel) {
            this.addChannelView((Channel) arg);
            this.setLeaveEnabled();
        }
        
        if(arg instanceof String) {
            String code = (String)arg;
            if(code.startsWith("ConnectedServer")) {
                String serverAddress = code.replace("ConnectedServer:", "");
                serverTabs.get(serverAddress).addMsg("Connected.");
            }
        }
    }
    
//TODO javadoc
    
    /**
     * Inner class for quit action
     */
    public class ActionQuit extends AbstractAction {
        
        /**
         * Constructor
         * @param text Defines an Action object with the specified description 
         * string and a default icon
         */
        public ActionQuit(String text) {
            super(text);
        }
        
        /**
         * Invoked when an action occurs. 
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
    
    /**
     * Inner class for connect action
     */
    public class ActionConnect extends AbstractAction {
        
        /**
         * Constructor
         * @param text Defines an Action object with the specified description 
         * string and a default icon
         */
        public ActionConnect(String text) {
            super(text);
        }
        
        /**
         * Invoked when an action occurs. 
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
    
    /**
     * Inner class for disconnect action
     */
    public class ActionDisconnect extends AbstractAction {
        
        /**
         * Constructor
         * @param text Defines an Action object with the specified description 
         * string and a default icon
         */
        public ActionDisconnect(String text) {
            super(text);
        }
        
        /**
         * Invoked when an action occurs. 
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
    
    /**
     * Inner class for reconnect action
     */
    public class ActionReconnect extends AbstractAction {
        
        /**
         * Constructor
         * @param text Defines an Action object with the specified description 
         * string and a default icon
         */
        public ActionReconnect(String text) {
            super(text);
        }
        
        /**
         * Invoked when an action occurs. 
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
    
    /**
     * Inner class for join action
     */
    public class ActionJoin extends AbstractAction {
        
        /**
         * Constructor
         * @param text Defines an Action object with the specified description 
         * string and a default icon
         */
        public ActionJoin(String text) {
            super(text);
        }
        
        /**
         * Invoked when an action occurs. 
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
    
    /**
     * Inner class for leave action
     */
    public class ActionLeave extends AbstractAction {
        
        /**
         * Constructor
         * @param text Defines an Action object with the specified description 
         * string and a default icon
         */
        public ActionLeave(String text) {
            super(text);
        }
        
        /**
         * Invoked when an action occurs. 
         * @param e ActionEvent
         */
        @Override
        public void actionPerformed(ActionEvent e) {
        }
        
    }
}
