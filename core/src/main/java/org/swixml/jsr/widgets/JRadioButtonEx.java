/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swixml.jsr.widgets;

import javax.swing.JRadioButton;
import org.swixml.jsr295.BindingUtils;

/**
 *
 * @author sorrentino
 */
public class JRadioButtonEx extends JRadioButton {
    String bindWith;

    public String getBindWith() {
        return bindWith;
    }

    public void setBindWith(String bindWith) {

        this.bindWith = bindWith;
        if( null!=bindWith) {
            BindingUtils.parseBind( this, "selected", bindWith );
        }
    }

}
