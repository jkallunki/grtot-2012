/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swirc;

import java.util.Observable;
import javax.swing.DefaultListModel;

/**
 * Class that contains channel data and functionality
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */

public class Channel extends Observable {
    private String name;
    private DefaultListModel users;
    private String contents;
    private IrcGateway gateway;
    
    public Channel(String name, IrcGateway gw) {
        this.name = name;
        this.users = new DefaultListModel();
        this.contents = "Now talking at " + this.name;
        this.gateway = gw;
    }
    
    public DefaultListModel getUsers() {
        return this.users;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void addMsg(String sender, String msg) {
        this.contents = this.contents + "\n<" + sender + "> " + msg;
        this.setChanged();
        this.notifyObservers("message");
    }
    
    public String getContents() {
        return contents;
    }
    
    public void addUser(String nick) {
        users.addElement(nick);
        this.setChanged();
        this.notifyObservers("message");
    }
    
    public void userJoins(String nick, String login, String hostname) {
        this.contents = this.contents + "\n" + nick + " (" + login + "@" + hostname + ") has joined the channel.";
        this.addUser(nick);
    }
    
    public void kick(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        gateway.kick(this.name, nick);
    }
    
    public void ban(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        gateway.ban(name, nick + "!*@*");
    }
}