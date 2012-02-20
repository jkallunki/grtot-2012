package swirc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class SwircController implements ActionListener, Observer {
    private SwircModel model;
    private SwircView view;
    
    public SwircController(SwircModel m, SwircView v) {
       model = m;
       view = v;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        if(code.equals("connectServer")) {
            HashMap<String,String> con = view.connectPrompt();
            view.addServerView(con.get("serverAddress"));
            model.connect(con.get("serverAddress"), con.get("nick"));
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
