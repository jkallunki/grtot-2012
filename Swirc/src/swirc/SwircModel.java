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
        } catch (Exception e) {
            System.out.println("Cant connect!");
        }
    }
    
    /**
     * Disconnects every IrcGateway in connections-ArrayList
     */
    public void disconnect() {
        Object[] cons = connections.toArray();
        for(int i = 0; i < cons.length; i++) {
            irc = (IrcGateway) cons[i];
            irc.disconnect();
        }
    }

    /**
     * Joins to channel given in String.
     * @param channel Given channel.
     */
    public void joinChannel(String channel) {
        if(channel.charAt(0)!='#') 
            channel = "#"+channel;
        Object[] cons = connections.toArray();
        irc = (IrcGateway) cons[0];
        
        irc.joinChannel(channel);
    }

    /**
     * Leaves from channel.
     */
    public void leaveChannel() {
        //TODO
        
    }
}
