package legacy;
import javax.swing.JFrame;

import org.swixml.SwingEngine;




/**
 * The Form class shows how to do a simple JGoodies FormLayout
 */
public class Form extends JFrame {
  /** Default ctor for a SwingEngine. */

  private Form() {
    try {
      SwingEngine engine = new SwingEngine(this);	
      engine.render( "xml/form.xml" ).setVisible( true );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new Form();
  }
}