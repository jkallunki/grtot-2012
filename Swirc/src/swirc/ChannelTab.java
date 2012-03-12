/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swirc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

/**
 *
 * @author Janne
 */
public class ChannelTab extends JPanel implements Observer {
    
    private Channel channel;
    private JTextPane messages;
    private JPopupMenu userMenu;
    private JList users;
    
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
        //item.addActionListener(new PopupListener());
        userMenu.add(item);
        
        item = new JMenuItem("Ban");
        item.setActionCommand("ban");
        //item.addActionListener(new PopupListener());
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
    
    public String getChannelName() {
        return channel.getName();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals("message")) {
            messages.setText(channel.getContents());
        }
    }
    
    
}
