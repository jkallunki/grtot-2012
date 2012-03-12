/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swirc;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
    
    public ChannelTab(Channel c) {
        this.channel = c;
        this.channel.addObserver(this);
        this.setLayout(new BorderLayout());
        
        messages = new JTextPane();
        messages.setText("foobar");
        messages.setEditable(false);
        JScrollPane msgPane = new JScrollPane(messages);
        this.add(msgPane, BorderLayout.CENTER);
        
        JList users = new JList(channel.getUsers());
        JScrollPane userPane = new JScrollPane(users);
        userPane.setPreferredSize(new Dimension(120, 100));
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
