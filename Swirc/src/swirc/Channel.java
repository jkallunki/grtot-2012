package swirc;

import java.util.Observable;
import javax.swing.DefaultListModel;

/**
 * Class that contains channel data and functionality
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class Channel extends Observable {
    private String name;
    private String server;
    private SwircModel model;
    
    private DefaultListModel users;
    private String contents;
    
    /**
     * Constructor
     * @param name Name of channel
     * @param server Server address
     * @param model SwircModel of this channel
     */
    public Channel(String name, String server, SwircModel model) {
        this.name = name;
        this.server = server;
        this.model = model;
        this.users = new DefaultListModel();
        this.contents = "Now talking at " + this.name;
    }
    
    /**
     * Returns users of the channel
     * @return Users of the channel
     */
    public DefaultListModel getUsers() {
        return this.users;
    }
    
    /**
     * Returns name of the channel
     * @return Name of the channel
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Sends message to channel
     * @param sender Sender of the message
     * @param msg Message to be sent to channel
     */
    public void addMsg(String sender, String msg) {
        this.contents = this.contents + "\n<" + sender + "> " + msg;
        this.setChanged();
        this.notifyObservers("message");
    }
    
    /**
     * Adds a row to the channel message list
     * @param row Row to be added
     */
    public void addRow(String row) {
        this.contents = this.contents + "\n" + row;
        this.setChanged();
        this.notifyObservers("message");
    }
    
    
    /**
     * Returns contents of channel
     * @return Contents of channel
     */
    public String getContents() {
        return contents;
    }
    
    /**
     * Adds new user to channel with given nick
     * @param nick Nick of the new user
     */
    public void addUser(String nick) {
        users.addElement(nick);
        this.setChanged();
        this.notifyObservers("message");
    }
    
    /**
     * Adds message of user joining to channel to contents 
     * @param nick Nick of the user joining
     * @param login Username of the user
     * @param hostname Hostname of the server
     */
    public void userJoins(String nick, String login, String hostname) {
        this.contents = this.contents + "\n" + nick + " (" + login + "@" + hostname + ") has joined the channel.";
        this.addUser(nick);
    }
    
    /**
     * Kicks given user from the channel
     * @param nick User to be kicked from the channel
     */
    public void kick(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        model.kick(this.server, this.name, nick);
    }
    
    /**
     * Bans given user
     * @param nick User to be banned
     */
    public void ban(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        model.ban(this.server, name, nick + "!*@*");
    }
    
    /**
     * Ops given user
     * @param nick User to be opped
     */
    public void op(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        model.op(this.server, name, nick);
    }
    
    /**
     * De-ops given user
     * @param nick User to be de-opped
     */
    public void deOp(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        model.deOp(this.server, name, nick);
    }
    
    /**
     * Voices given user
     * @param nick User to be voiced
     */
    public void voice(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        model.voice(this.server, name, nick);
    }
    
    /**
     * De-voices given user
     * @param nick User to be de-voiced
     */
    public void deVoice(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        model.deVoice(this.server, name, nick);
    }
    
    /**
     * Removes user from users-list
     * @param nick User to be removed
     */
    public void removeUser(String nick) {
        Object[] nicks = users.toArray();
        for(int i = 0; i < nicks.length; i++) {
            String n = (String) nicks[i];
            String n2 = n.replace("@", "").replace("+", "");
            if(n2.equals(nick)) {
                users.removeElement(n);
            }
        }
    }
}