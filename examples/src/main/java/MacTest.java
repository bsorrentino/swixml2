import javax.swing.JFrame;

import org.swixml.SwingEngine;



/**
 * The HelloMac class shows a couple of the Mac specifics exposed
 * <code>HeeloMac</code> renders the GUI, which is described in hellomac.xml
 *
 * @author <a href="mailto:wolf@paulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 *
 * @since swixml 1.1
 */
public class MacTest  extends JFrame {
  private static SwingEngine swix;

  private MacTest() throws Exception {
    swix= new SwingEngine( this );
    swix.render( "xml/mactester.xml" );    
    setVisible( true );
  }

  
  //
  //  Make the class bootable
  //
  public static void main( String[] args ) throws Exception {
    new MacTest();
  }

/**
 * @return
 */
public static SwingEngine getSwix() {
    // TODO Auto-generated method stub
    return swix;
}


}