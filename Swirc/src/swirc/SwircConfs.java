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
    
    /**
     * Constructer.
     */
    protected SwircConfs() {
        serverProperties = new Properties();
        initServerProperties();
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
            File mkefile = new File("src/properties/usedServers");
        }
        catch(Exception e) {
            System.out.println(e.toString());
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
}
