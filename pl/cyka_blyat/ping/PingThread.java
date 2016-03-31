package pl.cyka_blyat.ping;

import java.awt.MenuItem;
import java.io.IOException;
import java.net.URLConnection;

public class PingThread extends Thread {
    private final Ping ping;
    
    public PingThread(Ping ping) {
        super("Ping Thread");
        this.ping = ping;
    }
    
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                long took = System.currentTimeMillis();
                
                this.ping();
                this.updateScreen();
                
                Thread.sleep(Math.max(1L, this.ping.getCooldown() - (System.currentTimeMillis() - took)));
            } catch (Throwable ex) {
                if (ex instanceof InterruptedException) {
                    return;
                }
                
                ex.printStackTrace();
            }
        }
    }
    
    private void ping() throws Throwable {
        URLConnection connection = this.ping.getURL().openConnection();
        connection.setConnectTimeout(this.ping.getTimeout());
        
        long took = System.currentTimeMillis();
        connection.connect();
        this.ping.setLastPing(System.currentTimeMillis() - took);
    }
    
    private void updateScreen() throws Throwable {
        Tray.updateScreen(this.ping);
    }
}
