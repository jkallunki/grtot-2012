package swirc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ConnectDialogController provides controller for ConnectDialog. It implements
 * ActionListener interface.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
public class ConnectDialogController implements ActionListener {
    SwircModel model;
    ConnectDialog view;
    
    /**
     * Constructer.
     * @param model Model object of Swircs MVC-model
     * @param view View object of Swircs MVC-model
     */
    public ConnectDialogController(SwircModel model, ConnectDialog view) {
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
        if(code.equals("connect")) {
            view.dispose();
        }
        else if(code.equals("cancel")) {
            view.dispose();
        }
    }
}
