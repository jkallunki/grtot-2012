package swirc;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

public class SwircView extends JFrame {
    private SwircController controller;
    private SwircModel model;
    
    private JTextField input = new JTextField(25);
    private JButton submit = new JButton("Send");
    private JTabbedPane tabs = new JTabbedPane();
    private JMenuBar menuBar;
    private JToolBar toolBar;
    
    public SwircView(SwircModel model) {
        this.model = model;
        controller = new SwircController(model, this);
        
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
        
        
        item = new JMenuItem("Join channel");
        item.setActionCommand("join");
        item.addActionListener(controller);
        serverMenu.add(item);
        
        item = new JMenuItem("Leave channel");
        item.setActionCommand("leave");
        item.addActionListener(controller);
        serverMenu.add(item);
        menuBar.add(serverMenu);
        
        topPane.add(menuBar);
        cp.add(topPane, BorderLayout.NORTH);
        
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
    
    public String getInput() {
        return input.getText();
    }
    
    public void setInput(String val) {
        input.setText(val);
    }
    
    public void resetInput() {
        input.setText("");
    }
    
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
        
        tabs.addTab("#" + channel, tab);
    }
    
    public void addServerView(String serverAddress) {
        JPanel tab = new JPanel(new BorderLayout());
        
        JTextPane messages = new JTextPane();
        messages.setText("Connected " + serverAddress + "\n");
        messages.setEditable(false);
        JScrollPane msgPane = new JScrollPane(messages);
        tab.add(msgPane, BorderLayout.CENTER);
        
        tabs.addTab(serverAddress, tab);
    }
    
    public HashMap<String,String> connectPrompt() {
        ConnectDialog cd = new ConnectDialog(this, model);
        cd.setVisible(true);
        HashMap<String,String> connectDetails = new HashMap<String,String>();
        connectDetails.put("serverAddress", cd.getServerAddress());  
        connectDetails.put("nick", cd.getNick());    
        return connectDetails;
    }
    
    public String joinPrompt() {
        JoinDialog jd = new JoinDialog(this, model);
        jd.setVisible(true);
        String channel = jd.getChannel();
        return channel;
    }
}
