package swirc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwircController {
    private SwircModel model;
    private SwircView view;
    public SwircController(SwircModel m, SwircView v) {
       System.out.println("SwircController initialized.");
       model = m;
       view = v;
       view.addSubmitListener(new SubmitListener());
    }
    
    class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.sendMsg(view.getInput());
            view.resetInput();
        }
    }
}
