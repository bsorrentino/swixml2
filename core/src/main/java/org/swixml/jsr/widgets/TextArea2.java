/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import javax.swing.JTextArea;
import org.swixml.SwingEngine;
import org.swixml.jsr295.BindingUtils;

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
    
    public static void register( SwingEngine engine ) {
        engine.getTaglib().registerTag( "textarea2", TextArea2.class  );
    } 

}
