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
    
    /**
     * Constructor
     * @param name Name of channel
     */
    public Channel(String name) {
        this.name = name;
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
     * @param msg Message to be sent to channel
     */
    public void addMsg(String msg) {
        this.contents = this.contents + "\n" + msg;
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
}