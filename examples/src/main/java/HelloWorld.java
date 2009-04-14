import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.swixml.SwingEngine;



public class HelloWorld extends JFrame{
  /** submit counter */
  private int clicks;

  /** JTextField member gets instantiated through Swixml (look for id="tf" in xml descriptor) */
  public JTextField tf;

  /** Jlabel to display number of button clicks */
  public JLabel cnt;

  /** Action appends a '#' to the textfields content.  */
  public Action submit = new AbstractAction() {
    public void actionPerformed( ActionEvent e ) {
      tf.setText( tf.getText() + '#' );
      cnt.setText(String.valueOf( ++clicks ));
    }
  };

  /** Renders UI at construction */
  private HelloWorld() throws Exception {
    new SwingEngine( this ).render( "xml/helloworld.xml" ).setVisible( true );
  }

  /** Makes the class bootable */
  public static void main( String[] args ) throws Exception {
    new HelloWorld();
  }
}
