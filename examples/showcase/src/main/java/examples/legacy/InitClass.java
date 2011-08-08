package examples.legacy;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import org.swixml.SwingEngine;



/**
 * The InitClass class demonstrates how to use the initclass attribute.
 * Date: Mar 10, 2003
 *
 * @author <a href="mailto:wolf@paulus.com">Wolf Paulus</a>
 * @version $Revision: 1.1 $
 * @since swixml 0.76
 */

public class InitClass extends JFrame {
  public Action DO_SELECT = new AbstractAction() {
    public void actionPerformed(ActionEvent e) {
      System.out.println( ((JComboBox) e.getSource()).getSelectedItem().toString() );
    }
  };

  private InitClass() throws Exception {
    new SwingEngine( this ).render( "xml/initclass.xml" ).setVisible( true );
  }

  public static void main(String[] args) {
    try {
      new InitClass();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

