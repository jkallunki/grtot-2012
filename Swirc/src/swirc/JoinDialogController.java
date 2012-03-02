/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package swirc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author jask
 */
class JoinDialogController implements ActionListener {
    private SwircModel model;
    private JoinDialog view;

    public JoinDialogController(SwircModel model, JoinDialog view) {
        this.model = model;
        this.view = view;
    }

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
