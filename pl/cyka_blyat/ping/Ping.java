package pl.cyka_blyat.ping;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Ping {
    public static void main(String[] args) {
        new Ping().start();
    }
    
    public static final int DEFAULT_COOLDOWN = 1 * 1000;
    public static final int DEFAULT_TIMEOUT = 10 * 1000;
    
    private int cooldown = DEFAULT_COOLDOWN;
    private long lastPing;
    private final String name;
    private int timeout = DEFAULT_TIMEOUT;
    private final Thread thread;
    private Tray tray;
    private URL url;
    
    public Ping() {
        this.name = "Ping v1.0 by TheMolkaPL (przy patrzeniu Dimitrijijijj)";
        this.thread = new PingThread(this);
    }
    
    public void start() {
//        String host = "216.58.209.67";
        String host = "http://google.com";
        
        try {
            this.url = new URL(host);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown Hook Thread") {
            @Override
            public void run() {
                Ping.this.stop();
            }
        });
        
        this.getThread().start();
        
        if (SystemTray.isSupported()) {
            try {
                this.tray = new Tray(this);
                SystemTray.getSystemTray().add(tray);
            } catch (AWTException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void stop() {
        this.getThread().interrupt();
    }
    
    public int getCooldown() {
        return this.cooldown;
    }
    
    public long getLastPing() {
        return this.lastPing;
    }
    
    public int getTimeout() {
        return this.timeout;
    }
    
    public Thread getThread() {
        return this.thread;
    }
    
    public String getTooltip() {
        return this.name;
    }
    
    public Tray getTray() {
        return this.tray;
    }
    
    public URL getURL() {
        return this.url;
    }
    
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
    
    public void setLastPing(long lastPing) {
        this.lastPing = lastPing;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public void setURL(URL url) {
        this.url = url;
    }
}
