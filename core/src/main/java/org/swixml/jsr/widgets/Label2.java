/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import javax.swing.JLabel;
import org.swixml.SwingEngine;
import org.swixml.jsr295.BindingUtils;

/**
 *
 * @author sorrentino
 */
public class Label2 extends JLabel {
    String bindWith;

    public String getBindWith() {
        return bindWith;
    }

    public void setBindWith(String bindWith) {
        this.bindWith = bindWith;
        if( null!=bindWith) {
            BindingUtils.parseBind( this, "text",bindWith );
        }
    }
    
    public static void register( SwingEngine engine ) {
        engine.getTaglib().registerTag( "label2", Label2.class  );
    } 

}
