package swirc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Janne Kallunki, Ville Hämäläinen, Jaakko Ritvanen
 */
class JoinDialogController implements ActionListener {
    private SwircModel model;
    private JoinDialog view;

    /**
     * 
     * @param model
     * @param view 
     */
    public JoinDialogController(SwircModel model, JoinDialog view) {
        this.model = model;
        this.view = view;
    }

    /**
     * 
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String code = e.getActionCommand();
        if(code.equals("cancel")) {
            view.dispose();
        }
        else if(code.equals("join")) {
            view.dispose();
        }
    }
    
}
