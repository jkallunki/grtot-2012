package swirc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Properties;

/**
 * Model class for Swirc MVC-model. Extends abstract class Observable.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class SwircModel extends Observable {
    private ArrayList<IrcGateway> connections = new ArrayList<IrcGateway>();
    //private String channel = "#the_three_stooges";
    private String message;
    private String sender;
    private String channel;
    
    // Temporary container for single gateways being handled:
    private IrcGateway irc;
    
    // User data
    private Properties userData;
    
    /**
     * Constructor.
     */
    public SwircModel() {
        // Handling user data
        userData = new Properties();
        try {
            FileInputStream dataIn = new FileInputStream("userData");
            userData.load(dataIn);
            dataIn.close();
        }
        catch(Exception e) {
            //TODO properties not found
        }
    }
    
    /**
     * Method sends message to channel.
     * @param msg Message to be sent
     * @param channel Channel wherre message will be sent 
     */
    public void sendMsg(String msg, String channel) {
        Object[] cons = connections.toArray();
        if(msg != null && msg.length() > 0)
            for(int i = 0; i < cons.length; i++) {
                irc = (IrcGateway) cons[i];
                irc.sendMessage(channel, msg);
            }
    }
    
    /**
     * Method creates connection to server.
     * @param serverAddress Address of the server
     * @param nick Nickname of the user 
     */
    public void connect(String serverAddress, String nick) {
        IrcGateway igw = new IrcGateway(this, nick);
        
        // Enable debugging output.
        igw.setVerbose(true);
        try {
            igw.connect(serverAddress);
            connections.add(igw);
            this.setChanged();
            this.notifyObservers("connected");
        } catch (Exception e) {
            System.out.println("Cant connect!");
        }
    }
    
    /**
     * Disconnects every IrcGateway in connections-ArrayList
     */
    public void disconnect() {
        // virheenkäsittely!!!
        Object[] cons = connections.toArray();
        for(int i = 0; i < cons.length; i++) {
            irc = (IrcGateway) cons[i];
            irc.disconnect();
            this.setChanged();
            this.notifyObservers("disconnect");
        }
    }
    
    /**
     * Reconnects to disconnected IrcGateway
     */
    public void reconnect() {
        Object[] cons = connections.toArray();
        for(int i = 0; i < cons.length; i++) {
            irc = (IrcGateway) cons[i];
            try {
                irc.reconnect();
                this.setChanged();
                this.notifyObservers("reconnect");
            }
            catch(Exception e) {
                // TODO ilmoitus virheestä
            }
        }
    }

    /**
     * Joins to channel in given server.
     * @param channel Given channel.
     * @param server Server of channel.
     */
    public void joinChannel(String channel, int server) {
        if(channel.charAt(0)!='#') 
            channel = "#"+channel;
        Object[] cons = connections.toArray();
        try {
            irc = (IrcGateway) cons[server];
            irc.joinChannel(channel);
            this.setChanged();
            this.notifyObservers("join");
        }
        catch(Exception e) {
            // TODO
        }
    }

    /**
     * Leaves from given channel.
     * @param channel Channel from where to leave
     */
    public void leaveChannel(String channel) {
        Object[] cons = connections.toArray();
        irc = (IrcGateway) cons[0];
        irc.partChannel(channel);
        this.setChanged();
        this.notifyObservers("leave");
    }
    
    /**
     * Returns array of connected servers
     * @return Array of connected servers
     */
    public String[] getConnectedServers() {
        Object[] cons = connections.toArray();
        String[] servers = new String[cons.length];
        for(int i = 0; i < cons.length; i++) {
            irc = (IrcGateway) cons[i];
            servers[i] = irc.getServer();
        }
        return servers;
    }
    
    /**
     * Returns array of connected channels
     * @return Array of connected channels
     */
    public String[] getConnectedChannels() {
        Object[] cons = connections.toArray();
        String[] channels = null;
        for(int i = 0; i < cons.length; i++) {
            irc = (IrcGateway) cons[i];
            String[] temp = irc.getChannels();
            channels = temp;
        }
        return channels;
    }
    
    public void receiveMessage(String message, String channel, String sender) {
        this.channel = channel;
        this.message = message;
        this.sender = sender;
        this.setChanged();
        this.notifyObservers("message");
    }
    
    public String[] getMessage() {
        String[] messageArray = new String[3];
        messageArray[0] = this.message;
        messageArray[1] = this.channel;
        messageArray[2] = this.sender;
        return messageArray;
    }
    
    public void setUserData(String key, String value) {
        this.userData.setProperty(key, value);
    }
    
    public String getUserData(String key) {
        return this.userData.getProperty(key);
    }
    
    public void saveUserData() {
        try {
            FileOutputStream out = new FileOutputStream("userData");
            userData.store(out, "---No Comment---");
            out.close();
        }
        catch(Exception e) {
            //TODO Saving user data failed
        }
    }
}
