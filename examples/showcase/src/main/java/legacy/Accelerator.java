package legacy;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.swixml.SwingEngine;



/**
 * The Accelerator shows in the usage of accelerators.
 *
 * @author <a href="mailto:wolf@paulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 *
 * @since swixml (#101)
 */
public class Accelerator extends JDialog {
  private static final String DESCRIPTOR = "xml/accelerator.xml";
  SwingEngine swix = new SwingEngine( this );

  public Accelerator() throws Exception {
    swix.render( Accelerator.DESCRIPTOR ).setVisible( true );
  }

  public Action newAction = new AbstractAction() {
    public void actionPerformed( ActionEvent e ) {
      JOptionPane.showMessageDialog( Accelerator.this, "Sorry, not implemented yet." );
    }
  };

  public Action aboutAction = new AbstractAction() {
    public void actionPerformed( ActionEvent e ) {
      JOptionPane.showMessageDialog( Accelerator.this, "This is the Accelerator Example." );
    }
  };

  public static void main( String[] args ) {
    try {
      new Accelerator();
    } catch (Exception e) {
      System.err.println( e.getMessage() );
    }
  }

}
