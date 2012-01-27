package swirc;

public class Swirc {
    public static void main(String[] args) {
        IrcGateway irc = new IrcGateway();
        
        // Enable debugging output.
        irc.setVerbose(true);
        try {
            irc.connect("irc.cc.tut.fi");
            String channel = "#the_three_stooges";
            irc.changeNick("TheStoogeBot");
            irc.joinChannel(channel);
            irc.sendMessage(channel, "Iltaa!");
        } catch (Exception e) {
            System.out.println("Cant connect!");
        }
    }
}