import java.util.TimeZone;

import javax.swing.JFrame;

import org.swixml.Converter;
import org.swixml.ConverterLibrary;
import org.swixml.SwingEngine;



/**
 * Extend the TagLib with a new Class and a new Converter
 */
public class NewTag extends JFrame {

	SwingEngine<JFrame> swix = new SwingEngine<JFrame>(this);
  private NewTag() {
    //
    // Register a new new Converter,
    // Generally, Converters should be regsitered before Tags
    //
    ConverterLibrary.getInstance().register( TimeZone.class, (Converter) new TimeZoneConverter() );
    //
    //  Register a Tag that uses a SwingEngine itself ...
    //
    swix.getTaglib().registerTag( "xpanel", XPanel.class );
    try {
      swix.getTaglib().registerTag( "redlabel", RedLabel.class );
    } catch (Exception e) {
      System.err.println( e.getMessage() );
    }

    try {
      swix.render( "xml/newtag.xml" ).setVisible( true );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new NewTag();
  }
}
