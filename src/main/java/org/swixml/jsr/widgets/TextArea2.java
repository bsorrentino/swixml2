/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import javax.swing.JTextArea;
import org.swixml.jsr295.BindingUtils;
import org.swixml.jsr296.SwingApplication;

/**
 *
 * @author sorrentino
 */
public class TextArea2 extends JTextArea {
    String bindWith;

    public String getBindWith() {
        return bindWith;
    }

    public void setBindWith(String bindWith) {
        this.bindWith = bindWith;
        if( null!=bindWith) {
            BindingUtils.parseBind( this, "text", bindWith );
        }
    }
    
    public static void register( SwingApplication app ) {
        app.getSwix().getTaglib().registerTag( "textarea2", TextArea2.class  );
    } 

}
