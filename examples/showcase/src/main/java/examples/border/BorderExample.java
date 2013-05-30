/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package examples.border;

import javax.swing.JDialog;

import org.swixml.jsr296.SWIXMLApplication;

/**
 *
 * @author bsorrentino
 */
public class BorderExample extends SWIXMLApplication {
    public static void main(String args[]) {
        SWIXMLApplication.launch(BorderExample.class, args);
    }

    @Override
    protected void startup() {

        try {

            JDialog frame = render(new BorderDialog());

            show(frame);


        } catch (Exception e) {

            e.printStackTrace();
            exit();
        }

    }
}
