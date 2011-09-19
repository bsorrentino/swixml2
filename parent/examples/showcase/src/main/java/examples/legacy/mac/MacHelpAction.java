package examples.legacy.mac;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;

import com.apple.eio.FileManager;


/**
 * Externalized help action taken from {@link HelloMac}.
 * 
 * @author $Author: tichy $
 */
public class MacHelpAction extends AbstractAction {
    public void actionPerformed( ActionEvent e ) {
        try {
          FileManager.openURL("http://www.swixml.org/apidocs/index.html");
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
}
