package swirc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Class to store SwircModel configurations
 * @author Janne Kallunki, Jaakko Ritvanen, Ville Hämäläinen
 */
public class SwircConfs {
    private static SwircConfs instance;
    private Properties serverProperties;
    private Properties userData;
    
    /**
     * Constructer.
     */
    protected SwircConfs() {
        serverProperties = new Properties();
        initServerProperties();
        userData = new Properties();
        initUserData();
    }
    
    /**
     * Returns this instance of SwircConfs.
     * @return This instance of SwircConfs
     */
    public static SwircConfs getInstance() {
        if(instance == null) {
            instance = new SwircConfs();
        }
        return instance;
    }
    
    private void initServerProperties() {
        try {
            FileInputStream input = new FileInputStream("src/properties/usedServers");
            serverProperties.load(input);
            input.close();
        }
        catch(FileNotFoundException e) {
            System.out.println(e.toString());
            File makefile = new File("src/properties/usedServers");
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }
    
    private void initUserData() {
        try {
            FileInputStream dataIn = new FileInputStream("src/properties/userData");
            userData.load(dataIn);
            dataIn.close();
        }
        catch(FileNotFoundException e) {
            System.out.println(e.toString());
            File makefile = new File("src/properties/userData");
        }
        catch(Exception e) {
            //TODO properties not found
        }
    }
    
    public void addServer(String server) {
        if(serverProperties.isEmpty()) {
            serverProperties.setProperty("servers", server);
        }
        else {
            String servers = serverProperties.getProperty("servers");
            System.out.println(servers);
            servers = servers.concat("," + server);
            System.out.println(servers);
            serverProperties.setProperty("servers", servers);
        }
        try {
            FileOutputStream output = new FileOutputStream("src/properties/usedServers");
            serverProperties.store(output, "");
            output.close();
        }
        catch(Exception e) {
            
        }
    }
    
    public String[] getServers() {
        if(!serverProperties.isEmpty()) {
            String temp = serverProperties.getProperty("servers");
            return temp.split(",");
        }
        return null;
    }
    
    public boolean findServer(String server) {
        if(serverProperties.isEmpty()) {
            return false;
        }
        String[] servers = getServers();
        for(int i = 0; i < servers.length; i++) {
            if(servers[i].equals(server)) {
                return true;
            }
        }
        return false;
    }
    
    public void setUserData(String key, String value) {
        this.userData.setProperty(key, value);
    }
    
    public String getUserData(String key) {
        return this.userData.getProperty(key);
    }
    
    public void saveUserData() {
         try {
            FileOutputStream out = new FileOutputStream("userData");
            userData.store(out, "---No Comment---");
            out.close();
        }
        catch(Exception e) {
            //TODO Saving user data failed
        }
    }
}
