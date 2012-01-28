package swirc;
public class SwircModel {
    protected IrcGateway irc;
    protected String channel = "#the_three_stooges";
    
    public SwircModel() {
       System.out.println("SwircModel initialized.");
       irc = new IrcGateway();

        // Enable debugging output.
        irc.setVerbose(true);
        try {
            irc.connect("irc.cc.tut.fi");
            irc.changeNick("StoogeBot");
            irc.joinChannel(channel);
            irc.sendMessage(channel, "Iltaa!");
        } catch (Exception e) {
            System.out.println("Cant connect!");
        }
    }
    
    public void sendMsg(String msg) {
        if(msg != null && msg.length() > 0)
            irc.sendMessage(channel, msg);
    }
}
