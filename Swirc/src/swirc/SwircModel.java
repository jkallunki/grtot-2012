package swirc;


import java.util.*;

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
     * @param port Server's port
     * @param password  Server's password
     */
    public void connect(String serverAddress, String port, String password) {
        IrcGateway igw;
        try {
            igw = new IrcGateway(this, serverAddress, confs.getUserData("nick"), port, password);
            new Thread(igw).start();
            connections.add(igw);
            if(!confs.findServer(serverAddress)) {
                confs.addServer(serverAddress);
            }
            this.setChanged();
            this.notifyObservers("connected");
        }
        catch (Exception ex) {
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
     * @param userData User's data
     */
    public void setUserData(HashMap<String, String> userData) {
        HashMap<String, String> data = new HashMap<String, String>();
        Iterator i = userData.entrySet().iterator();
        while(i.hasNext()) {
            Map.Entry entry = (Map.Entry)i.next();
            
            if(data.containsValue((String)entry.getValue())) {
                this.setChanged();
                this.notifyObservers("userDataDublicate");
                break;
            }
            else {
                confs.setUserData((String)entry.getKey(), (String)entry.getValue());
            }
            data.put((String)entry.getKey(), (String)entry.getValue());
            i.remove();
        }
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
     * @param c Channel where someone joined
     */
    public void joinedChannel(Channel c) {
        this.setChanged();
        this.notifyObservers(c);
    }
    
    /**
     * Returns IrcGateway which has given name of the server
     * @param serverName Name of the server in wanted IrcGateway
     * @return IrcGateway which has given name of the server
     */
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
    
    /**
     * Kicks user with given nick out of the given channel in given server
     * @param server Server of the wanted channel
     * @param channel Channel from where user will be kicked
     * @param nick User's nick
     */
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
    
    /**
     * Bans user with given nick out of the given channel in given server
     * @param server Server of the wanted channel
     * @param channel Channel from where user will be kicked
     * @param nick User's nick
     */
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

    /**
     * Gives op rights to user
     * @param server Server of the event
     * @param channel Channel of the event
     * @param nick User to be given the op rights
     */
    public void op(String server, String channel, String nick) {
        // Get the correct connection
        IrcGateway gw = this.getGateway(server);
        
        if(gw != null) {
            // Remove mode symbols
            nick = nick.replace("@", "").replace("+", "");

            // Op the user
            gw.op(channel, nick);
        }
    }
    
    /**
     * Removes op rights from user
     * @param server Server of the event
     * @param channel Channel of the event
     * @param nick User whose op rights will be removed
     */
    public void deOp(String server, String channel, String nick) {
        // Get the correct connection
        IrcGateway gw = this.getGateway(server);
        
        if(gw != null) {
            // Remove mode symbols
            nick = nick.replace("@", "").replace("+", "");

            // Op the user
            gw.deOp(channel, nick);
        }
    }
    
    /**
     * Gives voice rights to user
     * @param server Server of the event
     * @param channel Channel of the event
     * @param nick User who will be given voice rights
     */
    public void voice(String server, String channel, String nick) {
        // Get the correct connection
        IrcGateway gw = this.getGateway(server);
        
        if(gw != null) {
            // Remove mode symbols
            nick = nick.replace("@", "").replace("+", "");

            // Ban only the nick without hostmask
            gw.voice(channel, nick);
        }
    }
    
    /**
     * Removes voice rights from user
     * @param server Server of the event
     * @param channel Channel of the event
     * @param nick User whose voice rights will be removed
     */
    public void deVoice(String server, String channel, String nick) {
        // Get the correct connection
        IrcGateway gw = this.getGateway(server);
        
        if(gw != null) {
            // Remove mode symbols
            nick = nick.replace("@", "").replace("+", "");

            // Ban only the nick without hostmask
            gw.deVoice(channel, nick);
        }
    }
    
    /**
     * Returns configuration object
     * @return SwircConfs configuration object
     */
    public SwircConfs getConfs() {
        return confs;
    }
    
    /**
     * Notifies observers that can't connect to server
     */
    public void cantConnect() {
        this.setChanged();
        this.notifyObservers("cant connect");
    }
    
    /**
     * Notifies observers that has connected to server
     * @param serverAddress server where is connected
     */
    public void connectedServer(String serverAddress) {
        this.setChanged();
        this.notifyObservers("ConnectedServer:" + serverAddress);
    }
}
