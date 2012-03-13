package swirc;

import java.util.ArrayList;
import java.util.Observable;
import org.jibble.pircbot.NickAlreadyInUseException;

/**
 * Model class for Swirc MVC-model. Extends abstract class Observable.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class SwircModel extends Observable {
    private ArrayList<IrcGateway> connections = new ArrayList<IrcGateway>();
    private SwircConfs confs;
    
    // Temporary container for single gateways being handled:
    private IrcGateway irc;
    
    /**
     * Constructor.
     */
    public SwircModel() {
        confs = SwircConfs.getInstance();
    }
    
    /**
     * Method sends message to channel.
     * @param msg Message to be sent
     * @param channel Channel wherre message will be sent 
     */
    public void sendMsg(String msg, String channel) {
        Object[] cons = connections.toArray();
        if(msg != null && msg.length() > 0) {
            for(int i = 0; i < cons.length; i++) {
                irc = (IrcGateway) cons[i];
                irc.sendMessage(channel, msg);
                if(irc.getChannel(channel) != null) {
                    irc.getChannel(channel).addMsg(irc.getNick(), msg);
                }
            }
        }
    }
    
    /**
     * Method creates connection to server.
     * @param serverAddress Address of the server
     * @param nick Nickname of the user
     * @param port Server's port
     * @param password  Server's password
     */
    public void connect(String serverAddress, String nick, String port, String password) {
        try {
            IrcGateway igw = new IrcGateway(this, nick);
            igw.setVerbose(true);
            if(port == null && password == null) {
                igw.connect(serverAddress);
            }
            else if(port != null && password == null) {
                igw.connect(serverAddress, Integer.parseInt(port));
            }
            else {
                igw.connect(serverAddress, Integer.parseInt(port), password);
            }
            connections.add(igw);
            if(!confs.findServer(serverAddress)) {
                confs.addServer(serverAddress);
            }
            this.setChanged();
            this.notifyObservers("connected");
        }
        catch(NickAlreadyInUseException e) {
            IrcGateway igw = new IrcGateway(this, confs.getUserData("secondaryNick"));
            igw.setVerbose(true);
            try {
                if(port == null && password == null) {
                    igw.connect(serverAddress);
                }
                else if(port != null && password == null) {
                    igw.connect(serverAddress, Integer.parseInt(port));
                }
                else {
                    igw.connect(serverAddress, Integer.parseInt(port), password);
                }
                connections.add(igw);
                if(!confs.findServer(serverAddress)) {
                    confs.addServer(serverAddress);
                }
                this.setChanged();
                this.notifyObservers("connected");
            }
            catch(Exception ee) {
                System.out.println("Cant connect!");
                this.setChanged();
                this.notifyObservers("cant connect");
            }
        }
        catch(Exception e) {
            System.out.println("Cant connect!");
            this.setChanged();
            this.notifyObservers("cant connect");
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
                this.setChanged();
                this.notifyObservers("cant connect");
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
        }
        catch(Exception e) {
            this.setChanged();
            this.notifyObservers("cant join");
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
     * Returns servers which has been used from confs.
     * @return Used servers
     */
    public String[] getUsedServers() {
        return confs.getServers();
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
    
    /**
     * Sets user data to confs
     * @param key Key to user's data
     * @param value User's data
     */
    public void setUserData(String key, String value) {
        confs.setUserData(key, value);
    }
    
    /**
     * Returns user data with given key
     * @param key Key to user data
     * @return User data with given key
     */
    public String getUserData(String key) {
        return confs.getUserData(key);
    }
    
    /**
     * Saves user's data
     */
    public void saveUserData() {
        confs.saveUserData();
    }
    
    /**
     * Notifies Observers that someone joined to channel
     */
    void joinedChannel(Channel c) {
        this.setChanged();
        this.notifyObservers(c);
        System.out.println("JOINED CHANNEL " + c.getName());
    }
}
