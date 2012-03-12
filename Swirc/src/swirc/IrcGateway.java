package swirc;
import org.jibble.pircbot.*;

/**
 * Class to extend abstract class PircBot.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class IrcGateway extends PircBot {
    private SwircModel model;
    /**
     * Constructor.
     * @param nick Nickname of the user
     */
    public IrcGateway(SwircModel model, String nick) {
        this.model = model;
        this.setName(nick);
    }
    
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        model.receiveMessage(message, channel, sender);
    }
}
