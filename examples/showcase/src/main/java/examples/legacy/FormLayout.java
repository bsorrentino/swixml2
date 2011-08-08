package examples.legacy;
import javax.swing.JFrame;

import org.swixml.SwingEngine;


/**
 * The Layout class shows the use of layout managers
 *
 * @author <a href="mailto:wolf@paulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 *
 * @since swixml (#151)
 */
public class FormLayout extends JFrame{
  private static final String DESCRIPTOR = "xml/formlayout.xml";

  private FormLayout() throws Exception {
    new SwingEngine( this ).render( FormLayout.DESCRIPTOR ).setVisible( true );
  }

  public static void main( String[] args ) {
    try {
      new FormLayout();
    } catch (Exception e) {
      System.err.println( e.getMessage() );
    }
  }
}