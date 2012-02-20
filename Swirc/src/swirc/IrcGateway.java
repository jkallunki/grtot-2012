package swirc;
import org.jibble.pircbot.*;

public class IrcGateway extends PircBot {
    public IrcGateway(String nick) {
        this.setName(nick);
    }
}