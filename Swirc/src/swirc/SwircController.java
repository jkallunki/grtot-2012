package swirc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        if(code.equals("connectServer")) {
            HashMap<String,String> con = view.connectPrompt();
            this.view.addServerView(con.get("serverAddress"));
            this.model.connect(con.get("serverAddress"), con.get("nick"));
        }
        else if(code.equals("disconnect")) {
            this.model.disconnect();
        }
        else if(code.equals("join")) {
            String channel = view.joinPrompt();
            view.addChannelView(channel);
            model.joinChannel(channel);
        }
        else if(code.equals("leave")) {
            model.leaveChannel();
        }
        else if(code.equals("quit")) {
            System.exit(0);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
