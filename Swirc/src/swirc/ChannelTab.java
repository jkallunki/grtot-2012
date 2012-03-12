package swirc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

/**
 * Class for creating channel tabs
 * @author Janne Kallunki
 */
public class ChannelTab extends JPanel implements Observer {
    
    private Channel channel;
    private JTextPane messages;
    private JPopupMenu userMenu;
    private JList users;
    
    /**
     * Constructor
     * @param c Channel of tab being created
     */
    public ChannelTab(Channel c) {
        this.channel = c;
        this.channel.addObserver(this);
        this.setLayout(new BorderLayout());     
        
        messages = new JTextPane();
        messages.setText("foobar");
        messages.setEditable(false);
        JScrollPane msgPane = new JScrollPane(messages);
        this.add(msgPane, BorderLayout.CENTER);
        
        users = new JList(channel.getUsers());
        JScrollPane userPane = new JScrollPane(users);
        userPane.setPreferredSize(new Dimension(120, 100));
        
        userMenu = new JPopupMenu();
        
        JMenuItem item = new JMenuItem("Kick");
        item.setActionCommand("kick");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nick = (String) users.getSelectedValue();
                channel.kick(nick);
            }
            
        });
        userMenu.add(item);
        
        item = new JMenuItem("Ban");
        item.setActionCommand("ban");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nick = (String) users.getSelectedValue();
                channel.ban(nick);
            }
            
        });
        userMenu.add(item);
        
        MouseListener userMenuListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e)  {
                trigger(e);
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                trigger(e);
            }
            
            public void trigger(MouseEvent e) {
                if (e.isPopupTrigger()) { //if the event shows the menu
                    users.setSelectedIndex(users.locationToIndex(e.getPoint())); //select the item
                    userMenu.show(users, e.getX(), e.getY()); //and show the menu
                }
            }
            
        };
        users.addMouseListener(userMenuListener);
        
        this.add(userPane, BorderLayout.EAST);
    }
    
    /**
     * Returns name of the tab's channel
     * @return Name of the tab's channel
     */
    public String getChannelName() {
        return channel.getName();
    }
    
    /**
     * This method is called whenever the observed object is changed. 
     * An application calls an Observable object's notifyObservers method to 
     * have all the object's observers notified of the change.
     * @param o the observable object
     * @param arg an argument passed to the notifyObservers method
     */
    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals("message")) {
            messages.setText(channel.getContents());
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
    
}
