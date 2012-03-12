package swirc;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Model class for Swirc MVC-model. Extends abstract class Observable.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class SwircModel extends Observable {
    private ArrayList<IrcGateway> connections = new ArrayList<IrcGateway>();
    //private String channel = "#the_three_stooges";
    
    // Temporary container for single gateways being handled:
    private IrcGateway irc;
    
    /**
     * Constructor.
     */
    public SwircModel() {
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
        IrcGateway igw = new IrcGateway(nick);
        
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
    
    public void reconnect() {
        Object[] cons = connections.toArray();
        for(int i = 0; i < cons.length; i++) {
            irc = (IrcGateway) cons[i];
            try {
                irc.reconnect();
            }
            catch(Exception e) {
                // TODO ilmoitus virheestä
            }
        }
    }

    /**
     * Joins to channel given in String.
     * @param channel Given channel.
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
     * Leaves from channel.
     */
    public void leaveChannel(String channel) {
        Object[] cons = connections.toArray();
        irc = (IrcGateway) cons[0];
        irc.partChannel(channel);
        this.setChanged();
        this.notifyObservers("leave");
    }
    
    public String[] getConnectedServers() {
        Object[] cons = connections.toArray();
        String[] servers = new String[cons.length];
        for(int i = 0; i < cons.length; i++) {
            irc = (IrcGateway) cons[i];
            String temp = irc.toString();
            String[] temp1 = temp.split(" ");
            servers[i] = temp1[8].substring(7, temp1[8].length() - 1);
        }
        return servers;
    }
    
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
}
