package org.swixml.contrib;

import org.swixml.contrib.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import org.jdesktop.application.Application;
import org.swixml.SwingTagLibrary;
import org.swixml.jsr296.SwingApplication;


/**
 * Unit test for simple App.
 */
public class AppTest extends SwingApplication {

    class MainFrame extends JFrame {
        JMenuButton btnMenu;

        
    }
    MainFrame dlg = new MainFrame();

    @Override
    protected void startup() {
        SwingTagLibrary.getInstance().registerTag("menubutton", JMenuButton.class);
        SwingTagLibrary.getInstance().registerTag("animatedbutton", JAnimatedButton.class);
        try {
            super.render(dlg, "swixml/Dialog1.xml");

            dlg.btnMenu.addActions(
                        new AbstractAction("open"){

                        public void actionPerformed(ActionEvent e) {
                            System.out.println( getValue(NAME) );
                        }

                         },
                        new AbstractAction("close"){

                        public void actionPerformed(ActionEvent e) {
                            System.out.println( getValue(NAME) );
                        }

                         }
                    );

            show( dlg );
        } catch (Exception ex) {
            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main( String[] args ) {

        URL url = AppTest.class.getClassLoader().getResource("./images/RecAni1.png");

        System.out.println( "url " + url );
        Application.launch(AppTest.class, args);

    }



}