package swirc;

import java.util.ArrayList;
import java.util.Observable;

public class SwircModel extends Observable {
    private ArrayList<IrcGateway> connections = new ArrayList<IrcGateway>();
    private String channel = "#the_three_stooges";
    
    // Temporary container for single gateways being handled:
    private IrcGateway irc;
    
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
    
    public void sendMsg(String msg) {
        Object[] cons = connections.toArray();
        if(msg != null && msg.length() > 0)
            for(int i = 0; i < cons.length; i++) {
                irc = (IrcGateway) cons[i];
                irc.sendMessage(channel, msg);
            }
    }
    
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
}
