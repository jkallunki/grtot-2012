package swirc;

import java.io.*;
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
    
    /**
     * Add server to server properties
     * @param server Server to be added to properties
     */
    public void addServer(String server) {
        if(serverProperties.isEmpty()) {
            serverProperties.setProperty("servers", server);
        }
        else {
            String servers = serverProperties.getProperty("servers");
            servers = servers.concat("," + server);
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
    
    /**
     * Returns servers from properties
     * @return Servers from properties
     */
    public String[] getServers() {
        if(!serverProperties.isEmpty()) {
            String temp = serverProperties.getProperty("servers");
            return temp.split(",");
        }
        return null;
    }
    
    /**
     * Searches and returns given server
     * @param server Server to be searched
     * @return Searched server
     */
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
    
    /**
     * Sets user data 
     * @param key Key of user's data
     * @param value Value of user's data
     */
    public void setUserData(String key, String value) {
        this.userData.setProperty(key, value);
    }
    
    /**
     * Gets user data with given key
     * @param key Key of user's data
     * @return User data with given key
     */
    public String getUserData(String key) {
        return this.userData.getProperty(key);
    }
    
    /**
     * Saves user's data to properties
     */
    public void saveUserData() {
         try {
            FileOutputStream out = new FileOutputStream("src/properties/userData");
            userData.store(out, "---No Comment---");
            out.close();
        }
        catch(Exception e) {
            //TODO Saving user data failed
        }
    }
}
