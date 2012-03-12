package swirc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

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
    private SwircConfs confs;
    
    // Temporary container for single gateways being handled:
    private IrcGateway irc;
    
    /**
     * Constructor.
     */
    public SwircModel() {
        confs = SwircConfs.getInstance();
        /*IrcGateway igw = new IrcGateway();
        connections.add(igw);
        
        // Enable debugging output.
        igw.setVerbose(true);
        try {
            igw.connect("irc.cc.tut.fi");
            igw.changeNick("StoogeBot3");
            igw.joinChannel(channel);
            igw.sendMessage(channel, "Iltaa!");
        } catch (Exception e) {
            System.out.println("Cant connect!");
        }*/
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
            if(!confs.findServer(serverAddress)) {
                confs.addServer(serverAddress);
            }
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
    
    public void saveUserInfo(HashMap<String, String> info) {
        // TODO
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
}
