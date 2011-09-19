package examples.legacy.mac;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;


/**
 * Externalized exit action taken from {@link HelloMac}.
 * 
 * @author $Author: tichy $
 */
public class MacExitAction extends AbstractAction {
    public void actionPerformed( ActionEvent e ) {
        JOptionPane.showMessageDialog( (Component) e.getSource(), 
                MacTest.getSwix().getLocalizer().getString("mis_Exit"));
        System.exit(0);
      }
}
