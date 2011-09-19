/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package issue60;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.swixml.jsr296.SwingApplication;

/**
 *
 * @author bsorrentino
 */
public class Issue60Application extends SwingApplication{

    @Override
    protected void startup() {
        try {

            Issue60Frame frame = super.render(new Issue60Frame());

            System.out.printf( "frame.size=[%s] getDividerLocation=[%d]\n",  frame.getSize().toString(), frame.split.getDividerLocation() );

            super.show(frame);
            
            System.out.printf( "frame.size=[%s] getDividerLocation=[%d]\n",  frame.getSize().toString(), frame.split.getDividerLocation() );



        } catch (Exception ex) {
            Logger.getLogger(Issue60Application.class.getName()).log(Level.SEVERE, null, ex);
            exit();
        }
    }

	public static void main(String[] args) {
		SwingApplication.launch(Issue60Application.class, args);

	}

}
