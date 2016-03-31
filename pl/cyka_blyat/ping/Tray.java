package pl.cyka_blyat.ping;

import java.awt.Color;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Tray extends TrayIcon {
    public static final String DEFAULT_ICON_NAME = "default-icon.png";
    public static final String GOOD_ICON_NAME = "good-icon.png";
    public static final String BAD_ICON_NAME = "bad-icon.png";
    public static final String WORST_ICON_NAME = "worst-icon.png";
    
    private static MenuItem menuItem;
    private final Ping ping;
    
    public Tray(Ping ping) {
        super(createImage(DEFAULT_ICON_NAME), ping.getTooltip(), createMenu());
        this.ping = ping;
        
        this.setImageAutoSize(true);
    }
    
    private static Image createImage(String icon) {
        BufferedImage image = null;
        try (InputStream input = Tray.class.getClassLoader().getResourceAsStream(icon)) {
            if (input != null) {
                image = ImageIO.read(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        if (image == null) {
            return null;
        }
        
        return image;
    }
    
    private static PopupMenu createMenu() {
        MenuItem pingItem = new MenuItem("Calculating your ping...");
        pingItem.setEnabled(false);
        menuItem = pingItem;
        
        MenuItem exitItem = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_Q));
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        PopupMenu menu = new PopupMenu();
        menu.add(pingItem);
        menu.addSeparator();
        menu.add(exitItem);
        return menu;
    }
    
    public static void updateScreen(Ping ping) {
        MenuItem item = menuItem;
        if (item == null) {
            return;
        }
        
        long lastPing = ping.getLastPing();
        item.setLabel("Your ping: " + lastPing + " ms");
        
        Image icon = null;
        if (lastPing > 1000) {
            // worst
            icon = createImage(WORST_ICON_NAME);
        } else if (lastPing > 100) {
            // bad
            icon = createImage(BAD_ICON_NAME);
        } else {
            // good
            icon = createImage(GOOD_ICON_NAME);
        }
        
        if (icon != null) {
            ping.getTray().setImage(icon);
        }
    }
}
