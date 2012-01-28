package swirc;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class SwircView extends JFrame {
    private JTextField input = new JTextField(25);
    private JButton submit = new JButton("Send");
    
    public SwircView(SwircModel model) {
        System.out.println("SwircView initialized.");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Swirc");
        
        // Constructing the layout
        JPanel content = new JPanel();
        content.setLayout(new FlowLayout());
        
        JPanel inputs = new JPanel();
        inputs.setLayout(new FlowLayout());
        inputs.add(input);
        inputs.add(submit);
        
        content.add(inputs);
        this.setContentPane(content);
        
        // Set preferred size for the window:
        this.pack();
    }
    
    public String getInput() {
        return input.getText();
    }
    
    public void setInput(String val) {
        input.setText(val);
    }
    
    public void resetInput() {
        input.setText("");
    }
    
    
    public void addSubmitListener(ActionListener sal) {
        submit.addActionListener(sal);
    }
}