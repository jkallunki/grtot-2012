package swirc;

/**
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
     * Returns this instance of SwircConfs.
     * @return This instance of SwircConfs
     */
    public SwircConfs getInstance() {
        if(instance == null) {
            instance = new SwircConfs();
        }
        return instance;
    }
}
