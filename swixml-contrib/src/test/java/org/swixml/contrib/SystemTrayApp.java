/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swixml.contrib;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.jdesktop.application.Application;
import org.jdesktop.application.Resource;
import org.junit.Test;
import org.swixml.ApplicationPropertiesEnum;
import org.swixml.jsr296.SWIXMLApplication;

/**
 *
 * @author bsorrentino
 */
public class SystemTrayApp extends SWIXMLApplication {
    
    public class MainFrame extends JFrame {
        
        @Resource(key="Application.id") String id;
        @Resource(key="Application.icon") ImageIcon icon;
        
        TrayIcon trayIcon;
        
        @Override
        public void addNotify() {
            
            super.addNotify();
            
            
            PopupMenu pMenu = new PopupMenu();
            
            MenuItem mnuExit = new MenuItem("exit");
            
            mnuExit.addActionListener( new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    exit();
                }
            });
            pMenu.add( mnuExit);
            
            trayIcon = new TrayIcon(icon.getImage(), id, pMenu);
            
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();

                trayIcon.setImageAutoSize(true);
                trayIcon.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        System.out.println("In here");
                        trayIcon.displayMessage("Tester!", "Some action performed", TrayIcon.MessageType.INFO);
                    }
                });

                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    System.err.println("TrayIcon could not be added.");
                }
            }

        }
    }
    
    

    @Override
    protected void startup() {
         

        try {
            
            ApplicationPropertiesEnum.AUTO_INJECTFIELD.set(true);
            
            MainFrame mainFrame = super.render(new MainFrame());
            
            System.out.printf( "\n\tApplication id [%s]\n\tApplication icon [%s]\n", mainFrame.id, mainFrame.icon);
            show(mainFrame);


        } catch (Exception ex) {
            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        Application.launch(SystemTrayApp.class, args);

    }

    @Test
    public void dummy() {
    }
    
}
