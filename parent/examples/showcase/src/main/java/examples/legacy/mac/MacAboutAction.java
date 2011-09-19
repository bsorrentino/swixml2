package examples.legacy.mac;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;


/**
 * Externalized AboutAction taken from {@link HelloMac}.
 * 
 * @author $Author: tichy $
 */
public class MacAboutAction extends AbstractAction {
    public void actionPerformed( ActionEvent e ) {
        JOptionPane.showMessageDialog( (Component) e.getSource(), "This is the Mac OS X Example." );
      }
   
}
