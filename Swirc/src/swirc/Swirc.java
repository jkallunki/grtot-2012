package swirc;

/**
 * Main-class for Swirc IRC-client. Works as a wrapper for components of MVC-model
 * and also runs the program.
 * @author Janne Kallunki
 */
public class Swirc {
   private SwircModel model;
   private SwircView view;
   
   /**
    *
    */
    public Swirc(){
        this.model = new SwircModel();
        this.view = new SwircView(model);
    }
    
    /*
     * Main method. Creates instance of Swirc and runs the program.
     * @args Not in use.
     */
    public static void main(String[] args) {
        Swirc swirc = new Swirc();     
    }
}
