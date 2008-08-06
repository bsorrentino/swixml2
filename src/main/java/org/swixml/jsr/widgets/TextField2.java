/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import javax.swing.JTextField;
import org.swixml.SwingEngine;
import org.swixml.jsr295.BindingUtils;

/**
 *
 * @author Sorrentino
 */
public class TextField2 extends JTextField {
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
        engine.getTaglib().registerTag( "textfield2", TextField2.class  );
    } 
    
}
