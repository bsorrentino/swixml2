import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;


/**
 * Demonstrates delivering of a valid action command from the ActionEvent.
 * 
 * On MacOS, this contains strings like {@link org.swixml.Parser#ATTR_MACOS_ABOUT}
 * when the attribute <code>"macos_about"</code> was set to <code>true</code>. See the
 * provided file <code>mactester.xml</code> as an usage example. This allows specifying
 * of multiple <code>"macos_*"</code> attributes for one {@link javax.swing.Action}.
 *  
 * 
 * @author $Author: tichy $
 */
public class MacMultipleAction extends AbstractAction {

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        JOptionPane.showMessageDialog( (Component) e.getSource(), "This is the Mac OS X MultipleExample. Showing "+e.getActionCommand() );
               
    }

}
