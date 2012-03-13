package swirc;

import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     */
    public void connect(String serverAddress, String nick, String port, String password) {
        IrcGateway igw;
        try {
            igw = new IrcGateway(this, serverAddress, nick, port, password);
            new Thread(igw).start();
            connections.add(igw);
            if(!confs.findServer(serverAddress)) {
                confs.addServer(serverAddress);
            }
            this.setChanged();
        } catch (Exception ex) {
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
    
    public void setUserData(String key, String value) {
        confs.setUserData(key, value);
    }
    
    public String getUserData(String key) {
        return confs.getUserData(key);
    }
    
    public void saveUserData() {
        confs.saveUserData();
    }

    public void joinedChannel(Channel c) {
        this.setChanged();
        this.notifyObservers(c);
    }
    
    public IrcGateway getGateway(String serverName) {
        Object[] cons = connections.toArray();
        String[] servers = new String[cons.length];
        for(int i = 0; i < cons.length; i++) {
            irc = (IrcGateway) cons[i];
            if(serverName.equals(irc.getServer())) {
                return irc;
            }
        }
        return null;
    }
    
    public void kick(String server, String channel, String nick) {
        // Get the correct connection
        IrcGateway gw = this.getGateway(server);
        
        if(gw != null) {
            // Remove mode symbols
            nick = nick.replace("@", "").replace("+", "");

            // Kick the user
            gw.kick(channel, nick);
        }
    }
    
    public void ban(String server, String channel, String nick) {
        // Get the correct connection
        IrcGateway gw = this.getGateway(server);
        
        if(gw != null) {
            // Remove mode symbols
            nick = nick.replace("@", "").replace("+", "");

            // Ban only the nick without hostmask
            gw.ban(channel, nick + "!*@*");
        }
    }
    
    public void cantConnect() {
        this.setChanged();
        this.notifyObservers("cant connect");
    }
    
    public void connectedServer(String serverAddress) {
        this.setChanged();
        this.notifyObservers("ConnectedServer:" + serverAddress);
    }
}
