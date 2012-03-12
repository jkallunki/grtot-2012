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
    
    /**
     * Constructor
     * @param name Name of channel
     * @param gw IrcGateway for the channel 
     */
    public Channel(String name, IrcGateway gw) {
        this.name = name;
        this.users = new DefaultListModel();
        this.contents = "Now talking at " + this.name;
        this.gateway = gw;
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
     * Kikcs given user from the channel
     * @param nick User to be kicked from the channel
     */
    public void kick(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        gateway.kick(this.name, nick);
    }
    
    /**
     * Bans given user
     * @param nick User to be banned
     */
    public void ban(String nick) {
        nick = nick.replace("@", "").replace("+", "");
        gateway.ban(name, nick + "!*@*");
    }
}