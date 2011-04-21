/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.border;

import javax.swing.JFrame;
import org.swixml.jsr296.SwingApplication;

/**
 *
 * @author bsorrentino
 */
public class BorderExample extends SwingApplication {
    public static void main(String args[]) {
        SwingApplication.launch(BorderExample.class, args);
    }

    @Override
    protected void startup() {

        try {

            JFrame frame = render(new BorderFrame());

            show(frame);


        } catch (Exception e) {

            e.printStackTrace();
            exit();
        }

    }
}