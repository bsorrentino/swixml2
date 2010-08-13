import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.swixml.SwingEngine;


/**
 * Localization, also shows localization for the MAC OS
 *
 * @author <a href="mailto:wolf@paulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 *
 * @since swixml (#129)
 */


public class Localization extends JFrame {

  private static final String DESCRIPTOR = "xml/localization.xml";
  SwingEngine swix = new SwingEngine( this );



  public Localization() throws Exception {
    swix.render( Localization.DESCRIPTOR ).setVisible( true );
  }


  public Action actionOptions = new AbstractAction() {
    public void actionPerformed( ActionEvent e ) {
      JOptionPane.showMessageDialog( Localization.this, "Sorry, " +swix.getLocalizer().getString("mis_Options") + " not implemented yet.");
    }
  };

  public Action actionAbout = new AbstractAction() {
    public void actionPerformed( ActionEvent e ) {
      JOptionPane.showMessageDialog( Localization.this, "This is the Mac OS X Example." );
    }
  };

 public Action actionHelp = new AbstractAction() {
    public void actionPerformed( ActionEvent e ) {
      JOptionPane.showMessageDialog( Localization.this, "Help ...." );
    }
  };

  public Action actionExit = new AbstractAction() {
    public void actionPerformed( ActionEvent e ) {
      JOptionPane.showMessageDialog( Localization.this, swix.getLocalizer().getString("mis_Exit"));
      Localization.this.windowClosing(null);
    }
  };

  /**
   * Invoked when a window is in the process of being closed.
   * The close operation can be overridden at this point.
   */
  public void windowClosing( WindowEvent e ) {
    System.exit(0);
  }

  public static void main( String[] args ) {
    try {
      new Localization();
    } catch (Exception e) {
      System.err.println( e.getMessage() );
    }
  }

}
