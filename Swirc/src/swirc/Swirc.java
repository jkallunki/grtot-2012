package swirc;

/**
 * Main-class for Swirc IRC-client
 * @author Janne Kallunki
 */
public class Swirc {
    
    /*
     * Main method. Wraps MVC-model together.
     * @args Not in use.
     */
    public static void main(String[] args) {
        SwircModel model = new SwircModel();
        SwircView view = new SwircView(model);
        
        view.setVisible(true);
    }
}
