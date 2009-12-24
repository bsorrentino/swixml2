package org.swixml.contrib;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.junit.Test;
import org.swixml.SwingTagLibrary;
import org.swixml.jsr296.SwingApplication;


/**
 * Unit test for simple App.
 */
public class AppTest extends SwingApplication {

    public class MainFrame extends JFrame {
        JSimpleMenuButton btnMenu;
        JAnimatedButton btnAnimated2;

        @Action
        public void button2() {
            System.out.printf( "button2 selected[%b]\n", btnAnimated2.isSelected() );
        }

        @Action
        public void action() {
            btnMenu.showPopup();
        }

        @Action
        public void action1() {
            System.out.printf( "action1 \n" );
        }

        @Action
        public void action2() {
            System.out.printf( "action2 \n" );
        }
        
    }

    MainFrame dlg = new MainFrame();

    @Override
    protected void startup() {
        SwingTagLibrary.getInstance().registerTag("menubutton", JSimpleMenuButton.class);
        SwingTagLibrary.getInstance().registerTag("animatedbutton", JAnimatedButton.class);
        try {
            super.render(dlg, "swixml/Dialog1.xml");

            show( dlg );

            dlg.btnAnimated2.start();

        } catch (Exception ex) {
            Logger.getLogger(AppTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main( String[] args ) {

        URL url = AppTest.class.getClassLoader().getResource("./images/RecAni1.png");

        System.out.println( "url " + url );
        Application.launch(AppTest.class, args);

    }

    @Test
    public void dummy() {

    }


}