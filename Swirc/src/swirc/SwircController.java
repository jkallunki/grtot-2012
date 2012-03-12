package swirc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Controller class for Swirc MVC-model. Implements ActionListener and Observer
 * interfaces.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class SwircController implements ActionListener, Observer {
    private SwircModel model;
    private SwircView view;
    
    /**
     * Constructer.
     * @param model Model object of Swircs MVC-model
     * @param view View object of Swircs MVC-model
     */
    public SwircController(SwircModel model, SwircView view) {
       this.model = model;
       this.view = view;
    }

    /**
     * Invoked when an action occurs. 
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        if(code.equals("connectServer")) {
            HashMap<String,String> con = view.connectPrompt();
            if(con != null && con.get("serverAddress") != null && con.get("nick") != null) {
                this.view.addServerView(con.get("serverAddress"));
                this.model.connect(con.get("serverAddress"), con.get("nick"));
            }
        }
        else if(code.equals("disconnect")) {
            this.model.disconnect();
        }
        else if(code.equals("reconnect")) {
            this.model.reconnect();
        }
        else if(code.equals("join")) {
            HashMap<String, String> join = view.joinPrompt();
            if(join != null) {
                view.addChannelView(join.get("channel"));
                model.joinChannel(join.get("channel"), Integer.parseInt(join.get("server")));
            }
        }
        else if(code.equals("leave")) {
            String channel = view.getActiveChannel();
            if(channel.startsWith("#")) {
                model.leaveChannel(channel);
            }
        }
        else if(code.equals("quit")) {
            System.exit(0);
        }
        else if(code.equals("userData")) {
            HashMap<String, String> saveUser = view.userPrompt();
            if(saveUser != null) {
                Iterator i = saveUser.entrySet().iterator();
                while(i.hasNext()) {
                    Map.Entry entry = (Map.Entry)i.next();
                    this.model.setUserData((String)entry.getKey(), (String)entry.getValue());
                    i.remove();
                }
                this.model.saveUserData();

            }
        }
        else if(code.equals("send")) {
            String msg = view.getInput();
            String channel = view.getActiveChannel();
            if(!msg.isEmpty() && channel.startsWith("#")) { 
                model.sendMsg(msg, channel);
            }
            view.resetInput();
        }
    }

    /**
     * This method is called whenever the observed object is changed.
     * @param o The observable object.
     * @param arg  An argument passed to the notifyObservers method.
     */
    @Override
    public void update(Observable o, Object arg) {
        String code = arg.toString();
        //System.out.println(code);
        if(code.equals("connected")) {
            view.setJoinEnabled();
            view.setReconnectEnabled();
        }
        else if(code.equals("disconnect")) {
            view.closeAllTabs();
            view.setJoinUnenabled();
            view.setLeaveUnenabled();
        }
        else if(code.equals("reconnect")) {
            String[] servers = model.getConnectedServers();
            if(servers != null) {
                for(int i = 0; i < servers.length; i++) {
                    this.view.addServerView(servers[i]);
                }
            }
        }
        else if(code.equals("join")) {
            view.setLeaveEnabled();
        }
        else if(code.equals("leave")) {
            view.closeTab();
            if(view.getTabCount() < 2) {
                view.setLeaveUnenabled();
            }
        }
        else if(code.equals("message")) {
            view.appendMessage();
        }
    }
}
