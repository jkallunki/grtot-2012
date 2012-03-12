package swirc;
import java.util.ArrayList;
import java.util.Iterator;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

/**
 * Class to extend abstract class PircBot.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class IrcGateway extends PircBot {
    private SwircModel model;
    private ArrayList<Channel> channels;
    /**
     * Constructor.
     * @param nick Nickname of the user
     */
    public IrcGateway(SwircModel model, String nick) {
        this.model = model;
        this.setName(nick);
        channels = new ArrayList<Channel>();
    }
    
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        //model.receiveMessage(message, channel, sender);
        this.getChannel(channel).addMsg(sender, message);
    }
    
    public Channel getChannel(String name) {
        Iterator<Channel> i = channels.iterator();
        while (i.hasNext()) {
            Channel c = i.next();
            if(name.equals(c.getName())) return c;
        }
        return null;
    }
    
    @Override
    protected void onJoin(String channelName, String joinedNick, String login, String hostname)  {
        Channel c;
        
        // We joined a channel
        System.out.println("onJoin()");
        if(joinedNick.equals(this.getNick())) {
            System.out.println("it was us!");
            c = new Channel(channelName, this);
            channels.add(c);
            model.joinedChannel(c);
        }
        else {
            c = this.getChannel(channelName);
            if(c != null) {
                c.userJoins(joinedNick, login, hostname);
            }
        }
    }
    
    @Override
    protected void onUserList(String channel, User[] users) {
        Channel c = this.getChannel(channel);
        for(int i = 0; i < users.length; i++) {
            c.addUser(users[i].toString());
        }
    }
}
