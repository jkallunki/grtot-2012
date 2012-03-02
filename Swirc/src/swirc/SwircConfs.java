package swirc;

/*
 * Class to store SwircModel configurations
 * @author Janne Kallunki, Jaakko Ritvanen, Ville Hämäläinen
 */
public class SwircConfs {
    private SwircConfs instance;
    
    /**
     * Constructer.
     */
    protected SwircConfs() {
        
    }
    
    /**
     * //TODO add fitting javacode
     */
    public SwircConfs getInstance() {
        if(instance == null) {
            instance = new SwircConfs();
        }
        return instance;
    }
}
