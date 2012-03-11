package swirc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller class for JoinDialog. Implements Action listener.
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
class JoinDialogController implements ActionListener {
    private SwircModel model;
    private JoinDialog view;

    /**
     * Constructor.
     * @param model Model object of Swircs MVC-model
     * @param view View object of Swircs MVC-model
     */
    public JoinDialogController(SwircModel model, JoinDialog view) {
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
        if(code.equals("cancel")) {
            view.dispose();
        }
        else if(code.equals("join")) {
            view.setConfirmed(true);
            view.dispose();
        }
    }
    
}
