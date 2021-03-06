package examples.legacy;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.swixml.SwingEngine;



/**
 * The <code>Cards</code> class shows an example for the usage of a CardLayout.
 *
 * @author <a href="mailto:wolf@paulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 *
 * @since swixml #109
 */
public class Cards extends JFrame {

  private static final String DESCRIPTOR = "xml/cards.xml";
  private SwingEngine swix = new SwingEngine( this );

  /** panel with a CardLayout */
  public JPanel pnl;

  private Cards() throws Exception {
    swix.render( Cards.DESCRIPTOR ).setVisible( true );
    this.showAction.actionPerformed( null );
  }

  /** shows the next card */
  public Action nextAction = new AbstractAction() {
    public void actionPerformed( ActionEvent e ) {
      CardLayout cl = (CardLayout) ( pnl.getLayout() );
      cl.next( pnl );
    }
  };

  /** shows the card with the id requested in the actioncommand */
  public Action showAction = new AbstractAction() {
    public void actionPerformed( ActionEvent e ) {
      //System.err.println( "ActionCommand=" + e.getActionCommand() );
      CardLayout cl = (CardLayout) ( pnl.getLayout() );
      if (e!=null) {
        cl.show( pnl, e.getActionCommand() );
      }
    }
  };

  public static void main( String[] args ) {
    try {
      new Cards();
    } catch (Exception e) {
      System.err.println( e.getMessage() );
    }
  }

}
