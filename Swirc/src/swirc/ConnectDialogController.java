package swirc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ConnectDialogController provides controller for ConnectDialog. It implements
 * ActionListener interface.
 * @auth Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class ConnectDialogController implements ActionListener {
    SwircModel model;
    ConnectDialog view;
    
    /**
     *
     */
    public ConnectDialogController(SwircModel model, ConnectDialog view) {
        this.model = model;
        this.view = view;
    }

    /**
     *
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        if(code.equals("connect")) {
            view.dispose();
        }
        else if(code.equals("cancel")) {
            view.dispose();
        }
    }
}
