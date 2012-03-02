package swirc;
import org.jibble.pircbot.*;

/**
 * Class to extend abstract class PircBot.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class IrcGateway extends PircBot {

    /**
     * Constructor.
     * @param nick Nickname of the user
     */
    public IrcGateway(String nick) {
        this.setName(nick);
    }
}
